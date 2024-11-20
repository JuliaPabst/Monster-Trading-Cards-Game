package jules.pabst.application.monsterTradingCards.routing;

public class ControllerNotFound extends RuntimeException {
    public ControllerNotFound(String message) {
        super(message);
    }
}
