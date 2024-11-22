package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.entity.TokenRequest;
import jules.pabst.application.monsterTradingCards.exception.InvalidUserCredentials;
import jules.pabst.application.monsterTradingCards.repository.UserMemoryRepository;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;

import java.util.Optional;

public class TokenService {
    private final UserRepository userRepository;
    public TokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<TokenRequest> authenticate(TokenRequest tokenRequest){
        if(this.userRepository.authenticate(tokenRequest.getUsername(), tokenRequest.getPassword()).isPresent()){
            tokenRequest.setToken("%s-mtcgToken".formatted(tokenRequest.getUsername()));
            return Optional.of(tokenRequest);
        }

        throw new InvalidUserCredentials("Invalid User Credentials");
    }
}
