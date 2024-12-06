package jules.pabst.application.monsterTradingCards.exception;

public class AllPackagesOwned extends RuntimeException {
    public AllPackagesOwned(String message) {
        super(message);
    }
    public AllPackagesOwned(Throwable cause) {
        super(cause);
    }
}
