package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.DTOs.UserDTO;
import jules.pabst.application.monsterTradingCards.entity.User;

import java.sql.ResultSet;
import java.util.Optional;

import jules.pabst.application.monsterTradingCards.data.ConnectionPool;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.NotEnoughCredit;
import jules.pabst.application.monsterTradingCards.exception.UpdatingUserFailed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserDbRepository implements UserRepository {
    private final static String NEW_USER
            = "INSERT INTO users (uuid, username, password, credit) VALUES (?, ?, ?, ?)";
    private final static String FIND_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    private final static String FIND_USER_BY_TOKEN = "SELECT * FROM users WHERE token = ?";
    private final static String READ_CURRENT_CREDIT = "SELECT credit FROM users WHERE username = ?";
    private final static String UPDATE_TOKEN = "UPDATE users SET token = ? WHERE uuid = ?";
    private final static String UPDATE_CREDITS = "UPDATE users SET credit = ? WHERE username = ?";
    private final static String UPDATE_USERDATA = "UPDATE users SET username = ?, bio = ?, image= ? WHERE username = ?";
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
    public Optional<User> findUserByName(String name) {
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_USERNAME)
        ) {
            System.out.println("name in findUserByName:'%s'".formatted(name));
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Result set token finduserByName: " + resultSet.getString("token"));
                System.out.println("Result set username finduserByName: " + resultSet.getString("username"));
                return Optional.of(new User(resultSet.getString("uuid"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("bio"), resultSet.getString("image"), resultSet.getInt("elo"), resultSet.getInt("wins"), resultSet.getInt("losses"), resultSet.getString("token"), resultSet.getInt("credit")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    public Optional<User>findUserByAuthenticationToken(String authenticationToken){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_TOKEN)
        ) {
            System.out.println("authenticationtoken in findUserByAuthentication:'%s'".formatted(authenticationToken));
            preparedStatement.setString(1, authenticationToken);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Result set token findUserByAuthentication: " + resultSet.getString("token"));
                System.out.println("Result set username findUserByAuthentication: " + resultSet.getString("username"));
                return Optional.of(new User(resultSet.getString("uuid"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("bio"), resultSet.getString("image"), resultSet.getInt("elo"), resultSet.getInt("wins"), resultSet.getInt("losses"), resultSet.getString("token"), resultSet.getInt("credit")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    public void updateToken(User user, String token){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TOKEN)
        ) {
            int newCredit = user.getCredit() - 5;
            System.out.println("Uuid: %s".formatted(user.getUuid()));
            preparedStatement.setString(1, token);
            preparedStatement.setString(2, user.getUuid());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

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

    public void updateCredits(User user){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CREDITS)
        ) {
            int newCredit = user.getCredit() - 5;
            System.out.println("Username: %s".formatted(user.getUsername()));
            preparedStatement.setInt(1, newCredit);
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public UserDTO updateUserData(String originalUsername, UserDTO userDTO){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERDATA)
        ) {
            System.out.println("Username UserDTO: %s".formatted(userDTO.getUsername()));
            System.out.println("Username Original:" + originalUsername);
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
