package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.DTOs.TradeDTO;
import jules.pabst.application.monsterTradingCards.entity.User;
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

    public List<TradeDTO> readTradeDeals(String auth){
        User user = userService.getUserByAuthenticationToken(auth);
        List<TradeDTO> tradeDTOS = tradeRepository.findAllTradesByUserUuid(user);
        return tradeDTOS;
    }
}
