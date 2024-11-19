package jules.pabst.application.monsterTradingCards.entity;

public enum TradeType {
    monster("monster"),
    spell("spell");

    private final String name;

    TradeType(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
