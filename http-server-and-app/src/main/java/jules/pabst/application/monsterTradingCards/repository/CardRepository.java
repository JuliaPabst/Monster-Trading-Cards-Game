package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.CardPackage;
import jules.pabst.application.monsterTradingCards.entity.CardType;
import jules.pabst.application.monsterTradingCards.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CardRepository {
    public List<Optional<Card>> findAll();
    public Card save(Card card);
    public List<Card> findCardsByPackage(List<CardPackage>  cardPackages);
    public List<Card> findCardsByDeck(User user);
    public List<Card> findCardsById(List<String> cardIds);
    public List<Card> updateDeckUserId(List<Card> cards);
}
