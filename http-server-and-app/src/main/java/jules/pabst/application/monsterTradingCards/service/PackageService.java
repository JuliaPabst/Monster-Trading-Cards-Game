package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.repository.CardRepository;
import jules.pabst.application.monsterTradingCards.repository.PackageRepository;

import java.util.List;
import java.util.UUID;

public class PackageService {

    private final PackageRepository packageRepository;

    public PackageService(PackageRepository packageRepository) {
       this.packageRepository = packageRepository;
    }

    public String create(String packageId) {
        if (packageId == null) {
            throw new IllegalArgumentException("Package Id cannot be null");
        }

        return packageRepository.save(packageId);
    }
}
