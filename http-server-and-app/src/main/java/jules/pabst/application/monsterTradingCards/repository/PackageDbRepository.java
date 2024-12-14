package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.DTOs.AquirePackageDTO;
import jules.pabst.application.monsterTradingCards.data.ConnectionPool;
import jules.pabst.application.monsterTradingCards.entity.CardPackage;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.AllPackagesOwned;
import jules.pabst.application.monsterTradingCards.exception.NoPackagesOwned;

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

    private final static String FIND_PACKAGES_WITHOUT_OWNER = "SELECT * FROM packages WHERE owner_id IS NULL";
    private final static String FIND_PACKAGES_BY_OWNER = "SELECT * FROM packages WHERE owner_id = ?";
    private final static String UPDATE_PACKAGE
            = "UPDATE packages SET owner_id = ? WHERE id = ?";

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

    @Override
    public String findPackageWithoutOwner(){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_PACKAGES_WITHOUT_OWNER)
        ) {
            List<CardPackage> packages = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println("id: %s".formatted(resultSet.getString("id")));
                CardPackage newPackage = new CardPackage(resultSet.getString("id"), Optional.ofNullable(resultSet.getString("owner_id")));
                packages.add(newPackage);
            }

            if (!packages.isEmpty()) {
                return packages.get(0).getId();
            }

            throw(new AllPackagesOwned("All Packages are already owned by someone"));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CardPackage> findPackagesByOwner(User user){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_PACKAGES_BY_OWNER)
        ) {
            List<CardPackage> packages = new ArrayList<>();

            preparedStatement.setString(1, user.getUuid());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println("id: %s".formatted(resultSet.getString("id")));
                CardPackage newPackage = new CardPackage(resultSet.getString("id"), Optional.ofNullable(resultSet.getString("owner_id")));
                packages.add(newPackage);
            }

            if (!packages.isEmpty()) {
                System.out.println("-----------------");
                return packages;
            }

            throw(new NoPackagesOwned("This user does not own any packages"));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public AquirePackageDTO updatePackage(String packageId, User user){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PACKAGE)
        ) {
            System.out.println("Updating package with ID: " + packageId);
            System.out.println("Setting owner to: " + user.getUuid());
            preparedStatement.setString(1, user.getUuid());
            preparedStatement.setString(2, packageId);
            preparedStatement.execute();
            return new AquirePackageDTO(packageId, user.getUuid());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
