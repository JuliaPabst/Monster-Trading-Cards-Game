package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.entity.TradingDeal;

import java.util.List;

public interface TradeRepository {

    TradingDeal save(TradingDeal tradeDeal);
    TradingDeal updateTradeDealStatus(TradingDeal tradeDeal);
    List<TradingDeal> findAllTradingDeals();
    List<TradingDeal> findAllOpenTradingDeals();

    String delete(String tradeId);
}
