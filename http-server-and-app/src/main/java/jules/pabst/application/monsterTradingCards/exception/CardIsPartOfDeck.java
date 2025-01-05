package jules.pabst.application.monsterTradingCards.exception;

public class CardIsPartOfDeck extends RuntimeException {
    public CardIsPartOfDeck(String message) {
        super(message);
    }
    public CardIsPartOfDeck(Throwable cause) {super(cause);}
}
