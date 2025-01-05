package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.TradingDeal;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.CardsNotFound;
import jules.pabst.application.monsterTradingCards.exception.NotAuthorized;
import jules.pabst.application.monsterTradingCards.repository.TradeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TradeService {
    TradeRepository tradeRepository;
    UserService userService;
    CardService cardService;

    public TradeService(TradeRepository tradeRepository, UserService userService, CardService cardService) {
        this.tradeRepository = tradeRepository;
        this.userService = userService;
        this.cardService = cardService;
    }

    public List<TradingDeal> readOpenTradeDeals(String auth){
        userService.getUserByAuthenticationToken(auth);
        return tradeRepository.findAllTradingDeals();
    }

    private void updateCards(Card card1, Card card2){
        List<Card> cardsToUpdate = new ArrayList<>();
        String idTrader1 = card1.getOwnerUuid();
        card1.setOwnerUuid(card2.getOwnerUuid());
        card2.setOwnerUuid(idTrader1);

        cardsToUpdate.add(card1);
        cardsToUpdate.add(card2);

        cardService.updateCards(cardsToUpdate);
    }

    private TradingDeal updateTradingDeal(TradingDeal tradingDeal1, TradingDeal tradingDeal2){
        tradingDeal1.setTradeStatus("completed");
        tradingDeal2.setTradeStatus("completed");

        tradeRepository.updateTradeDealStatus(tradingDeal2);
        return tradeRepository.updateTradeDealStatus(tradingDeal1);
    }

    public TradingDeal createTradeDeal(String auth, TradingDeal tradingDeal){
        User user = userService.getUserByAuthenticationToken(auth);
        Card card = cardService.checkIfCardIsOwnedByTraderAndIsNotInDeck(user, tradingDeal);

        List<TradingDeal> openTradingDeals = tradeRepository.findAllOpenTradingDeals();
        List<Card> cards = cardService.findCardsById(openTradingDeals);
        tradeRepository.save(tradingDeal);

        if(!openTradingDeals.isEmpty()){
            return trade(card, cards, tradingDeal, openTradingDeals);
        }

        return tradingDeal;
    }

    public TradingDeal trade(Card card, List<Card> cards, TradingDeal tradingDeal, List<TradingDeal> openTradingDeals){
        for(int i = 0; i < cards.size(); i++){
            if(cards.get(i).getDamage() > tradingDeal.getMinimumDamage() && card.getDamage() > openTradingDeals.get(i).getMinimumDamage()){
                // check if card fits spell constraints for chosen TradeDeal
                if(openTradingDeals.get(i).getType().getName().equals("spell") && card.getName().contains("spell")){
                    // check if chosen card fits constraints for current tradingDeal
                    Optional<TradingDeal> updatedTradingDeal = checkForRequirementsAndExecuteUpdating(card, cards, tradingDeal, openTradingDeals, i);
                    if(updatedTradingDeal.isPresent()){
                        return updatedTradingDeal.get();
                    }
                }

                // check if card fits monster constraints for chosen TradeDeal
                if(openTradingDeals.get(i).getType().getName().equals("monster") && !card.getName().contains("spell")){
                    // check if chosen card fits constraints for current tradingDeal
                    Optional<TradingDeal> updatedTradingDeal = checkForRequirementsAndExecuteUpdating(card, cards, tradingDeal, openTradingDeals, i);
                    if(updatedTradingDeal.isPresent()){
                        return updatedTradingDeal.get();
                    }
                }
            }
        }
        throw new CardsNotFound("No open Trade Deal with the requested card type and damage found");
    }

    public Optional<TradingDeal> checkForRequirementsAndExecuteUpdating(Card card, List<Card> cards, TradingDeal tradingDeal, List<TradingDeal> openTradingDeals, int i){
        if(tradingDeal.getType().getName().equals("spell") && cards.get(i).getName().contains("spell")){
            if(cards.get(i).getOwnerUuid().equals(card.getOwnerUuid())){
                throw new NotAuthorized("You cannot trade with yourself");
            }

            updateCards(card, cards.get(i));
            return Optional.of(updateTradingDeal(tradingDeal, openTradingDeals.get(i)));
        }

        if(tradingDeal.getType().getName().equals("monster") && !cards.get(i).getName().contains("spell")){
            if(cards.get(i).getOwnerUuid().equals(card.getOwnerUuid())){
                throw new NotAuthorized("You cannot trade with yourself");
            }

            updateCards(card, cards.get(i));
            return Optional.of(updateTradingDeal(tradingDeal, openTradingDeals.get(i)));
        }

        return Optional.empty();
    }

    public List<TradingDeal> deleteTradeDeals(String auth, String tradeId){
        User user = userService.getUserByAuthenticationToken(auth);
        List<TradingDeal> openTradingDeals = tradeRepository.findAllOpenTradingDeals();

        for(int i = 0; i < openTradingDeals.size(); i++){
            if(openTradingDeals.get(i).getId().equals(tradeId)){
                List<Card> cards = cardService.findCardsById(openTradingDeals);

                if(cards.getFirst().getOwnerUuid().equals(user.getUuid())){
                    tradeRepository.delete(tradeId);
                    // return openTradingDealsAfterDeletion
                    return tradeRepository.findAllOpenTradingDeals();
                }

                throw new NotAuthorized("Trading Deal does not belong to user");
            }
        }

        throw(new NotAuthorized("This card has not been traded by this user"));
    }
}
