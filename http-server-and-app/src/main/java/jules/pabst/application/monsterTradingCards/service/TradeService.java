package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.DTOs.TradeDTO;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.CardPackage;
import jules.pabst.application.monsterTradingCards.entity.TradingDeal;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.CardsNotFound;
import jules.pabst.application.monsterTradingCards.exception.NotAuthorized;
import jules.pabst.application.monsterTradingCards.repository.TradeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TradeService {
    UserService userService;
    TradeRepository tradeRepository;
    CardService cardService;

    public TradeService(UserService userService, TradeRepository tradeRepository, CardService cardService) {
        this.userService = userService;
        this.tradeRepository = tradeRepository;
        this.cardService = cardService;
    }

    public List<TradeDTO> readTradeDeals(String auth){
        User user = userService.getUserByAuthenticationToken(auth);
        List<TradeDTO> tradeDTOS = tradeRepository.findAllTradesByUserUuid(user);
        return tradeDTOS;
    }

    public TradeDTO createTradeDeal(String auth, TradingDeal tradingDeal){
        User user = userService.getUserByAuthenticationToken(auth);
        Optional<Card> card = cardService.checkIfCardIsOwnedByTrader(user, tradingDeal);
        if(card.isPresent()){
            Optional<Card> selectedCard = cardService.readCardNotOwnedByTraderWithDamage(user, tradingDeal);
            if(selectedCard.isPresent()){
                Optional<User> ownerOfSelectedCard = userService.readOwnerOfCard(selectedCard.get());
                if(ownerOfSelectedCard.isPresent()){
                    TradeDTO tradeDTO = new TradeDTO(tradingDeal.getId(), tradingDeal.getCardToTrade(), selectedCard.get().getId(), user.getUuid(), ownerOfSelectedCard.get().getUuid(), tradingDeal.getType().getName(), tradingDeal.getMinimumDamage());
                    return tradeRepository.save(tradeDTO);
                }
                TradeDTO tradeDTO = new TradeDTO(tradingDeal.getId(), tradingDeal.getCardToTrade(), selectedCard.get().getId(), user.getUuid(), null, tradingDeal.getType().getName(), tradingDeal.getMinimumDamage());
                return tradeRepository.save(tradeDTO);
            }
            throw(new CardsNotFound("No card with the requested card type and damage found"));
        }
        throw(new NotAuthorized("Card is not belonging to the provided user"));
    }

    public List<TradeDTO> deleteTradeDeals(String auth, String tradeId){
        User user = userService.getUserByAuthenticationToken(auth);
        List<TradeDTO> tradeDTOS = tradeRepository.findAllTradesByUserUuid(user);

        for(TradeDTO tradeDTO : tradeDTOS){
            if(tradeDTO.getTradeId().equals(tradeId)){
                tradeRepository.delete(tradeId);
                return tradeRepository.findAllTradesByUserUuid(user);
            }
        }

        throw(new NotAuthorized("This card has not been traded by this user"));
    }
}
