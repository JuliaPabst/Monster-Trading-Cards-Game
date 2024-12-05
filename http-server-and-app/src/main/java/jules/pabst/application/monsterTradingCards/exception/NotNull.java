package jules.pabst.application.monsterTradingCards.exception;

public class NotNull extends RuntimeException {
    public NotNull(String message) {
        super(message);
    }
  public NotNull(Throwable cause) {
    super(cause);
  }
}
