package jules.pabst.application.monsterTradingCards.exception;

public class UserNotFound extends RuntimeException {
    public UserNotFound(String message) {
        super(message);
    }

    public UserNotFound(Throwable cause) {
        super(cause);
    }
}
