package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.DTOs.UserDTO;
import jules.pabst.application.monsterTradingCards.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    List<User> findAllUsers();
    Optional<User> findUserByName(String name);
    Optional<User>findUserByAuthenticationToken(String authenticationToken);

    int readCurrentCredit(User user);

    void updateUserByUuid(User user);
    UserDTO updateUserDataByUserName(String originalUsername, UserDTO userDTO);
}
