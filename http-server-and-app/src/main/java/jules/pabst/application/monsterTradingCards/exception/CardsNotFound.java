package jules.pabst.application.monsterTradingCards.exception;

public class CardsNotFound extends RuntimeException {
    public CardsNotFound(String message) {
        super(message);
    }
    public CardsNotFound(Throwable cause) {
        super(cause);
    }
}
