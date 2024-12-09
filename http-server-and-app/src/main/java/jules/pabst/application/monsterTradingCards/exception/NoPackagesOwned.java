package jules.pabst.application.monsterTradingCards.exception;

public class NoPackagesOwned extends RuntimeException {
  public NoPackagesOwned(String message) {
    super(message);
  }
  public NoPackagesOwned(Throwable cause) {super(cause);}
}
