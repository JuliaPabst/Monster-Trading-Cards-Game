package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.data.ConnectionPool;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.TradingDeal;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.CardsNotFound;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardDbRepository implements CardRepository {
    private final static String NEW_CARD
            = "INSERT INTO cards VALUES (?, ?, ?)";
    private final static String ALL_CARDS
            = "SELECT * FROM cards";
    private final static String CARDS_BELONGING_TO_DECK = "SELECT * from cards where deck_owner_uuid = ?";
    private final static String CARDS_BELONGING_TO_USER = "SELECT * FROM cards WHERE owner_uuid = ?";
    private final static String CARDS_NOT_BELONGING_TO_ANY_USER = "SELECT * FROM cards WHERE owner_uuid is NULL";
    private final static String CARDS_BELONGING_TO_CARD_ID = "SELECT * from cards where id = ?";
    private final static String UPDATED_CARD = "UPDATE cards SET owner_uuid = ?, deck_owner_uuid = ? where id = ?";
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
            preparedStatement.setString(1, card.getId());
            preparedStatement.setString(2, card.getName());
            preparedStatement.setFloat(3, card.getDamage());
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
                PreparedStatement preparedStatement = connection.prepareStatement(ALL_CARDS)
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Card card = new Card(resultSet.getString("id"), resultSet.getString("name"), resultSet.getFloat("damage"), resultSet.getString("owner_uuid"), resultSet.getString("deck_owner_uuid"));
                cards.add(Optional.of(card));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return cards;
    }

    public List<Card> findCardsByDeck(User user){
        List<Card> cards = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(CARDS_BELONGING_TO_DECK)
        ) {
            preparedStatement.setString(1, user.getUuid());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Card card = new Card(resultSet.getString("id"), resultSet.getString("name"), resultSet.getFloat("damage"), resultSet.getString("owner_uuid"), resultSet.getString("deck_owner_uuid"));
                cards.add(card);
            }
            return cards;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Card> findCardsByUserUuid(User user){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(CARDS_BELONGING_TO_USER)
        ) {
            List<Card> cards = new ArrayList<>();
            preparedStatement.setString(1, user.getUuid());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Card card = new Card(resultSet.getString("id"), resultSet.getString("name"), resultSet.getFloat("damage"), resultSet.getString("owner_uuid"), resultSet.getString("deck_owner_uuid"));
                cards.add(card);
            }

            return cards;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Card> findCardsNotBelongingToAnyUser(User user){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(CARDS_NOT_BELONGING_TO_ANY_USER)
        ) {
            List<Card> cards = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Card card = new Card(resultSet.getString("id"), resultSet.getString("name"), resultSet.getFloat("damage"), resultSet.getString("owner_uuid"), resultSet.getString("deck_owner_uuid"));
                cards.add(card);
            }

            return cards;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Card> findCardsById(List<String> ids){
        List<Card> cards = new ArrayList<>();

        for(String id : ids){
            try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(CARDS_BELONGING_TO_CARD_ID)
            ) {
                preparedStatement.setString(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    Card card = new Card(resultSet.getString("id"), resultSet.getString("name"), resultSet.getFloat("damage"), resultSet.getString("owner_uuid"), resultSet.getString("deck_owner_uuid"));
                    cards.add(card);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

         if (cards.size() == ids.size()) {
            return cards;
        }

        throw new CardsNotFound("No cards belonging to the provided ids repo");
    }

    public Card updateCard(Card card){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATED_CARD)
        ) {
            preparedStatement.setString(1, card.getOwnerUuid());
            preparedStatement.setString(2, card.getDeckUserId());
            preparedStatement.setString(3, card.getId());
            preparedStatement.execute();
            return card;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Card> updateDeckUserId(List<Card> cards){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATED_CARD)
        ) {
            for(Card card : cards){
                preparedStatement.setString(1, card.getOwnerUuid());
                preparedStatement.setString(2, card.getDeckUserId());
                preparedStatement.setString(3, card.getId());
                preparedStatement.execute();
            }

            return cards;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
