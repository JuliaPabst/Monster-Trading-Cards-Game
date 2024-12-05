package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.CardType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardMemoryRepository implements CardRepository {
    public List<Optional<Card>> findAll() {
//        Card testCard1 = new Card("1", CardType.WaterGoblin, 1);
//        Card testCard2 = new Card("2", CardType.WaterElf, 2);
//        Card testCard3 = new Card("3", CardType.WaterSpell, 3);

        List<Optional<Card>> cards = new ArrayList<>();
//        cards.add(testCard1);
//        cards.add(testCard2);
//        cards.add(testCard3);

        return cards;
    }

    @Override
    public Card save(Card card) {
        return null;
    }
}
