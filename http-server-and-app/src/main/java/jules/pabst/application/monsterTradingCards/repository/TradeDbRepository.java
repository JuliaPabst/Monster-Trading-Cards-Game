package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.DTOs.TradeDTO;
import jules.pabst.application.monsterTradingCards.data.ConnectionPool;
import jules.pabst.application.monsterTradingCards.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TradeDbRepository implements TradeRepository {
    private final static String  NEW_DEAL = "INSERT INTO trades (id, card1_id, card2_id, card1_new_owner_id, card2_new_owner_id, type, minimum_damage) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final static String FIND_DEALS_BY_USER_UUID = "SELECT * FROM trades WHERE card1_new_owner_id = ? OR card2_new_owner_id = ?";
    private final static String DELETE_DEAL = "DELETE FROM trades WHERE id = ?";

    private final ConnectionPool connectionPool;
    public TradeDbRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public TradeDTO save(TradeDTO tradeDTO){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(NEW_DEAL)
        ) {
            preparedStatement.setString(1, tradeDTO.getTradeId());
            preparedStatement.setString(2, tradeDTO.getCard1Id());
            preparedStatement.setString(3, tradeDTO.getCard2Id());
            preparedStatement.setString(4, tradeDTO.getCard1NewOwnerId());
            preparedStatement.setString(5, tradeDTO.getCard2NewOwnerId());
            preparedStatement.setString(6, tradeDTO.getType());
            preparedStatement.setFloat(7, tradeDTO.getMinimumDamage());
            preparedStatement.execute();
            return tradeDTO;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TradeDTO> findAllTradesByUserUuid(User user){
        List<TradeDTO> tradeDTOs = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_DEALS_BY_USER_UUID);
        ) {
            preparedStatement.setString(1, user.getUuid());
            preparedStatement.setString(2, user.getUuid());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                TradeDTO newTradeDTO =  new TradeDTO(resultSet.getString("id"), resultSet.getString("card1_id"), resultSet.getString("card2_id"), resultSet.getString("card1_new_owner_id"), resultSet.getString("card2_new_owner_id"), resultSet.getString("type"), resultSet.getInt("minimum_damage"));
                tradeDTOs.add(newTradeDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return tradeDTOs;
    }

    @Override
    public String delete(String tradeId){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_DEAL)
        ) {
            preparedStatement.setString(1, tradeId);
            preparedStatement.execute();
            return tradeId;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
