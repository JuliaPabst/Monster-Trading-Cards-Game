package jules.pabst.application.monsterTradingCards.exception;

public class NotAuthorized extends RuntimeException {
    public NotAuthorized(String message) {
        super(message);
    }

    public NotAuthorized(Throwable cause) {
      super(cause);
    }
}
