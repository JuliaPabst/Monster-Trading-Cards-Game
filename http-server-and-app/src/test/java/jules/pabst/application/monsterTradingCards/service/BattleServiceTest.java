package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.repository.CardRepository;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BattleServiceTest {

    @Test
    public void give_validUsersAndCards_when_battle_then_producesDetailedLog() {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        CardRepository cardRepository = mock(CardRepository.class);
        UserService userService = mock(UserService.class);
        CardService cardService = mock(CardService.class);

        BattleService battleService = new BattleService(userRepository, cardRepository, userService, cardService);

        User player1 = new User("player1-uuid", "Player1", "password", "", "", 1200, 10, 5, "player1-token", 20);
        User player2 = new User("player2-uuid", "Player2", "password", "", "", 1250, 15, 3, "player2-token", 20);

        when(userService.getUserByAuthenticationToken("player1-token")).thenReturn(player1);
        when(userService.getUserByAuthenticationToken("player2-token")).thenReturn(player2);

        Card card1 = new Card("card1", "WaterSpell", 50, "player1-uuid", "player1-uuid");
        Card card2 = new Card("card2", "FireSpell", 40, "player2-uuid", "player2-uuid");

        when(cardRepository.findCardsByDeck(player1)).thenReturn(List.of(card1));
        when(cardRepository.findCardsByDeck(player2)).thenReturn(List.of(card2));

        // Act
        String battleLog = battleService.battle("player1-token", "player2-token");

        // Assert
        assertNotNull(battleLog);
        assertTrue(battleLog.contains("Player1") && battleLog.contains("Player2"));
        assertTrue(battleLog.contains("wins the battle!") || battleLog.contains("It's a draw!"));
        verify(cardService, atLeastOnce()).updateDeckuserId(any(Card.class));
        verify(userRepository, times(2)).updateUserByUuid(any(User.class));
    }

    @Test
    public void give_goblinAndDragon_when_battle_then_logSpecialRuleApplied() {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        CardRepository cardRepository = mock(CardRepository.class);
        UserService userService = mock(UserService.class);
        CardService cardService = mock(CardService.class);

        BattleService battleService = new BattleService(userRepository, cardRepository, userService, cardService);

        User player1 = new User("player1-uuid", "Player1", "password", "", "", 1200, 10, 5, "player1-token", 20);
        User player2 = new User("player2-uuid", "Player2", "password", "", "", 1250, 15, 3, "player2-token", 20);

        when(userService.getUserByAuthenticationToken("player1-token")).thenReturn(player1);
        when(userService.getUserByAuthenticationToken("player2-token")).thenReturn(player2);

        Card card1 = new Card("card1", "RegularGoblin", 50, "player1-uuid", "player1-uuid");
        Card card2 = new Card("card2", "Dragon", 100, "player2-uuid", "player2-uuid");

        when(cardRepository.findCardsByDeck(player1)).thenReturn(List.of(card1));
        when(cardRepository.findCardsByDeck(player2)).thenReturn(List.of(card2));

        // Act
        String battleLog = battleService.battle("player1-token", "player2-token");

        // Assert
        assertTrue(battleLog.contains("Goblin is too afraid to attack Dragon!"));
        verifyNoInteractions(cardService); // Ensure no deck updates occur for this special case
    }

    @Test
    public void give_wizzardAndOrk_when_battle_then_logSpecialRuleApplied() {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        CardRepository cardRepository = mock(CardRepository.class);
        UserService userService = mock(UserService.class);
        CardService cardService = mock(CardService.class);

        BattleService battleService = new BattleService(userRepository, cardRepository, userService, cardService);

        User player1 = new User("player1-uuid", "Player1", "password", "", "", 1200, 10, 5, "player1-token", 20);
        User player2 = new User("player2-uuid", "Player2", "password", "", "", 1250, 15, 3, "player2-token", 20);

        when(userService.getUserByAuthenticationToken("player1-token")).thenReturn(player1);
        when(userService.getUserByAuthenticationToken("player2-token")).thenReturn(player2);

        Card card1 = new Card("card1", "Wizzard", 50, "player1-uuid", "player1-uuid");
        Card card2 = new Card("card2", "Ork", 100, "player2-uuid", "player2-uuid");

        when(cardRepository.findCardsByDeck(player1)).thenReturn(List.of(card1));
        when(cardRepository.findCardsByDeck(player2)).thenReturn(List.of(card2));

        // Act
        String battleLog = battleService.battle("player1-token", "player2-token");

        // Assert
        assertTrue(battleLog.contains("Wizzard controls the Ork; Ork cannot attack!"));
    }

    @Test
    public void give_knightAndWaterSpell_when_battle_then_knightDrowns() {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        CardRepository cardRepository = mock(CardRepository.class);
        UserService userService = mock(UserService.class);
        CardService cardService = mock(CardService.class);

        BattleService battleService = new BattleService(userRepository, cardRepository, userService, cardService);

        User player1 = new User("player1-uuid", "Player1", "password", "", "", 1200, 10, 5, "player1-token", 20);
        User player2 = new User("player2-uuid", "Player2", "password", "", "", 1250, 15, 3, "player2-token", 20);

        when(userService.getUserByAuthenticationToken("player1-token")).thenReturn(player1);
        when(userService.getUserByAuthenticationToken("player2-token")).thenReturn(player2);

        Card card1 = new Card("card1", "Knight", 80, "player1-uuid", "player1-uuid");
        Card card2 = new Card("card2", "WaterSpell", 100, "player2-uuid", "player2-uuid");

        when(cardRepository.findCardsByDeck(player1)).thenReturn(List.of(card1));
        when(cardRepository.findCardsByDeck(player2)).thenReturn(List.of(card2));

        // Act
        String battleLog = battleService.battle("player1-token", "player2-token");

        // Assert
        assertTrue(battleLog.contains("Knight drowns instantly due to WaterSpell!"));
    }

    @Test
    public void give_krakenAndSpell_when_battle_then_krakenImmune() {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        CardRepository cardRepository = mock(CardRepository.class);
        UserService userService = mock(UserService.class);
        CardService cardService = mock(CardService.class);

        BattleService battleService = new BattleService(userRepository, cardRepository, userService, cardService);

        User player1 = new User("player1-uuid", "Player1", "password", "", "", 1200, 10, 5, "player1-token", 20);
        User player2 = new User("player2-uuid", "Player2", "password", "", "", 1250, 15, 3, "player2-token", 20);

        when(userService.getUserByAuthenticationToken("player1-token")).thenReturn(player1);
        when(userService.getUserByAuthenticationToken("player2-token")).thenReturn(player2);

        Card card1 = new Card("card1", "Kraken", 120, "player1-uuid", "player1-uuid");
        Card card2 = new Card("card2", "WaterSpell", 80, "player2-uuid", "player2-uuid");

        when(cardRepository.findCardsByDeck(player1)).thenReturn(List.of(card1));
        when(cardRepository.findCardsByDeck(player2)).thenReturn(List.of(card2));

        // Act
        String battleLog = battleService.battle("player1-token", "player2-token");

        // Assert
        assertTrue(battleLog.contains("Kraken is immune to spells!"));
    }

    @Test
    public void give_fireElfAndDragon_when_battle_then_fireElfEvades() {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        CardRepository cardRepository = mock(CardRepository.class);
        UserService userService = mock(UserService.class);
        CardService cardService = mock(CardService.class);

        BattleService battleService = new BattleService(userRepository, cardRepository, userService, cardService);

        User player1 = new User("player1-uuid", "Player1", "password", "", "", 1200, 10, 5, "player1-token", 20);
        User player2 = new User("player2-uuid", "Player2", "password", "", "", 1250, 15, 3, "player2-token", 20);

        when(userService.getUserByAuthenticationToken("player1-token")).thenReturn(player1);
        when(userService.getUserByAuthenticationToken("player2-token")).thenReturn(player2);

        Card card1 = new Card("card1", "FireElf", 70, "player1-uuid", "player1-uuid");
        Card card2 = new Card("card2", "Dragon", 150, "player2-uuid", "player2-uuid");

        when(cardRepository.findCardsByDeck(player1)).thenReturn(List.of(card1));
        when(cardRepository.findCardsByDeck(player2)).thenReturn(List.of(card2));

        // Act
        String battleLog = battleService.battle("player1-token", "player2-token");

        // Assert
        assertTrue(battleLog.contains("FireElf evades Dragon's attack!"));
    }

    @Test
    public void give_waterSpellAndFireSpell_when_battle_then_waterSpellDealsDoubleDamage() {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        CardRepository cardRepository = mock(CardRepository.class);
        UserService userService = mock(UserService.class);
        CardService cardService = mock(CardService.class);

        BattleService battleService = new BattleService(userRepository, cardRepository, userService, cardService);

        User player1 = new User("player1-uuid", "Player1", "password", "", "", 1200, 10, 5, "player1-token", 20);
        User player2 = new User("player2-uuid", "Player2", "password", "", "", 1250, 15, 3, "player2-token", 20);

        when(userService.getUserByAuthenticationToken("player1-token")).thenReturn(player1);
        when(userService.getUserByAuthenticationToken("player2-token")).thenReturn(player2);

        Card card1 = new Card("card1", "WaterSpell", 50, "player1-uuid", "player1-uuid");
        Card card2 = new Card("card2", "FireSpell", 50, "player2-uuid", "player2-uuid");

        when(cardRepository.findCardsByDeck(player1)).thenReturn(List.of(card1));
        when(cardRepository.findCardsByDeck(player2)).thenReturn(List.of(card2));

        // Act
        String battleLog = battleService.battle("player1-token", "player2-token");

        // Assert
        assertTrue(battleLog.contains("WaterSpell wins the round!"));
    }

    @Test
    public void give_twoMonsters_when_battle_then_elementTypeHasNoEffect() {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        CardRepository cardRepository = mock(CardRepository.class);
        UserService userService = mock(UserService.class);
        CardService cardService = mock(CardService.class);

        BattleService battleService = new BattleService(userRepository, cardRepository, userService, cardService);

        User player1 = new User("player1-uuid", "Player1", "password", "", "", 1200, 10, 5, "player1-token", 20);
        User player2 = new User("player2-uuid", "Player2", "password", "", "", 1250, 15, 3, "player2-token", 20);

        when(userService.getUserByAuthenticationToken("player1-token")).thenReturn(player1);
        when(userService.getUserByAuthenticationToken("player2-token")).thenReturn(player2);

        Card card1 = new Card("card1", "FireGoblin", 60, "player1-uuid", "player1-uuid");
        Card card2 = new Card("card2", "WaterTroll", 80, "player2-uuid", "player2-uuid");

        when(cardRepository.findCardsByDeck(player1)).thenReturn(List.of(card1));
        when(cardRepository.findCardsByDeck(player2)).thenReturn(List.of(card2));

        // Act
        String battleLog = battleService.battle("player1-token", "player2-token");

        // Assert
        assertTrue(battleLog.contains("WaterTroll wins the round!"));
        assertFalse(battleLog.contains("element type")); // Ensure no element effect is applied
    }


    @Test
    public void give_noWinnerAfter100Rounds_when_battle_then_gameEndsInDraw() {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        CardRepository cardRepository = mock(CardRepository.class);
        UserService userService = mock(UserService.class);
        CardService cardService = mock(CardService.class);

        BattleService battleService = new BattleService(userRepository, cardRepository, userService, cardService);

        User player1 = new User("player1-uuid", "Player1", "password", "", "", 1200, 10, 5, "player1-token", 20);
        User player2 = new User("player2-uuid", "Player2", "password", "", "", 1250, 15, 3, "player2-token", 20);

        when(userService.getUserByAuthenticationToken("player1-token")).thenReturn(player1);
        when(userService.getUserByAuthenticationToken("player2-token")).thenReturn(player2);

        Card card1 = new Card("card1", "RegularElf", 50, "player1-uuid", "player1-uuid");
        Card card2 = new Card("card2", "RegularElf", 50, "player2-uuid", "player2-uuid");

        // Mocking decks with identical cards to ensure a draw
        when(cardRepository.findCardsByDeck(player1)).thenReturn(List.of(card1));
        when(cardRepository.findCardsByDeck(player2)).thenReturn(List.of(card2));

        // Act
        String battleLog = battleService.battle("player1-token", "player2-token");

        // Assert
        assertTrue(battleLog.contains("Round 100"));
        assertTrue(battleLog.contains("It's a draw!"));
    }

    @Test
    public void give_endlessBattle_when_battle_then_stopsAfter100Rounds() {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        CardRepository cardRepository = mock(CardRepository.class);
        UserService userService = mock(UserService.class);
        CardService cardService = mock(CardService.class);

        BattleService battleService = new BattleService(userRepository, cardRepository, userService, cardService);

        User player1 = new User("player1-uuid", "Player1", "password", "", "", 1200, 10, 5, "player1-token", 20);
        User player2 = new User("player2-uuid", "Player2", "password", "", "", 1250, 15, 3, "player2-token", 20);

        when(userService.getUserByAuthenticationToken("player1-token")).thenReturn(player1);
        when(userService.getUserByAuthenticationToken("player2-token")).thenReturn(player2);

        Card card1 = new Card("card1", "RegularGoblin", 50, "player1-uuid", "player1-uuid");
        Card card2 = new Card("card2", "RegularGoblin", 50, "player2-uuid", "player2-uuid");

        // Simulate decks with only one card each to force an endless battle scenario
        when(cardRepository.findCardsByDeck(player1)).thenReturn(List.of(card1));
        when(cardRepository.findCardsByDeck(player2)).thenReturn(List.of(card2));

        // Act
        String battleLog = battleService.battle("player1-token", "player2-token");

        // Assert
        assertTrue(battleLog.contains("Round 100"), "Battle should stop after 100 rounds.");
        assertTrue(battleLog.contains("It's a draw!"), "If no winner is determined after 100 rounds, it should be declared a draw.");
        assertFalse(battleLog.contains("Round 101"), "The battle must not exceed 100 rounds.");
    }

}
