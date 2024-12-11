package jules.pabst.application.monsterTradingCards.exception;

public class UpdatingUserFailed extends RuntimeException {
    public UpdatingUserFailed(String message) {
        super(message);
    }
    public UpdatingUserFailed(Throwable cause) {super(cause);}
}
