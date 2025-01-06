package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.TradingDeal;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.CardsNotFound;
import jules.pabst.application.monsterTradingCards.exception.NotAuthorized;
import jules.pabst.application.monsterTradingCards.exception.TradingDealNotFound;
import jules.pabst.application.monsterTradingCards.exception.TradingDealRequirementsNotMet;
import jules.pabst.application.monsterTradingCards.repository.TradeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;

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

    public TradingDeal createTradeDeal(String auth, TradingDeal tradingDeal){
        User user = userService.getUserByAuthenticationToken(auth);
        cardService.checkIfCardIsOwnedByTraderAndIsNotInDeck(user, tradingDeal);

        tradeRepository.save(tradingDeal);

        return tradingDeal;
    }

    public TradingDeal trade(String auth, String tradeId, String cardId){
        User user = userService.getUserByAuthenticationToken(auth);
        List<TradingDeal> openTradingDeals = tradeRepository.findAllOpenTradingDeals();

        if(openTradingDeals.isEmpty()){
            throw new TradingDealNotFound("There are no open trading deals");
        }

        Optional<TradingDeal> currentTradingDeal = Optional.empty();

        for(TradingDeal tradingDeal : openTradingDeals){
            if(tradingDeal.getId().equals(tradeId)){
                currentTradingDeal = Optional.of(tradingDeal);
            }
        }

        if(currentTradingDeal.isEmpty()){
            throw new TradingDealNotFound("Selected trading deal not available");
        }

        Optional<Card> tradingDealCard = Optional.empty();
        tradingDealCard = cardService.findCardById(currentTradingDeal.get().getCardToTrade());
        if(tradingDealCard.isEmpty()){
            throw new CardsNotFound("Trading deal card not found");
        }

        Optional<Card> cardToTrade = cardService.findCardById(cardId);
        if(cardToTrade.isEmpty()){
            throw new CardsNotFound("Card to trade with not found");
        }

        return checkForRequirementsAndExecuteUpdating(tradingDealCard.get(), cardToTrade.get(), currentTradingDeal.get(), user);
    }

    private TradingDeal checkForRequirementsAndExecuteUpdating(Card tradingDealCard, Card cardToTradeWith, TradingDeal currentTradingDeal, User user) {
        if (cardToTradeWith.getOwnerUuid().equals(user.getUuid())) {
            throw new NotAuthorized("You cannot trade with yourself");
        }

        if (currentTradingDeal.getTradeStatus().getName().equals("completed")) {
            throw new TradingDealNotFound("Trading Deal already completed in another trade");
        }

        if (cardToTradeWith.getDamage() > currentTradingDeal.getMinimumDamage()) {
            // check if card fits spell constraints for chosen TradeDeal
            if (currentTradingDeal.getType().getName().equals("spell") && cardToTradeWith.getName().contains("spell")) {
                updateCards(tradingDealCard, cardToTradeWith);
                return updateTradingDeal(currentTradingDeal);
            }

            // check if card fits monster constraints for chosen TradeDeal
            if (currentTradingDeal.getType().getName().equals("monster") && !cardToTradeWith.getName().contains("spell")) {
                updateCards(tradingDealCard, cardToTradeWith);
                return updateTradingDeal(currentTradingDeal);
            }
        }
        throw new TradingDealRequirementsNotMet("Selected Card to trade does not fulfill the requirements of the selected trading deal");
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

    private TradingDeal updateTradingDeal(TradingDeal currentTradingDeal){
        currentTradingDeal.setTradeStatus("completed");

        return tradeRepository.updateTradeDealStatus(currentTradingDeal);
    }

    public List<TradingDeal> deleteTradeDeals(String auth, String tradeId){
        User user = userService.getUserByAuthenticationToken(auth);
        List<TradingDeal> openTradingDeals = tradeRepository.findAllOpenTradingDeals();

        for(int i = 0; i < openTradingDeals.size(); i++){
            if(openTradingDeals.get(i).getId().equals(tradeId)){
                List<Card> cards = cardService.findCardsByTradingId(openTradingDeals);

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

