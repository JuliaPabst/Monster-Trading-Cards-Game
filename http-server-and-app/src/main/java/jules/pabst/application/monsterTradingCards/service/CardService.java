package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.CardPackage;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.CardsNotFound;
import jules.pabst.application.monsterTradingCards.exception.NoPackagesOwned;
import jules.pabst.application.monsterTradingCards.exception.NotNull;
import jules.pabst.application.monsterTradingCards.repository.CardRepository;
import jules.pabst.application.monsterTradingCards.repository.PackageRepository;

import java.util.List;
import java.util.UUID;

public class CardService {

    private final CardRepository cardRepository;
    private final PackageRepository packageRepository;

    public CardService(CardRepository cardRepository, PackageRepository packageRepository) {
       this.cardRepository = cardRepository;
       this.packageRepository = packageRepository;
    }

    public Card create(Card card) {
        if (card == null) {
            throw new NotNull("Card cannot be null");
        }

        if (card.getName() == null) {
            throw new NotNull("Card name cannot be null");
        }

        System.out.println("This is one card being created" + card.getName());
        System.out.println("This is the id: " + card.getId());

        card = cardRepository.save(card);

        return card;
    }

    public List<Card> readByUserToken(User user){
        try{
            List<CardPackage> packagesOwnedByUser = packageRepository.findPackagesByOwner(user);
            if(!packagesOwnedByUser.isEmpty()){
                return cardRepository.findCardsByPackage(packagesOwnedByUser);
            }
            throw new CardsNotFound("This user does not own any cards");
        } catch(NoPackagesOwned e){
            throw new NoPackagesOwned("This user does not own any cards");
        }

    }
}
