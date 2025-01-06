package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.DTOs.UserDTO;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.User;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;

import jules.pabst.application.monsterTradingCards.data.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UserDbRepository implements UserRepository {
    private final static String NEW_USER
            = "INSERT INTO users (uuid, username, password, credit) VALUES (?, ?, ?, ?)";
    private final static String FIND_ALL_USERS = "SELECT * FROM users";
    private final static String FIND_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    private final static String FIND_USER_BY_TOKEN = "SELECT * FROM users WHERE token = ?";
    private final static String FIND_OWNER_OF_CARD = "SELECT u.* FROM users u JOIN packages p ON u.uuid = p.owner_id JOIN cards c ON c.package_id = p.id WHERE c.id = ? ";
    private final static String READ_CURRENT_CREDIT = "SELECT credit FROM users WHERE username = ?";
    private final static String UPDATE_USER_BY_UUID = "UPDATE users SET username = ?, password = ?, bio = ?, image = ?, elo = ?, wins = ?, losses = ?, token = ?, credit = ? WHERE uuid = ?";
    private final static String UPDATE_USERDATA_BY_USERNAME = "UPDATE users SET username = ?, bio = ?, image= ? WHERE username = ?";
    private final ConnectionPool connectionPool;
    public UserDbRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public User save(User user) {
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(NEW_USER)
        ) {
            preparedStatement.setString(1, user.getUuid());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, 20);
            preparedStatement.execute();
            user.setToken("%s-mtcgToken".formatted(user.getUsername()));
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_USERS)
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User newUser = new User(resultSet.getString("uuid"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("bio"), resultSet.getString("image"), resultSet.getInt("elo"), resultSet.getInt("wins"), resultSet.getInt("losses"), resultSet.getString("token"), resultSet.getInt("credit"));
                users.add(newUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return users;
    }

    @Override
    public Optional<User> findUserByName(String name) {
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_USERNAME)
        ) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new User(resultSet.getString("uuid"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("bio"), resultSet.getString("image"), resultSet.getInt("elo"), resultSet.getInt("wins"), resultSet.getInt("losses"), resultSet.getString("token"), resultSet.getInt("credit")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User>findUserByAuthenticationToken(String authenticationToken){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_TOKEN)
        ) {
            preparedStatement.setString(1, authenticationToken);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new User(resultSet.getString("uuid"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("bio"), resultSet.getString("image"), resultSet.getInt("elo"), resultSet.getInt("wins"), resultSet.getInt("losses"), resultSet.getString("token"), resultSet.getInt("credit")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findOwnerOfCard(Card card){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_OWNER_OF_CARD)
        ) {
            preparedStatement.setString(1, card.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new User(resultSet.getString("uuid"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("bio"), resultSet.getString("image"), resultSet.getInt("elo"), resultSet.getInt("wins"), resultSet.getInt("losses"), resultSet.getString("token"), resultSet.getInt("credit")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public int readCurrentCredit(User user) {
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(READ_CURRENT_CREDIT)
        ) {
            preparedStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("credit");
            }
            return user.getCredit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUserByUuid(User user){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_BY_UUID)
        ) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getBio());
            preparedStatement.setString(4, user.getImage());
            preparedStatement.setInt(5, user.getElo());
            preparedStatement.setInt(6, user.getWins());
            preparedStatement.setInt(7, user.getLosses());
            preparedStatement.setString(8, user.getToken());
            preparedStatement.setInt(9, user.getCredit());
            preparedStatement.setString(10, user.getUuid());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDTO updateUserDataByUserName(String originalUsername, UserDTO userDTO){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERDATA_BY_USERNAME)
        ) {
            preparedStatement.setString(1, userDTO.getUsername());
            preparedStatement.setString(2, userDTO.getBio());
            preparedStatement.setString(3, userDTO.getImage());
            preparedStatement.setString(4, originalUsername);

            preparedStatement.execute();

            return userDTO;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new  RuntimeException(e);
        }
    }
}
