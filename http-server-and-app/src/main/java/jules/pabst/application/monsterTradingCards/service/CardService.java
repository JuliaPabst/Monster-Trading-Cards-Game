package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.DTOs.UserCreationDTO;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.UserAlreadyExists;
import jules.pabst.application.monsterTradingCards.repository.CardMemoryRepository;
import jules.pabst.application.monsterTradingCards.repository.CardRepository;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
       this.cardRepository = cardRepository;
    }

    public Card createCard(Card card, String packageId) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }

        card.setId(UUID.randomUUID().toString());

        card = cardRepository.save(card, packageId);

        return createdCard;
    }

    public List<Card> getAll(){
        return cardRepository.findAll();
    }
}
