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

    public LoginTokenDTO authenticate(TokenRequest tokenRequest){
        Optional<User> userOptional = this.userRepository.findUserByName(tokenRequest.getUsername());

        if (userOptional.isPresent() && userOptional.get().getPassword().equals(tokenRequest.getPassword())) {
            String token ="%s-mtcgToken".formatted(tokenRequest.getUsername());
            tokenRequest.setToken(token);
            User user = userOptional.get();
            user.setToken(token);

            this.userRepository.updateUserByUuid(user);
            return new LoginTokenDTO(tokenRequest.getUsername(), tokenRequest.getToken());
        }

        throw new InvalidUserCredentials("Invalid User Credentials");
    }
}
