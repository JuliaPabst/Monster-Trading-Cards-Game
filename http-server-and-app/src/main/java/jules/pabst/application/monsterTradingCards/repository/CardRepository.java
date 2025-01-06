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
    List<Card> findCardsNotBelongingToAnyUser(User user);
    List<Card> findCardsById(List<String> cardIds);
    Card updateCard(Card card);
    List<Card> updateDeckUserId(List<Card> cards);
}
