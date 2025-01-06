package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.TradingDeal;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.*;
import jules.pabst.application.monsterTradingCards.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceTest {

    private CardRepository cardRepository;
    private UserService userService;
    private CardService cardService;

    @BeforeEach
    void setUp() {
        cardRepository = mock(CardRepository.class);
        userService = mock(UserService.class);
        cardService = new CardService(cardRepository, userService);
    }

    @Test
    void givenUserAndValidTradingDeal_whenCheckIfCardOwnedByTraderAndNotInDeck_thenReturnCard() {
        // Arrange
        User user = new User("username", "password");
        TradingDeal tradingDeal = new TradingDeal("trade1", "card1", "monster", 20, "open");
        List<Card> userCards = Arrays.asList(
                new Card("card1", "FireGoblin", 50, user.getUuid(), null),
                new Card("card2", "WaterSpell", 30, user.getUuid(), "deck1")
        );

        when(cardRepository.findCardsByUserUuid(user)).thenReturn(userCards);

        // Act
        Card result = cardService.checkIfCardIsOwnedByTraderAndIsNotInDeck(user, tradingDeal);

        // Assert
        assertNotNull(result);
        assertEquals("card1", result.getId());
        verify(cardRepository, times(1)).findCardsByUserUuid(user);
    }

    @Test
    void givenUserAndCardInDeck_whenCheckIfCardOwnedByTraderAndNotInDeck_thenThrowException() {
        // Arrange
        User user = new User("username", "password");
        TradingDeal tradingDeal = new TradingDeal("trade1", "card2", "monster", 20, "open");
        List<Card> userCards = Arrays.asList(
                new Card("card1", "FireGoblin", 50, user.getUuid(), null),
                new Card("card2", "WaterSpell", 30, user.getUuid(), "deck1")
        );

        when(cardRepository.findCardsByUserUuid(user)).thenReturn(userCards);

        // Act & Assert
        assertThrows(CardIsPartOfDeck.class, () -> cardService.checkIfCardIsOwnedByTraderAndIsNotInDeck(user, tradingDeal));
    }

    @Test
    void givenUserWithSufficientCredit_whenCheckCreditAndAcquire_thenReturnCards() {
        // Arrange
        String authToken = "userToken";
        User user = new User("username", "password");
        user.setCredit(10);
        List<Card> cardsWithoutOwner = Arrays.asList(
                new Card("card1", "FireGoblin", 50, null, null),
                new Card("card2", "WaterSpell", 30, null, null),
                new Card("card3", "RegularSpell", 40, null, null),
                new Card("card4", "WaterGoblin", 20, null, null),
                new Card("card5", "FireSpell", 10, null, null)
        );

        when(userService.getUserByAuthenticationToken(authToken)).thenReturn(user);
        when(cardRepository.findCardsNotBelongingToAnyUser(user)).thenReturn(cardsWithoutOwner);
        when(cardRepository.updateCard(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        List<Card> acquiredCards = cardService.checkCreditAndAquire(authToken);

        // Assert
        assertEquals(5, acquiredCards.size());
        verify(cardRepository, times(5)).updateCard(any(Card.class));
        assertEquals(5, user.getCredit());
        verify(userService, times(1)).updateUserByUuid(user);
    }

    @Test
    void givenUserWithoutSufficientCredit_whenCheckCreditAndAcquire_thenThrowException() {
        // Arrange
        String authToken = "userToken";
        User user = new User("username", "password");
        user.setCredit(3);

        when(userService.getUserByAuthenticationToken(authToken)).thenReturn(user);

        // Act & Assert
        assertThrows(NotEnoughCredit.class, () -> cardService.checkCreditAndAquire(authToken));
        verify(cardRepository, never()).findCardsNotBelongingToAnyUser(user);
        verify(userService, never()).updateUserByUuid(user);
    }
}
