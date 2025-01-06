package jules.pabst.application.monsterTradingCards.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.NotRightAmountOfCards;
import jules.pabst.application.monsterTradingCards.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeckServiceTest {
    private CardRepository cardRepository;
    private UserService userService;
    private DeckService deckService;

    @BeforeEach
    void setUp() {
        cardRepository = mock(CardRepository.class);
        userService = mock(UserService.class);
        deckService = new DeckService(cardRepository, userService);
    }

    @Test
    void givenValidAuthenticationToken_whenReadDeck_thenReturnDeck() {
        // Arrange
        String authToken = "user-auth-token";
        User user = new User("user-uuid", "username", "password", "", "", 0, 0, 0, authToken, 0);

        List<Card> expectedDeck = Arrays.asList(
                new Card("card1", "WaterGoblin", 50, user.getUuid(), user.getUuid()),
                new Card("card2", "FireElf", 70, user.getUuid(), user.getUuid())
        );

        when(userService.getUserByAuthenticationToken(authToken)).thenReturn(user);
        when(cardRepository.findCardsByDeck(user)).thenReturn(expectedDeck);

        // Act
        List<Card> actualDeck = deckService.readDeck(authToken);

        // Assert
        assertEquals(expectedDeck, actualDeck);
        verify(userService, times(1)).getUserByAuthenticationToken(authToken);
        verify(cardRepository, times(1)).findCardsByDeck(user);
    }

    @Test
    void givenInvalidNumberOfCards_whenConfigureDeck_thenThrowException() {
        // Arrange
        String authToken = "user-auth-token";
        List<String> cardIds = Arrays.asList("card1", "card2"); // Less than 4 cards

        // Act & Assert
        NotRightAmountOfCards exception = assertThrows(NotRightAmountOfCards.class, () ->
                deckService.configureDeck(authToken, cardIds)
        );

        assertEquals("The number of provided  cards is not 4", exception.getMessage());
        verify(userService, never()).getUserByAuthenticationToken(any());
        verify(cardRepository, never()).findCardsById(any());
    }
}
