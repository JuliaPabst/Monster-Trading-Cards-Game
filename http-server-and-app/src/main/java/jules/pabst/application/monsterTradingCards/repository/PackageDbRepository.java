package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.data.ConnectionPool;
import jules.pabst.application.monsterTradingCards.entity.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PackageDbRepository implements PackageRepository {
    private final static String NEW_PACKAGE
            = "INSERT INTO packages VALUES (?)";
    private final ConnectionPool connectionPool;

    public PackageDbRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public String save(String packageId) {
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(NEW_PACKAGE)
        ) {
            preparedStatement.setString(1, packageId);
            preparedStatement.execute();
            return packageId;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
