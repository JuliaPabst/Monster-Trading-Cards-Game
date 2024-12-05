package jules.pabst.application.monsterTradingCards.entity;

public class Card {
    private String id;
    private CardType name;
    private float damage;

    public Card(String id, CardType name, float damage) {
        this.id = id;
        this.name = name;
        this.damage = damage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CardType getName() {
        return name;
    }

    public float getDamage() {
        return damage;
    }

}
