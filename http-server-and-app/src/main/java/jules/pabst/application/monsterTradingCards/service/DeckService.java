package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.repository.CardRepository;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;

public class DeckService {
    CardRepository cardRepository;
    UserRepository userRepository;

    public DeckService(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

}
