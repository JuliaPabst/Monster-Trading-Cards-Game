package jules.pabst.application.monsterTradingCards.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jules.pabst.application.monsterTradingCards.exception.InvalidCardType;
import jules.pabst.application.monsterTradingCards.exception.InvalidUserCredentials;

public class Card {
    private String id;
    private String ownerUuid;
    private String deckUserId;
    private CardType name;
    private float damage;



    public Card() {
        this.name = CardType.WaterSpell;
    }

    public Card(@JsonProperty("Id") String id,
                @JsonProperty("Name") String name,
                @JsonProperty("Damage") float damage,
                @JsonProperty("ownerUuid") String ownerUuid,
                @JsonProperty("DeckUserId") String deckUserId) {
        this.id = id;
        try {
            this.name = CardType.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new InvalidCardType("Invalid card type: " + name);
        }
        this.damage = damage;
        this.ownerUuid = ownerUuid;
        this.deckUserId = deckUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeckUserId() {
        return deckUserId;
    }

    public void setDeckUserId(String deckUserId) {
        this.deckUserId = deckUserId;
    }

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public String getName() {
        return name.getName();
    }

    public void setName(String name) {
        try {
            this.name = CardType.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new InvalidCardType("Invalid card type: " + name);
        }
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

}
