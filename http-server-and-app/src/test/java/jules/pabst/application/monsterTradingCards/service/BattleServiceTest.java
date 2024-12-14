package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.DTOs.UserCreationDTO;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class BattleServiceTest {
    @Test
    public void give_nonExistingUser_when_create_then_userCreationDTO(){
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        User testUser = new User();
        testUser.setUsername("testUsername");

        when(userRepository.findUserByName(any())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserService userService = new UserService(userRepository);

        // Act
        UserCreationDTO createdUser = userService.create(testUser);

        // Assert
        assertNotNull(createdUser);
    }

}