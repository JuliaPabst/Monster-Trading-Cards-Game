package jules.pabst.application.monsterTradingCards.entity;

public class TradingDeal {
    private String id;
    private String cardToTrade;
    private TradeType type;
    private float minimumDamage;

    TradingDeal(String id) {
        this.id = id;
        this.minimumDamage = 0;
    }

    TradingDeal(String id, String cardToTrade, TradeType type, float minimumDamage) {
        this.id = id;
        this.cardToTrade = cardToTrade;
        this.type = type;
        this.minimumDamage = minimumDamage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardToTrade() {
        return cardToTrade;
    }

    public void setCardToTrade(String cardToTrade) {
        this.cardToTrade = cardToTrade;
    }

    public TradeType getType() {
        return type;
    }

    public void setType(TradeType type) {
        this.type = type;
    }

    public float getMinimumDamage() {
        return minimumDamage;
    }

    public void setMinimumDamage(float minimumDamage) {
        this.minimumDamage = minimumDamage;
    }
}
