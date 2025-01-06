package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.TradingDeal;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.CardsNotFound;
import jules.pabst.application.monsterTradingCards.exception.NotAuthorized;
import jules.pabst.application.monsterTradingCards.exception.TradingDealNotFound;
import jules.pabst.application.monsterTradingCards.exception.TradingDealRequirementsNotMet;
import jules.pabst.application.monsterTradingCards.repository.TradeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private UserService userService;

    @Mock
    private CardService cardService;

    private TradeService tradeService;

    @BeforeEach
    void setUp() {
        tradeService = new TradeService(tradeRepository, userService, cardService);
    }

    @Test
    void givenInvalidCard_whenCreateTradeDeal_thenThrowNotAuthorized() {
        // Arrange
        User user = new User("uuid1", "Alice", "password", "", "", 1500, 10, 5, "token1", 20);
        TradingDeal tradingDeal = new TradingDeal("trade1", "invalidCard", "monster", 30, "open");

        when(userService.getUserByAuthenticationToken("valid-token")).thenReturn(user);
        doThrow(NotAuthorized.class).when(cardService).checkIfCardIsOwnedByTraderAndIsNotInDeck(user, tradingDeal);

        // Act & Assert
        assertThrows(NotAuthorized.class, () -> tradeService.createTradeDeal("valid-token", tradingDeal));
        verify(tradeRepository, never()).save(any(TradingDeal.class));
    }

    @Test
    void givenNonExistentTradingDeal_whenTrade_thenThrowTradingDealNotFound() {
        // Arrange
        User user = new User("uuid1", "Alice", "password", "", "", 1500, 10, 5, "token1", 20);

        when(userService.getUserByAuthenticationToken("valid-token")).thenReturn(user);
        when(tradeRepository.findAllOpenTradingDeals()).thenReturn(Arrays.asList());

        // Act & Assert
        assertThrows(TradingDealNotFound.class, () -> tradeService.trade("valid-token", "trade1", "card1"));
    }

    @Test
    void givenValidTradingDeal_whenTrade_thenSuccess() {
        // Arrange
        User user = new User("uuid1", "Alice", "password", "", "", 1500, 10, 5, "token1", 20);
        TradingDeal tradingDeal = new TradingDeal("trade1", "card1", "monster", 30, "open");
        Card cardToTrade = new Card("card2", "FireGoblin", 40, "uuid2", null);
        Card tradingDealCard = new Card("card1", "WaterGoblin", 35, "uuid1", null);

        when(userService.getUserByAuthenticationToken("valid-token")).thenReturn(user);
        when(tradeRepository.findAllOpenTradingDeals()).thenReturn(Arrays.asList(tradingDeal));
        when(cardService.findCardById("card1")).thenReturn(Optional.of(tradingDealCard));
        when(cardService.findCardById("card2")).thenReturn(Optional.of(cardToTrade));
        when(tradeRepository.updateTradeDealStatus(tradingDeal)).thenReturn(tradingDeal); // Mock updateTradeDealStatus to return the updated tradingDeal

        // Act
        TradingDeal result = tradeService.trade("valid-token", "trade1", "card2");

        // Assert
        verify(tradeRepository, times(1)).updateTradeDealStatus(tradingDeal);
        assertNotNull(result);
        assertEquals("completed", result.getTradeStatus().getName());
    }

}
