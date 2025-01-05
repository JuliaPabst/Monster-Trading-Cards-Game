package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.CardType;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.repository.CardRepository;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class BattleService {
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final UserService userService;
    private final CardService cardService;

    public BattleService(UserRepository userRepository, CardRepository cardRepository, UserService userService, CardService cardService) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.userService = userService;
        this.cardService = cardService;
    }

    public String battle(String player1AuthenticationToken, String player2AuthenticationToken) {
        User player1 = userService.getUserByAuthenticationToken(player1AuthenticationToken);
        User player2 = userService.getUserByAuthenticationToken(player2AuthenticationToken);

        List<Card> deck1 = new CopyOnWriteArrayList<>(cardRepository.findCardsByDeck(player1));
        List<Card> deck2 = new CopyOnWriteArrayList<>(cardRepository.findCardsByDeck(player2));

        StringBuilder battleLog = new StringBuilder();
        battleLog.append("Starting battle between ").append(player1.getUsername())
                .append(" and ").append(player2.getUsername()).append("\r\n");

        Random random = new Random();
        int round = 0;

        while (!deck1.isEmpty() && !deck2.isEmpty() && round < 100) {
            round++;
            battleLog.append("Round ").append(round).append("\r\n");

            Card card1 = deck1.get(random.nextInt(deck1.size()));
            Card card2 = deck2.get(random.nextInt(deck2.size()));

            battleLog.append(player1.getUsername()).append(" plays ").append(card1.getName())
                    .append(" (Damage: ").append(card1.getDamage()).append(")\r\n");
            battleLog.append(player2.getUsername()).append(" plays ").append(card2.getName())
                    .append(" (Damage: ").append(card2.getDamage()).append(")\r\n");

            if (specialRules(card1, card2, battleLog) || specialRules(card2, card1, battleLog)) {
                continue;
            }

            double damage1 = calculateDamage(card1, card2);
            double damage2 = calculateDamage(card2, card1);

            if (damage1 > damage2) {
                battleLog.append(card1.getName()).append(" wins the round!\r\n");
                deck2.remove(card2);
                deck1.add(card2);
                card2.setDeckUserId(deck1.getFirst().getDeckUserId());
                card2.setOwnerUuid(deck1.getFirst().getOwnerUuid());
                cardService.updateDeckuserId(card2);
            } else if (damage2 > damage1) {
                battleLog.append(card2.getName()).append(" wins the round!\r\n");
                deck1.remove(card1);
                deck2.add(card1);
                card1.setDeckUserId(deck2.getFirst().getDeckUserId());
                card1.setOwnerUuid(deck2.getFirst().getOwnerUuid());
                cardService.updateDeckuserId(card1);
            } else {
                battleLog.append("It's a draw!\r\n");
            }
        }

        String winner;
        if (deck1.isEmpty() && deck2.isEmpty()) {
            winner = "It's a draw!";
        } else if (deck1.isEmpty()) {
            winner = player2.getUsername() + " wins the battle!";
            updateElo(player2, player1, battleLog);
        } else {
            winner = player1.getUsername() + " wins the battle!";
            updateElo(player1, player2, battleLog);
        }

        battleLog.append(winner).append("\r\n");
        return battleLog.toString();
    }

    private boolean specialRules(Card card1, Card card2, StringBuilder log) {
        if (card1.getName().contains("Goblin") && card2.getName().contains("Dragon")) {
            log.append("Goblin is too afraid to attack Dragon!\r\n");
            return true;
        }
        if (card1.getName().contains("Wizzard") && card2.getName().contains("Ork")) {
            log.append("Wizzard controls the Ork; Ork cannot attack!\r\n");
            return true;
        }
        if (card1.getName().contains("Knight") && card2.getName().contains("WaterSpell")) {
            log.append("Knight drowns instantly due to WaterSpell!\r\n");
            return true;
        }
        if (card1.getName().contains("Kraken") && card2.getName().contains("Spell")) {
            log.append("Kraken is immune to spells!\r\n");
            return true;
        }
        if (card1.getName().contains("FireElf") && card2.getName().contains("Dragon")) {
            log.append("FireElf evades Dragon's attack!\r\n");
            return true;
        }
        return false;
    }

    private double calculateDamage(Card attacker, Card defender) {
        double multiplier = 1.0;

        if (attacker.getName().equals(CardType.WaterSpell.name()) && defender.getName().equals(CardType.FireSpell.name())) {
            multiplier = 2.0;
        } else if (attacker.getName().equals(CardType.FireSpell.name()) && defender.getName().equals(CardType.RegularSpell.name())) {
            multiplier = 2.0;
        } else if (attacker.getName().equals(CardType.RegularSpell.name()) && defender.getName().equals(CardType.WaterSpell.name())) {
            multiplier = 2.0;
        } else if (attacker.getName().equals(CardType.FireSpell.name()) && defender.getName().equals(CardType.WaterSpell.name())) {
            multiplier = 0.5;
        }

        return attacker.getDamage() * multiplier;
    }

    private void updateElo(User winner, User loser, StringBuilder log) {
        int eloChange = 30;
        winner.setElo(winner.getElo() + eloChange);
        loser.setElo(loser.getElo() - eloChange);

        winner.setWins(winner.getWins() + 1);
        loser.setLosses(loser.getLosses() + 1);

        userRepository.updateUserByUuid(winner);
        userRepository.updateUserByUuid(loser);

        log.append(winner.getUsername()).append(" gains ").append(eloChange).append(" ELO points!\r\n");
        log.append(loser.getUsername()).append(" loses ").append(eloChange).append(" ELO points.\r\n");
    }
}
