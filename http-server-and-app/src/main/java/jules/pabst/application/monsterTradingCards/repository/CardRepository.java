package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CardRepository {
    List<Optional<Card>> findAll();
    Card save(Card card);
    List<Card> findCardsByDeck(User user);
    List<Card> findCardsByUserUuid(User user);
    List<Card> findCardsNotOwnedByUserWithDamage(User user, TradingDeal tradingDeal);
    List<Card> findCardsById(List<String> cardIds);
    List<Card> updateDeckUserId(List<Card> cards);
}
