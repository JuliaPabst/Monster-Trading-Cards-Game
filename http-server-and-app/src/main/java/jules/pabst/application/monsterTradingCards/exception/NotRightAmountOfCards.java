package jules.pabst.application.monsterTradingCards.exception;

public class NotRightAmountOfCards extends RuntimeException {
    public NotRightAmountOfCards(String message) {
        super(message);
    }
    public NotRightAmountOfCards(Throwable cause) {super(cause);}
}
