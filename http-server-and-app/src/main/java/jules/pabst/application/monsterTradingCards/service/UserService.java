package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.DTOs.UserCreationDTO;
import jules.pabst.application.monsterTradingCards.DTOs.UserDTO;
import jules.pabst.application.monsterTradingCards.DTOs.UserUpdateDTO;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.InvalidUserCredentials;
import jules.pabst.application.monsterTradingCards.exception.UpdatingUserFailed;
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

    public UserDTO getUserByName(String name, String authenticationToken) {
        Optional <User> user1 = getUserByAuthenticationToken(authenticationToken);
        Optional<User> user2 = userRepository.findUserByName(name);
        if (user1.isPresent() && user2.isPresent()) {

            if(user1.get().getUuid().equals(user2.get().getUuid())){
                return new UserDTO(
                        user1.get().getUuid(),
                        user1.get().getUsername(),
                        user1.get().getBio(),
                        user1.get().getImage(),
                        user1.get().getElo(),
                        user1.get().getWins(),
                        user1.get().getLosses(),
                        user1.get().getToken(),
                        user1.get().getCredit());
            }
            throw new InvalidUserCredentials("No access rights of this user's data");
        }
        throw new UserNotFound("User not found");
    }

    public Optional<User> getUserByAuthenticationToken(String authenticationToken) {
        String token = authenticationToken.split(" ")[1];
        String name = token.split("-")[0];

        System.out.println("Username: " + name);

        return userRepository.findUserByName(name);
    }

    public UserDTO updateUserData(UserUpdateDTO user, String authenticationToken){
        Optional<User> originalUser = getUserByAuthenticationToken(authenticationToken);
        if(originalUser.isPresent()) {
        UserDTO userDTO = new UserDTO(
                originalUser.get().getUuid(),
                originalUser.get().getUsername(),
                originalUser.get().getBio(),
                originalUser.get().getImage(),
                originalUser.get().getElo(),
                originalUser.get().getWins(),
                originalUser.get().getLosses(),
                originalUser.get().getToken(),
                originalUser.get().getCredit());

        if (user.getName() != null) {
            userDTO.setUsername(user.getName());
        }

        if (user.getBio() != null) {
            userDTO.setBio(user.getBio());
        }

        if (user.getImage() != null) {
            userDTO.setImage(user.getImage());
        }
        return userRepository.updateUserData(originalUser.get().getUsername(), userDTO);
        }
        throw new UserNotFound("User not found");

    }
}
