package jules.pabst.application.monsterTradingCards.exception;

public class MissingAuthorizationHeader extends RuntimeException {
    public MissingAuthorizationHeader(String message) {
        super(message);
    }
    public MissingAuthorizationHeader(Throwable cause) {
      super(cause);
    }
}
