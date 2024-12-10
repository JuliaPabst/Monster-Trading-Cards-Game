package jules.pabst.application.monsterTradingCards.exception;

public class CardNotOwned extends RuntimeException {
    public CardNotOwned(String message) {
        super(message);
    }
    public CardNotOwned(Throwable cause) {super(cause);}
}
