package jules.pabst.application.monsterTradingCards.exception;

public class TradingDealNotFound extends RuntimeException {
    public TradingDealNotFound(String message) {
        super(message);
    }
    public TradingDealNotFound(Throwable cause){super(cause);}
}
