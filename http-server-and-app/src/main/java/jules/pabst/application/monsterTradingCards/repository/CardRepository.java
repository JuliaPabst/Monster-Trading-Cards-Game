package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.CardType;

import java.util.ArrayList;
import java.util.List;

public interface CardRepository {
    public List<Card> read();
}
