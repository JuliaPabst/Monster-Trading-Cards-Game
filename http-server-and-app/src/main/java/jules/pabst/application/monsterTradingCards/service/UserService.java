package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.DTOs.UserCreationDTO;
import jules.pabst.application.monsterTradingCards.DTOs.UserDTO;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.UserAlreadyExists;
import jules.pabst.application.monsterTradingCards.exception.UserNotFound;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;

import java.util.List;
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

    public UserDTO getUserByName(String name) {
        Optional<User> user = userRepository.findUserByName(name);
        if (user.isPresent()) {
            UserDTO userDTO = new UserDTO(
                    user.get().getUuid(),
                    user.get().getUsername(),
                    user.get().getBio(),
                    user.get().getImage(),
                    user.get().getElo(),
                    user.get().getWins(),
                    user.get().getLosses(),
                    user.get().getCredit());
            return userDTO;
        }
        throw new UserNotFound("User not found");
    }

    public Optional<User> getUserByAuthenticationToken(String authenticationToken) {
        String token = authenticationToken.split(" ")[1];
        String name = token.split("-")[0];

        System.out.println("Username: " + name);

        return userRepository.findUserByName(name);
    }
}
