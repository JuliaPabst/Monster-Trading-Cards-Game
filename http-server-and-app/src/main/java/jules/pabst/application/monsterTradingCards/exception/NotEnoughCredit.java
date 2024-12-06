package jules.pabst.application.monsterTradingCards.exception;

public class NotEnoughCredit extends RuntimeException {
    public NotEnoughCredit(String message) {
        super(message);
    }
    public NotEnoughCredit(Throwable cause) {
        super(cause);
    }
}
