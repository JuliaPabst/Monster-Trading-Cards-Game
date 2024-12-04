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
        Optional<User> user = this.userRepository.findUserByName(tokenRequest.getUsername());
        if(user.isPresent() && user.get().getPassword().equals(tokenRequest.getPassword())){
            Optional<LoginTokenDTO> loginTokenDTO = new LoginTokenDTO(tokenRequest.getUsername(), tokenRequest.getToken());
            tokenRequest.setToken("%s-mtcgToken".formatted(tokenRequest.getUsername()));
            return Optional.of(loginTokenDTO);
        }

        throw new InvalidUserCredentials("Invalid User Credentials");
    }
}
