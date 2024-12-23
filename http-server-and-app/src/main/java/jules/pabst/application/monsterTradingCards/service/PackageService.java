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
    private final UserService userService;

    public PackageService(PackageRepository packageRepository, UserRepository userRepository, CardService cardService, UserService userService) {
       this.packageRepository = packageRepository;
       this.userRepository = userRepository;
       this.cardService = cardService;
        this.userService = userService;
    }

}
