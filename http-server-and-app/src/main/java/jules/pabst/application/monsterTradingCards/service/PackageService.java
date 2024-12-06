package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.DTOs.AquirePackageDTO;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.AllPackagesOwned;
import jules.pabst.application.monsterTradingCards.exception.NotEnoughCredit;
import jules.pabst.application.monsterTradingCards.repository.CardRepository;
import jules.pabst.application.monsterTradingCards.repository.PackageRepository;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PackageService {

    private final PackageRepository packageRepository;
    private final UserRepository userRepository;

    public PackageService(PackageRepository packageRepository, UserRepository userRepository) {
       this.packageRepository = packageRepository;
       this.userRepository = userRepository;
    }

    public String create(String packageId) {
        if (packageId == null) {
            throw new IllegalArgumentException("Package Id cannot be null");
        }

        return packageRepository.save(packageId);
    }

    public AquirePackageDTO checkCreditAndAquire(User user){
        user.setCredit(userRepository.readCurrentCredit(user));
        System.out.println("User credit from db: " + userRepository.readCurrentCredit(user));
        System.out.println("User credit: %d".formatted(user.getCredit()));
        if(user.getCredit()>=5){
            String packageIdWithoutOwner = packageRepository.findPackageWithoutOwner();

            if(packageIdWithoutOwner.isEmpty()){
                throw(new AllPackagesOwned("All packages already owned"));
            }

            userRepository.updateCredits(user);

            return packageRepository.updatePackage(packageIdWithoutOwner, user);
        }

        throw(new NotEnoughCredit("Not enough credit"));
    }
}
