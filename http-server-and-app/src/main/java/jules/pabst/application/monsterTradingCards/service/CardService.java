package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.DTOs.UserCreationDTO;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.NotNull;
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

    public Card create(Card card) {
        if (card == null) {
            throw new NotNull("Card cannot be null");
        }

        if (card.getName() == null) {
            throw new NotNull("Card name cannot be null");
        }

        System.out.println("This is one card being created" + card.getName());
        card.setId(UUID.randomUUID().toString());

        card = cardRepository.save(card);

        return card;
    }

    public List<Optional<Card>> getAll(){
        return cardRepository.findAll();
    }
}
