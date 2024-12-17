package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.DTOs.TradeDTO;
import jules.pabst.application.monsterTradingCards.entity.User;

import java.util.List;

public interface TradeRepository {

    TradeDTO save(TradeDTO tradeDTO);

    List<TradeDTO> findAllTradesByUserUuid(User user);
}
