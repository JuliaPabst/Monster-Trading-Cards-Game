package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.DTOs.AquirePackageDTO;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.AllPackagesOwned;
import jules.pabst.application.monsterTradingCards.exception.NotAuthorized;
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
    private final CardService cardService;

    public PackageService(PackageRepository packageRepository, UserRepository userRepository, CardService cardService) {
       this.packageRepository = packageRepository;
       this.userRepository = userRepository;
       this.cardService = cardService;
    }

    public List<Card> createPackage(String authToken, List<Card> cards) {
        if(authToken.equals("admin-mtcgToken")){
            String packageId = UUID.randomUUID().toString();

            packageRepository.save(packageId);

            cards.forEach(card -> {
                card.setPackageId(packageId);
                cardService.create(card);
            });

            return cards;
        }

        throw new NotAuthorized("Only admins can create packages");
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

            userRepository.updateUserByUuid(user);

            return packageRepository.updatePackage(packageIdWithoutOwner, user);
        }

        throw(new NotEnoughCredit("Not enough credit"));
    }
}
