package jules.pabst.application.monsterTradingCards.exception;

public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists(String message) {
        super(message);
    }
    public UserAlreadyExists(Throwable cause) {
        super(cause);
    }
}
