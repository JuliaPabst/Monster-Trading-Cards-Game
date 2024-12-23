package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.DTOs.AquirePackageDTO;
import jules.pabst.application.monsterTradingCards.entity.CardPackage;
import jules.pabst.application.monsterTradingCards.entity.User;
import java.util.List;

public interface PackageRepository {

    String findPackageWithoutOwner();

    AquirePackageDTO updatePackage(String packageId, User user);
}
