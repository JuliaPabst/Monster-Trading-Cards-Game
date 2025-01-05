package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.data.ConnectionPool;
import jules.pabst.application.monsterTradingCards.entity.TradingDeal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TradeDbRepository implements TradeRepository {
    private final static String  NEW_DEAL = "INSERT INTO trades (id, card_to_trade, card_type, minimum_damage, status) VALUES (?, ?, ?, ?, ?)";
    private final static String FIND_ALL_DEALS = "SELECT * FROM trades";
    private final static String FIND_ALL_OPEN_DEALS = "SELECT * FROM trades WHERE status = 'open'";
    private final static String UPDATE_TRADE_DEAL_STATUS = "UPDATE trades SET status = ? WHERE id = ?";
    private final static String DELETE_DEAL = "DELETE FROM trades WHERE id = ?";

    private final ConnectionPool connectionPool;
    public TradeDbRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public TradingDeal save(TradingDeal tradingDeal){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(NEW_DEAL)
        ) {
            preparedStatement.setString(1, tradingDeal.getId());
            preparedStatement.setString(2, tradingDeal.getCardToTrade());
            preparedStatement.setString(3, tradingDeal.getType().getName());
            preparedStatement.setFloat(4, tradingDeal.getMinimumDamage());
            preparedStatement.setString(5, tradingDeal.getTradeStatus().getName());
            preparedStatement.execute();
            return tradingDeal;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TradingDeal> findAllTradingDeals(){
        List<TradingDeal> tradeDeals = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_DEALS);
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                TradingDeal newTradingDeal =  new TradingDeal(resultSet.getString("id"), resultSet.getString("card_to_trade"), resultSet.getString("card_type"), resultSet.getFloat("minimum_damage"), resultSet.getString("status"));
                tradeDeals.add(newTradingDeal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return tradeDeals;
    }

    @Override
    public List<TradingDeal> findAllOpenTradingDeals(){
        List<TradingDeal> openTradeDeals = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_OPEN_DEALS);
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                TradingDeal newTradingDeal =  new TradingDeal(resultSet.getString("id"), resultSet.getString("card_to_trade"), resultSet.getString("card_type"), resultSet.getFloat("minimum_damage"), resultSet.getString("status"));
                openTradeDeals.add(newTradingDeal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return openTradeDeals;
    }

    @Override
    public TradingDeal updateTradeDealStatus(TradingDeal tradingDeal){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TRADE_DEAL_STATUS);
        ) {
            preparedStatement.setString(1, tradingDeal.getTradeStatus().getName());
            preparedStatement.setString(2, tradingDeal.getId());
            preparedStatement.execute();

            return tradingDeal;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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
