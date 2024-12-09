package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.UserNotFound;
import jules.pabst.application.monsterTradingCards.repository.CardRepository;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class DeckService {
    CardRepository cardRepository;
    UserRepository userRepository;

    public DeckService(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    public List<Card> readDeck(String authenticationToken) {
        String token = authenticationToken.split(" ")[1];
        String name = token.split("-")[0];

        System.out.println("Username: " + name);

        Optional<User> user = userRepository.findUserByName(name);

        if (user.isPresent()) {
            return cardRepository.findCardsByDeck(user.get());
        }

        throw new UserNotFound("User not found");
    }
}
