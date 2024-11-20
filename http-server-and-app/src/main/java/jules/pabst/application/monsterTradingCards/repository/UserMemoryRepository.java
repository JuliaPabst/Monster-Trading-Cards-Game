package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserMemoryRepository implements UserRepository {

    private final List<User> users;

    public UserMemoryRepository()  {
        this.users = new ArrayList<>();
    }

    @Override
    public User save(User user) {
        user.setId(Integer.toString(this.users.size() + 1));
        this.users.add(user);

        return user;
    }

}
