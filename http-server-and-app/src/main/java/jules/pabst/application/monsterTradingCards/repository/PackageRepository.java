package jules.pabst.application.monsterTradingCards.repository;

import jules.pabst.application.monsterTradingCards.DTOs.AquirePackageDTO;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.CardPackage;
import jules.pabst.application.monsterTradingCards.entity.User;
import java.util.List;
import java.util.Optional;

public interface PackageRepository {
    public String save(String packageId);

    public AquirePackageDTO updatePackage(String packageId, User user);

    public String findPackageWithoutOwner();

    public List<CardPackage> findPackagesByOwner(User user);
}
