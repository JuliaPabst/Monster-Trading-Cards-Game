package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.DTOs.StatsDTO;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.UserNotFound;
import jules.pabst.application.monsterTradingCards.repository.CardRepository;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;

import java.util.Optional;

public class StatsService {
    UserRepository userRepository;

    public StatsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public StatsDTO readStats(String authentificationToken) {
        Optional<User> user = userRepository.findUserByAuthenticationToken(authentificationToken);

        if(user.isPresent()) {
            int elo = user.get().getWins()*3 - user.get().getLosses()*5;
            return new StatsDTO(user.get().getUsername(), elo, user.get().getWins(), user.get().getLosses());
        }

        throw new UserNotFound("User not found");
    }
}
