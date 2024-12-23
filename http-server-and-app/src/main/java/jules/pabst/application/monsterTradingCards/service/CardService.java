package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.CardPackage;
import jules.pabst.application.monsterTradingCards.entity.TradingDeal;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.CardsNotFound;
import jules.pabst.application.monsterTradingCards.exception.NoPackagesOwned;
import jules.pabst.application.monsterTradingCards.exception.NotAuthorized;
import jules.pabst.application.monsterTradingCards.exception.NotNull;
import jules.pabst.application.monsterTradingCards.repository.CardRepository;
import jules.pabst.application.monsterTradingCards.repository.PackageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CardService {

    private final CardRepository cardRepository;
    private final PackageRepository packageRepository;

    public CardService(CardRepository cardRepository, PackageRepository packageRepository) {
       this.cardRepository = cardRepository;
       this.packageRepository = packageRepository;
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
            if(!packagesOwnedByUser.isEmpty()){
                return cardRepository.findCardsByPackage(packagesOwnedByUser);
            }
            throw new CardsNotFound("This user does not own any cards");
        } catch(NoPackagesOwned e){
            throw new NoPackagesOwned("This user does not own any cards");
        }

    }

    public Optional<Card> checkIfCardIsOwnedByTrader(User user, TradingDeal tradingDeal){
        List<Card> cards = cardRepository.findCardsByUserUuid(user);
        if (cards.isEmpty()) {
            throw new CardsNotFound("No cards belonging to the provided user");
        }

        for(Card card : cards) {
            System.out.println("Current card id: " + card.getId());
            System.out.println("Trading deal id: " + tradingDeal.getCardToTrade());
            if(card.getId().equals(tradingDeal.getCardToTrade())){
                return Optional.of(card);
            }
        }

        return Optional.empty();
    }

    public Optional<Card> readCardNotOwnedByTraderWithDamage(User user, TradingDeal tradingDeal){
        List<Card> cards = cardRepository.findCardsNotOwnedByUserWithDamage(user, tradingDeal);
        if (cards.isEmpty()) {
            throw new CardsNotFound("No cards that have a damage bigger than the requested damage and that don't belong to the trader");
        }

        for(Card card : cards) {
            System.out.println("Current card name: " + card.getName());
            if(tradingDeal.getType().getName().contains("spell")){
                if(card.getName().contains("Spell")){
                    return Optional.of(card);
                }
            }
            return Optional.of(card);
        }

        return Optional.empty();
    }
}
