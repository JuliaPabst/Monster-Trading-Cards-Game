package jules.pabst.application.monsterTradingCards.DTOs;

public class TradeDTO {
    private String tradeId;
    private String card1Id;
    private String card2Id;
    private String card1NewOwnerId;
    private String card2NewOwnerId;
    private String type;
    private int minimumDamage;

    public TradeDTO(String tradeId, String card1Id, String card2Id, String card1NewOwnerId, String card2NewOwnerId, String type, int minimumDamage) {
        this.tradeId = tradeId;
        this.card1Id = card1Id;
        this.card2Id = card2Id;
        this.card1NewOwnerId = card1NewOwnerId;
        this.card2NewOwnerId = card2NewOwnerId;
        this.type = type;
        this.minimumDamage = minimumDamage;
    }
    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getCard1Id() {
        return card1Id;
    }

    public void setCard1Id(String card1Id) {
        this.card1Id = card1Id;
    }

    public String getCard2Id() {
        return card2Id;
    }

    public void setCard2Id(String card2Id) {
        this.card2Id = card2Id;
    }

    public String getCard1NewOwnerId() {
        return card1NewOwnerId;
    }

    public void setCard1NewOwnerId(String card1NewOwnerId) {
        this.card1NewOwnerId = card1NewOwnerId;
    }

    public String getCard2NewOwnerId() {
        return card2NewOwnerId;
    }

    public void setCard2NewOwnerId(String card2NewOwnerId) {
        this.card2NewOwnerId = card2NewOwnerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMinimumDamage() {
        return minimumDamage;
    }

    public void setMinimumDamage(int minimumDamage) {
        this.minimumDamage = minimumDamage;
    }
}
