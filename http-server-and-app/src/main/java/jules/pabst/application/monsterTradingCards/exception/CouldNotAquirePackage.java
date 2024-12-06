package jules.pabst.application.monsterTradingCards.exception;

public class CouldNotAquirePackage extends RuntimeException {
    public CouldNotAquirePackage(String message) {
        super(message);
    }

    public CouldNotAquirePackage(Throwable cause) {
        super(cause);
    }
}
