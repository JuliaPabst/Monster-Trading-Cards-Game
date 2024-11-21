package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.entity.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    User findUserByName(String name);
}
