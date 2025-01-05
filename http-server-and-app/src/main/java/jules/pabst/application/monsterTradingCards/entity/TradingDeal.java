package jules.pabst.application.monsterTradingCards.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jules.pabst.application.monsterTradingCards.exception.InvalidCardType;

public class TradingDeal {
    private String id;
    private String cardToTrade;
    private TradeType type;
    private float minimumDamage;
    private TradeStatus tradeStatus;

    public TradingDeal(@JsonProperty("Id") String id,
                @JsonProperty("CardToTrade") String cardToTrade,
                @JsonProperty("Type") String type,
                @JsonProperty("MinimumDamage") float minimumDamage) {
        this.id = id;
        try {
            this.type = TradeType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new InvalidCardType("Invalid trade type: " + type);
        }
        this.cardToTrade = cardToTrade;
        this.minimumDamage = minimumDamage;
        this.tradeStatus = TradeStatus.valueOf("open");
    }

    public TradingDeal(String id, String cardToTrade, String type, float minimumDamage, String tradeStatus) {
        this.id = id;
        this.cardToTrade = cardToTrade;
        this.type = TradeType.valueOf(type);
        this.minimumDamage = minimumDamage;
        this.tradeStatus = TradeStatus.valueOf(tradeStatus);
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

    public TradeStatus getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = TradeStatus.valueOf(tradeStatus);
    }
}
