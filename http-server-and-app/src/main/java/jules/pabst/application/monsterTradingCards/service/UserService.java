package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.DTOs.UserCreationDTO;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.UserAlreadyExists;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserCreationDTO create(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        Optional<User>  existingUser = userRepository.findUserByName(user.getUsername());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExists("User already exists");
        }

        user.setUuid(UUID.randomUUID().toString());

        User createdUser = userRepository.save(user);

        return new UserCreationDTO(createdUser.getUsername(), createdUser.getToken());
    }

    public Optional<User> getUserByName(String name) {
        return userRepository.findUserByName(name);
    }
}
