package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.InvalidUserCredentials;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserMemoryRepository implements UserRepository {

    private final List<User> users;

    public UserMemoryRepository()  {
        this.users = new ArrayList<>();
    }

    @Override
    public User save(User user) {
        user.setId(Integer.toString(this.users.size() + 1));
        user.setToken("%s-mtcgToken".formatted(user.getUsername()));
        this.users.add(user);

        return user;
    }

    @Override
    public Optional<User> findUserByName(String username){
        for(User user : this.users){
            if(user.getUsername().equals(username)){
                return Optional.of(user);
            }

        }
        return Optional.empty();
    }

    @Override
    public Optional<User> authenticate(String username, String password) {
       for(User user : this.users){
            if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                return Optional.of(user);
            }
        }
       throw new InvalidUserCredentials("Invalid username or password");
    }
}
