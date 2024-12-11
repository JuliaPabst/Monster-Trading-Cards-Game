package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.DTOs.UserDTO;
import jules.pabst.application.monsterTradingCards.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findUserByName(String name);
    //Continue here
    Optional<User>findUserByAuthenticationToken(String authenticationToken);

    void updateCredits(User user);

    void updateToken(User user, String token);

    int readCurrentCredit(User user);

    UserDTO updateUserData(String originalUsername, UserDTO userDTO);
}
