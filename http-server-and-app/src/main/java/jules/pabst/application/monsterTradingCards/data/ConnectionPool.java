package jules.pabst.application.monsterTradingCards.data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {
    private final static String URL = "jdbc:postgresql://localhost:5432/monster-trading-db";
    private final static String USERNAME = "monster1";
    private final static String PASSWORD = "monster1";
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
