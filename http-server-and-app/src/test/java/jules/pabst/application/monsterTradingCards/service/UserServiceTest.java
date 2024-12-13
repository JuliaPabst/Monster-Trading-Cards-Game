package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.DTOs.UserCreationDTO;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.UserAlreadyExists;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class UserServiceTest {

    @Test
    public void give_noUser_when_create_then_userCreationDTO(){
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        UserService userService = new UserService(userRepository);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.create(null));
    }

   @Test
    public void give_existingUser_when_create_then_userCreationDTO(){
       // Arrange
       UserRepository userRepository = mock(UserRepository.class);
       User existingUser = new User();
       existingUser.setUsername("testUsername");

       User newUser = new User();
       newUser.setUsername("testUsername");

       when(userRepository.findUserByName("testUsername")).thenReturn(Optional.of(existingUser));
       UserService userService = new UserService(userRepository);

       // Act & Assert
       assertThrows(UserAlreadyExists.class, () -> userService.create(newUser));
   }

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