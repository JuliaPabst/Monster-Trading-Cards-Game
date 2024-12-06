package jules.pabst.application.monsterTradingCards.entity;

import java.util.Optional;

public class CardPackage {
    private String id;
    private Optional<String> owner;

    public CardPackage(String id, Optional<String> owner) {
        this.id = id;
        this.owner = owner;
    }

    public Optional<String> getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = Optional.of(owner);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
