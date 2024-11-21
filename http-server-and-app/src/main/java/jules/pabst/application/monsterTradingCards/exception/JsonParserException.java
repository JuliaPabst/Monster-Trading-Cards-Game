package jules.pabst.application.monsterTradingCards.exception;

public class JsonParserException extends RuntimeException {
  public JsonParserException(Throwable cause) {
    super(cause);
  }

  public JsonParserException(String message) {
        super(message);
    }
}
