package jules.pabst.application.monsterTradingCards.entity;

public enum TradeStatus {
    open("open"),
    completed("completed");

    private final String name;

    TradeStatus(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
