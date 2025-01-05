package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.TradingDeal;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.CardsNotFound;
import jules.pabst.application.monsterTradingCards.exception.NotAuthorized;
import jules.pabst.application.monsterTradingCards.repository.TradeRepository;

import java.util.ArrayList;
import java.util.List;

public class TradeService {
    UserService userService;
    TradeRepository tradeRepository;
    CardService cardService;

    public TradeService(UserService userService, TradeRepository tradeRepository, CardService cardService) {
        this.userService = userService;
        this.tradeRepository = tradeRepository;
        this.cardService = cardService;
    }

    public List<TradingDeal> readOpenTradeDeals(String auth){
        User user = userService.getUserByAuthenticationToken(auth);
        List<TradingDeal> tradingDeals = tradeRepository.findAllTradingDeals();
        return tradingDeals;
    }

    private List<Card> updateCards(Card card1, Card card2){
        List<Card> cardsToUpdate = new ArrayList<>();
        String idTrader1 = card1.getOwnerUuid();
        card1.setOwnerUuid(card2.getOwnerUuid());
        card2.setOwnerUuid(idTrader1);

        cardsToUpdate.add(card1);
        cardsToUpdate.add(card2);

        return cardService.updateCards(cardsToUpdate);
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
        List<Card> updatedCards = new ArrayList<>();
        tradeRepository.save(tradingDeal);

        for(int i = 0; i < cards.size(); i++){
            if(cards.get(i).getDamage() > tradingDeal.getMinimumDamage() && card.getDamage() > openTradingDeals.get(i).getMinimumDamage()){
                if(card.getName().contains("spell") && openTradingDeals.get(i).getType().getName().equals("spell")){
                    if(tradingDeal.getType().getName().equals("spell") && cards.get(i).getName().contains("spell")){
                        updatedCards = updateCards(card, cards.get(i));
                        return updateTradingDeal(tradingDeal, openTradingDeals.get(i));
                    }

                    if(tradingDeal.getType().getName().equals("monster") && !cards.get(i).getName().contains("spell")){
                        updatedCards = updateCards(card, cards.get(i));
                    }
                }

                if(!card.getName().contains("spell") && !openTradingDeals.get(i).getType().getName().equals("spell")){
                    if(tradingDeal.getType().getName().equals("spell") && cards.get(i).getName().contains("spell")){
                        updatedCards = updateCards(card, cards.get(i));
                    }

                    if(tradingDeal.getType().getName().equals("monster") && !cards.get(i).getName().contains("spell")){
                        updatedCards = updateCards(card, cards.get(i));
                    }
                }
            }

            throw(new CardsNotFound("No open Trade Deal with the requested card type and damage found"));
        }
        throw(new NotAuthorized("Card is not belonging to the provided user"));
    }

//    public List<TradeDTO> deleteTradeDeals(String auth, String tradeId){
//        User user = userService.getUserByAuthenticationToken(auth);
//        List<TradeDTO> tradeDTOS = tradeRepository.findAllTradesByUserUuid(user);
//
//        for(TradeDTO tradeDTO : tradeDTOS){
//            if(tradeDTO.getTradeId().equals(tradeId)){
//                tradeRepository.delete(tradeId);
//                return tradeRepository.findAllTradesByUserUuid(user);
//            }
//        }
//
//        throw(new NotAuthorized("This card has not been traded by this user"));
//    }
}
