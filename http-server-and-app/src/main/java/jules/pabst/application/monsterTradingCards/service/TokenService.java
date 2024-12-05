package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.DTOs.LoginTokenDTO;
import jules.pabst.application.monsterTradingCards.entity.TokenRequest;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.InvalidUserCredentials;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;

import java.util.Optional;

public class TokenService {
    private final UserRepository userRepository;
    public TokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<LoginTokenDTO> authenticate(TokenRequest tokenRequest){
        try {
            Optional<User> user = this.userRepository.findUserByName(tokenRequest.getUsername());

            if (user.isPresent() && user.get().getPassword().equals(tokenRequest.getPassword())) {
                tokenRequest.setToken("%s-mtcgToken".formatted(tokenRequest.getUsername()));
                return Optional.of(new LoginTokenDTO(tokenRequest.getUsername(), tokenRequest.getToken()));
            }

            throw new InvalidUserCredentials("Invalid User Credentials");
        } catch (InvalidUserCredentials e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            throw new RuntimeException("Unexpected error during login", e);
        }

    }
}
