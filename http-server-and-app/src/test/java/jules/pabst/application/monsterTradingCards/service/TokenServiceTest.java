package jules.pabst.application.monsterTradingCards.service;

import static org.junit.jupiter.api.Assertions.*;

import jules.pabst.application.monsterTradingCards.DTOs.LoginTokenDTO;
import jules.pabst.application.monsterTradingCards.DTOs.UserCreationDTO;
import jules.pabst.application.monsterTradingCards.entity.TokenRequest;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;
import net.bytebuddy.description.ByteCodeElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

  @Test
    public void give_matchingTokenRequest_when_authenticate_then_LoginTokenDTO() {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("password123");

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setUsername("testUser");
        tokenRequest.setPassword("password123");

        when(userRepository.findUserByName("testUser")).thenReturn(Optional.of(testUser));
        TokenService tokenService = new TokenService(userRepository);

        // Act
        LoginTokenDTO loginTokenDTO = tokenService.authenticate(tokenRequest);

        // Assert
        assertNotNull(loginTokenDTO);
        assertEquals("testUser", loginTokenDTO.getUsername());
        assertTrue(loginTokenDTO.getToken().contains("testUser"));
    }
}