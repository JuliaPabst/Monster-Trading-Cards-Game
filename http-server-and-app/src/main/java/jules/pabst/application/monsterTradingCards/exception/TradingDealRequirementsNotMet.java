package jules.pabst.application.monsterTradingCards.exception;

public class TradingDealRequirementsNotMet extends RuntimeException {
    public TradingDealRequirementsNotMet(String message) {
        super(message);
    }
    public TradingDealRequirementsNotMet(Throwable cause) {super(cause);}
}
