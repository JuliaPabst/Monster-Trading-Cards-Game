package jules.pabst.application.monsterTradingCards.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.CardPackage;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.CardNotOwned;
import jules.pabst.application.monsterTradingCards.exception.NotRightAmountOfCards;
import jules.pabst.application.monsterTradingCards.exception.UserNotFound;
import jules.pabst.application.monsterTradingCards.repository.CardRepository;
import jules.pabst.application.monsterTradingCards.repository.PackageRepository;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeckService {
    CardRepository cardRepository;
    UserRepository userRepository;
    PackageRepository packageRepository;
    private final ObjectMapper objectMapper;


    public DeckService(CardRepository cardRepository, UserRepository userRepository, PackageRepository packageRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.packageRepository = packageRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new Jdk8Module());
        this.objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    public List<Card> readDeck(String authenticationToken) {
        User user = getUser(authenticationToken);
        return cardRepository.findCardsByDeck(user);
    }

    public List<Card> configureDeck(String authenticationToken, List<String> cardIds) throws JsonProcessingException {
        if(cardIds.size() != 4){
            throw new NotRightAmountOfCards("The number of provided  cards is not 4");
        }
        User user = getUser(authenticationToken);
        List<Card> cards = new ArrayList<>();
        List<CardPackage> packages = new ArrayList<>();

        packages = packageRepository.findPackagesByOwner(user);
        cards = cardRepository.findCardsById(cardIds);

        boolean packageOwnedByUser = false;
        for (Card card : cards) {
            for(CardPackage cardPackage : packages) {
                if (cardPackage.getId().equals(card.getPackageId())) {
                    packageOwnedByUser = true;
                    break;
                }
            }

            if(!packageOwnedByUser) {
                List<Card> originalDeck = cardRepository.findCardsByDeck(user);
                throw(new CardNotOwned("Original Deck: " + String.valueOf(objectMapper.writeValueAsString(originalDeck))));
            }

            card.setDeckUserId(user.getUuid());
            System.out.println("Deck user id set:" + card.getDeckUserId());

            packageOwnedByUser = false;
        }
        return cardRepository.updateDeckUserId(cards);
    }

    public User getUser(String authenticationToken) throws UserNotFound {
        String token = authenticationToken.split(" ")[1];
        String name = token.split("-")[0];

        System.out.println("Username: " + name);

        Optional<User> user = userRepository.findUserByName(name);

        return user.orElseThrow(() -> new UserNotFound("User not found"));
    }
}
