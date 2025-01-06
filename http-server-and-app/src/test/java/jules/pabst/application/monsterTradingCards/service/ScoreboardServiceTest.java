package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.DTOs.ScoreboardDTO;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScoreboardServiceTest {
    private UserRepository userRepository;
    private ScoreboardService scoreboardService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        scoreboardService = new ScoreboardService(userRepository);
    }

    @Test
    void givenDifferentEloScores_whenReadSortedScoreboard_thenCorrectPlacing() {
        // Arrange
        User user1 = new User("uuid1", "Alice", "password1", "", "", 1500, 10, 5, "", 0);
        User user2 = new User("uuid2", "Bob", "password2", "", "", 1200, 7, 8, "", 0);
        User user3 = new User("uuid3", "Charlie", "password3", "", "", 1800, 15, 3, "", 0);

        when(userRepository.findAllUsers()).thenReturn(Arrays.asList(user1, user2, user3));

        // Act
        List<ScoreboardDTO> scoreboard = scoreboardService.readSortedScoreboard();

        // Assert
        assertEquals(3, scoreboard.size());
        assertEquals(1, scoreboard.get(0).getPlace());
        assertEquals("Charlie", scoreboard.get(0).getUsername());
        assertEquals(1800, scoreboard.get(0).getElo());
        assertEquals(2, scoreboard.get(1).getPlace());
        assertEquals("Alice", scoreboard.get(1).getUsername());
        assertEquals(1500, scoreboard.get(1).getElo());
        assertEquals(3, scoreboard.get(2).getPlace());
        assertEquals("Bob", scoreboard.get(2).getUsername());
        assertEquals(1200, scoreboard.get(2).getElo());
    }

    @Test
    void givenSameEloScores_whenReadSortedScoreboard_thenCorrectPlacing() {
        // Arrange
        User user1 = new User("uuid1", "Alice", "password1", "", "", 1500, 10, 5, "", 0);
        User user2 = new User("uuid2", "Bob", "password2", "", "", 1500, 7, 8, "", 0);
        User user3 = new User("uuid3", "Charlie", "password3", "", "", 1800, 15, 3, "", 0);

        when(userRepository.findAllUsers()).thenReturn(Arrays.asList(user1, user2, user3));

        // Act
        List<ScoreboardDTO> scoreboard = scoreboardService.readSortedScoreboard();

        // Assert
        assertEquals(3, scoreboard.size());
        assertEquals(1, scoreboard.get(0).getPlace());
        assertEquals("Charlie", scoreboard.get(0).getUsername());
        assertEquals(1800, scoreboard.get(0).getElo());
        assertEquals(2, scoreboard.get(1).getPlace());
        assertEquals("Alice", scoreboard.get(1).getUsername());
        assertEquals(1500, scoreboard.get(1).getElo());
        assertEquals(2, scoreboard.get(2).getPlace());
        assertEquals("Bob", scoreboard.get(2).getUsername());
        assertEquals(1500, scoreboard.get(2).getElo());
    }
}
