package jules.pabst.application.monsterTradingCards.exception;

public class InvalidUserCredentials extends RuntimeException {
    public InvalidUserCredentials(String message) {
        super(message);
    }

    public InvalidUserCredentials(Throwable cause) {
        super(cause);
    }
}
