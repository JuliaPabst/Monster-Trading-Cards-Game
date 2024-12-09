package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.CardPackage;
import jules.pabst.application.monsterTradingCards.entity.CardType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CardRepository {
    public List<Optional<Card>> findAll();
    public Card save(Card card);
    public List<Card> findCardsByPackage(List<CardPackage>  cardPackages);
}
