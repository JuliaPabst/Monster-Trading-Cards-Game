package jules.pabst.application.monsterTradingCards.exception;

public class InvalidCardType extends RuntimeException {
    public InvalidCardType(String message) {
        super(message);
    }
    public InvalidCardType(Throwable cause) {
        super(cause);
    }
}
