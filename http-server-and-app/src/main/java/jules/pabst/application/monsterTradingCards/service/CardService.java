package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.TradingDeal;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.*;
import jules.pabst.application.monsterTradingCards.repository.CardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardService {

    private final CardRepository cardRepository;
    private final UserService userService;

    public CardService(CardRepository cardRepository, UserService userService) {
       this.cardRepository = cardRepository;
       this.userService = userService;
    }

    public List<Card> createPackage(String authToken, List<Card> cards) {
        if(authToken.equals("admin-mtcgToken")){
            List<Card> newCards = new ArrayList<>();
            cards.forEach(card -> {
               Card newCard = createCard(card);
               newCards.add(newCard);
            });

            return newCards;
        }

        throw new NotAuthorized("Only admins can create packages");
    }

    public Card createCard(Card card) {
        if (card == null) {
            throw new NotNull("Card cannot be null");
        }

        if (card.getName() == null) {
            throw new NotNull("Card name cannot be null");
        }

        System.out.println("This is one card being created" + card.getName());
        System.out.println("This is the id: " + card.getId());

        card = cardRepository.save(card);

        return card;
    }

    public List<Card> readByUserToken(User user){
        try{
            List<Card> cardOwnedByUser = cardRepository.findCardsByUserUuid(user);
            if(cardOwnedByUser.isEmpty()){
                throw new CardsNotFound("This user does not own any cards");
            }
            return cardOwnedByUser;
        } catch(NoPackagesOwned e){
            throw new NoPackagesOwned("This user does not own any cards");
        }
    }


    public List<Card> checkCreditAndAquire(String authtoken){
        User user = userService.getUserByAuthenticationToken(authtoken);
        System.out.println("User credit: %d".formatted(user.getCredit()));
        if(user.getCredit()>=5){
            List<Card> cardsToAquire = new ArrayList<>();
            List<Card> cardsWithoutOwner = cardRepository.findCardsNotBelongingToAnyUser(user);
            if(cardsWithoutOwner.size() < 5){
                throw new CardsNotFound("Not enough cards available");
            }

            for(int i = 0; i < 5; i++){
                cardsWithoutOwner.get(i).setOwnerUuid(user.getUuid());
                cardRepository.updateCard(cardsWithoutOwner.get(i));
                cardsToAquire.add(cardsWithoutOwner.get(i));
            }

            user.setCredit(user.getCredit()-5);

            userService.updateUserByUuid(user);

            return cardsToAquire;
        }

        throw(new NotEnoughCredit("Not enough credit"));
    }

    public Card checkIfCardIsOwnedByTraderAndIsNotInDeck(User user, TradingDeal tradingDeal){
        List<Card> cards = cardRepository.findCardsByUserUuid(user);

        for(Card card : cards) {
            System.out.println("Current card id: " + card.getId());
            System.out.println("Trading deal id: " + tradingDeal.getCardToTrade());
            if(card.getId().equals(tradingDeal.getCardToTrade())){
               if(card.getDeckUserId() == null){
                   return card;
               }
                throw new CardIsPartOfDeck("Traded card cannot be a part of the deck");
            }
        }

        throw new NotAuthorized("Card does not belong to the user");
    }

    public Card updateDeckuserId(Card card){
        card = cardRepository.updateCard(card);
        return card;
    }

    public List<Card> updateCards(List<Card> cards){
        List<Card> updatedCards = new ArrayList<>();
        for(Card card : cards){
            card = cardRepository.updateCard(card);
            updatedCards.add(card);
        }
        return updatedCards;
    }

    public List<Card> findCardsByTradingId(List<TradingDeal> tradingDeals){
        List<String> ids = new ArrayList<>();
        for(TradingDeal tradingDeal : tradingDeals){
            ids.add(tradingDeal.getCardToTrade());
        }
        return cardRepository.findCardsById(ids);
    }

    public Optional<Card> findCardById(String cardId){
        List<String> ids = new ArrayList<>();
        ids.add(cardId);
        List<Card> card = cardRepository.findCardsById(ids);

        if(card.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(card.get(0));
    }
}
