package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.repository.CardMemoryRepository;
import jules.pabst.application.monsterTradingCards.repository.CardRepository;

import java.util.List;

public class CardService {
    private final CardRepository cardRepository;

    public CardService() {
        this.cardRepository = new CardMemoryRepository();
    }

    public List<Card> getAll(){
        return cardRepository.findAll();
    }
}
