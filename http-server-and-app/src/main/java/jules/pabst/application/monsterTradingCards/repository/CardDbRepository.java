package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.data.ConnectionPool;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardDbRepository implements CardRepository {
    private final static String NEW_CARD
            = "INSERT INTO cards VALUES (?, ?, ?, ?)";
    private final static String ALL_CARDS
            = "SELECT * FROM cards";
    private final ConnectionPool connectionPool;

    public CardDbRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Card save(Card card) {
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(NEW_CARD)
        ) {
            System.out.println("I am creating a card!");
            preparedStatement.setString(1, card.getId());
            preparedStatement.setString(2, card.getName());
            preparedStatement.setFloat(3, card.getDamage());
            preparedStatement.setString(4, card.getPackageId());
            preparedStatement.execute();
            return card;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Optional<Card>> findAll(){
        List<Optional<Card>> cards = new ArrayList<>();
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(ALL_CARDS);
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Card card = new Card(resultSet.getString("id"), resultSet.getString("name"), resultSet.getFloat("damage"));
                cards.add(Optional.of(card));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return cards;
    }
}
