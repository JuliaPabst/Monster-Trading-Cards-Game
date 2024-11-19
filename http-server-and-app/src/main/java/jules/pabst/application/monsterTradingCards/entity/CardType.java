package jules.pabst.application.monsterTradingCards.entity;

public enum CardType {
    WaterGoblin("WaterGoblin"),
    FireGoblin("FireGoblin"),
    RegularGoblin("RegularGoblin"),
    WaterTroll("WaterTroll"),
    FireTroll("FireTroll"),
    RegularTroll("RegularTroll"),
    WaterElf("WaterElf"),
    FireElf("FireElf"),
    RegularElf("RegularElf"),
    WaterSpell("WaterSpell"),
    FireSpell("FireSpell"),
    RegularSpell("RegularSpell"),
    Knight("Knight"),
    Dragon("Dragon"),
    Ork("Ork"),
    Kraken("Kraken"),
    Wizzard("Wizzard");

    private final String name;

    CardType(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
