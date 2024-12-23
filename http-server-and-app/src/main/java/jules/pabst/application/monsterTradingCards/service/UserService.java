package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.DTOs.UserCreationDTO;
import jules.pabst.application.monsterTradingCards.DTOs.UserDTO;
import jules.pabst.application.monsterTradingCards.DTOs.UserUpdateDTO;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.InvalidUserCredentials;
import jules.pabst.application.monsterTradingCards.exception.MissingAuthorizationHeader;
import jules.pabst.application.monsterTradingCards.exception.UserAlreadyExists;
import jules.pabst.application.monsterTradingCards.exception.UserNotFound;
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

    public UserDTO getUserByPathName(String pathName, String authenticationToken) {
        if (authenticationToken.contains(pathName)) {
            Optional<User> user = userRepository.findUserByAuthenticationToken(authenticationToken);

            if (user.isPresent()) {
                return new UserDTO(
                        user.get().getUuid(),
                        user.get().getUsername(),
                        user.get().getBio(),
                        user.get().getImage(),
                        user.get().getElo(),
                        user.get().getWins(),
                        user.get().getLosses(),
                        user.get().getToken(),
                        user.get().getCredit());
            }
            throw new UserNotFound("User not found");
        }
        throw new InvalidUserCredentials("No access rights of this user's data");
    }

    public User getUserByAuthenticationToken (String authenticationToken){
        if (authenticationToken == null) {
            throw new MissingAuthorizationHeader("Authentication token cannot be null");
        }

        Optional<User> originalUser = userRepository.findUserByAuthenticationToken(authenticationToken);

        if (originalUser.isPresent()) {
            return originalUser.get();
        }

        throw new UserNotFound("User not found");
    }

    public Optional<User> readOwnerOfCard(Card card){
        return userRepository.findOwnerOfCard(card);
    }

    public UserDTO updateUserDataByUserName (UserUpdateDTO user, String authenticationToken, String pathName) {
        if (authenticationToken.contains(pathName)) {
            Optional<User> originalUser = userRepository.findUserByAuthenticationToken(authenticationToken);
            if (originalUser.isPresent()) {
                System.out.println("Authentication Token: " + authenticationToken);
                System.out.println("Original User: Token: " + originalUser.get().getToken());

                System.out.println("Authentication token equal to each other");
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

                if (user.getUsername() != null) {
                    userDTO.setUsername(user.getUsername());
                }

                if (user.getBio() != null) {
                    userDTO.setBio(user.getBio());
                }

                if (user.getImage() != null) {
                    userDTO.setImage(user.getImage());
                }

                return userRepository.updateUserDataByUserName(originalUser.get().getUsername(), userDTO);

            }
            throw new UserNotFound("User not found");
        }
        throw new InvalidUserCredentials("No access rights of this user's data");
    }

    public void updateUserByUuid(User user){
       userRepository.updateUserByUuid(user);
    }
}