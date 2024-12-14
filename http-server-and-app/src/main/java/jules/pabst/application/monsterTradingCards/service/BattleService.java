package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.CardType;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.repository.CardRepository;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;

import java.util.List;
import java.util.Random;

public class BattleService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    public BattleService(UserRepository userRepository, CardRepository cardRepository) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
    }

    public String battle(User player1, User player2) {
        List<Card> deck1 = cardRepository.findCardsByDeck(player1);
        List<Card> deck2 = cardRepository.findCardsByDeck(player2);

        StringBuilder battleLog = new StringBuilder();
        battleLog.append("Starting battle between ").append(player1.getUsername())
                .append(" and ").append(player2.getUsername()).append("\n");

        Random random = new Random();
        int round = 0;

        while (!deck1.isEmpty() && !deck2.isEmpty() && round < 100) {
            round++;
            battleLog.append("Round ").append(round).append("\n");

            // Choose random cards
            Card card1 = deck1.get(random.nextInt(deck1.size()));
            Card card2 = deck2.get(random.nextInt(deck2.size()));

            battleLog.append(player1.getUsername()).append(" plays ").append(card1.getName())
                    .append(" (Damage: ").append(card1.getDamage()).append(")\n");
            battleLog.append(player2.getUsername()).append(" plays ").append(card2.getName())
                    .append(" (Damage: ").append(card2.getDamage()).append(")\n");

            // Resolve special rules
            if (specialRules(card1, card2, battleLog)) {
                continue;
            }

            // Calculate damage
            double damage1 = calculateDamage(card1, card2);
            double damage2 = calculateDamage(card2, card1);

            if (damage1 > damage2) {
                battleLog.append(card1.getName()).append(" wins the round!\n");
                deck2.remove(card2);
                deck1.add(card2);
            } else if (damage2 > damage1) {
                battleLog.append(card2.getName()).append(" wins the round!\n");
                deck1.remove(card1);
                deck2.add(card1);
            } else {
                battleLog.append("It's a draw!\n");
            }
        }

        // Determine the winner
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

        battleLog.append(winner).append("\n");
        return battleLog.toString();
    }

    private boolean specialRules(Card card1, Card card2, StringBuilder log) {
        // Implementing some special rules
        if (card1.getName().contains("Goblin") && card2.getName().contains("Dragon")) {
            log.append("Goblin is too afraid to attack Dragon!\n");
            return true;
        }
        if (card1.getName().contains("Wizzard") && card2.getName().contains("Ork")) {
            log.append("Wizzard controls the Ork; Ork cannot attack!\n");
            return true;
        }
        if (card1.getName().contains("Knight") && card2.getName().contains("WaterSpell")) {
            log.append("Knight drowns instantly due to WaterSpell!\n");
            return true;
        }
        if (card1.getName().contains("Kraken") && card2.getName().contains("Spell")) {
            log.append("Kraken is immune to spells!\n");
            return true;
        }
        if (card1.getName().contains("FireElf") && card2.getName().contains("Dragon")) {
            log.append("FireElf evades Dragon's attack!\n");
            return true;
        }
        return false;
    }

    private double calculateDamage(Card attacker, Card defender) {
        // Check for elemental effectiveness
        double multiplier = 1.0;

        if (attacker.getName().equals(CardType.WaterSpell.name()) && defender.getName().equals(CardType.FireSpell.name())) {
            multiplier = 2.0; // Water -> Fire
        } else if (attacker.getName().equals(CardType.FireSpell.name()) && defender.getName().equals(CardType.RegularSpell.name())) {
            multiplier = 2.0; // Fire -> Normal
        } else if (attacker.getName().equals(CardType.RegularSpell.name()) && defender.getName().equals(CardType.WaterSpell.name())) {
            multiplier = 2.0; // Normal -> Water
        } else if (attacker.getName().equals(CardType.FireSpell.name()) && defender.getName().equals(CardType.WaterSpell.name())) {
            multiplier = 0.5; // Fire -> Water
        } else if (attacker.getName().equals(CardType.RegularSpell.name()) && defender.getName().equals(CardType.FireSpell.name())) {
            multiplier = 0.5; // Normal -> Fire
        } else if (attacker.getName().equals(CardType.WaterSpell.name()) && defender.getName().equals(CardType.RegularSpell.name())) {
            multiplier = 0.5; // Water -> Normal
        }

        return attacker.getDamage() * multiplier;
    }

    private void updateElo(User winner, User loser, StringBuilder log) {
        // Elo calculation logic
        int eloChange = 30;
        winner.setElo(winner.getElo() + eloChange);
        loser.setElo(loser.getElo() - eloChange);
        userRepository.updateCredits(winner); // Assuming ELO update is done via credits
        userRepository.updateCredits(loser);

        log.append(winner.getUsername()).append(" gains ").append(eloChange).append(" ELO points!\n");
        log.append(loser.getUsername()).append(" loses ").append(eloChange).append(" ELO points.\n");
    }
}
