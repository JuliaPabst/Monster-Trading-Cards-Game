package jules.pabst.application.monsterTradingCards.exception;

public class MissingAuthorizationHeaderException extends RuntimeException {
  public MissingAuthorizationHeaderException(String message) {
        super(message);
    }
  public MissingAuthorizationHeaderException(Throwable cause) {
    super(cause);
  }
}
