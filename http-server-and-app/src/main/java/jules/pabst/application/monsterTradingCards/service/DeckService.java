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
import jules.pabst.application.monsterTradingCards.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeckService {
    CardRepository cardRepository;
    UserService userService;
    private final ObjectMapper objectMapper;


    public DeckService(CardRepository cardRepository, UserService userService) {
        this.cardRepository = cardRepository;
        this.userService = userService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new Jdk8Module());
        this.objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    public List<Card> readDeck(String authenticationToken) {
        User user = userService.getUserByAuthenticationToken(authenticationToken);
        return cardRepository.findCardsByDeck(user);
    }

    public List<Card> configureDeck(String authenticationToken, List<String> cardIds) throws JsonProcessingException {
        if(cardIds.size() != 4){
            throw new NotRightAmountOfCards("The number of provided  cards is not 4");
        }
        User user = userService.getUserByAuthenticationToken(authenticationToken);
        List<Card> cards = new ArrayList<>();

        cards = cardRepository.findCardsById(cardIds);

        for (Card card : cards) {
            if(!card.getOwnerUuid().equals(user.getUuid())){
                List<Card> originalDeck = cardRepository.findCardsByDeck(user);
                throw(new CardNotOwned("Original Deck: " + String.valueOf(objectMapper.writeValueAsString(originalDeck))));
            }

            card.setDeckUserId(user.getUuid());
            System.out.println("Deck user id set:" + card.getDeckUserId());
        }

        return cardRepository.updateDeckUserId(cards);
    }
}
