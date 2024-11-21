package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.UserAlreadyExists;
import jules.pabst.application.monsterTradingCards.repository.UserMemoryRepository;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserMemoryRepository();
    }

    public User create(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        User existingUser = userRepository.findUserByName(user.getUsername());

        if (existingUser != null) {
            throw new UserAlreadyExists("User already exists");
        }

        return userRepository.save(user);
    }

    public User getUserByName(String name) {
        return userRepository.findUserByName(name);
    }
}
