package jules.pabst.application.monsterTradingCards.controller;

import jules.pabst.application.monsterTradingCards.entity.ErrorResponse;
import jules.pabst.application.monsterTradingCards.entity.TradingDeal;
import jules.pabst.application.monsterTradingCards.exception.*;
import jules.pabst.application.monsterTradingCards.service.TradeService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;

import java.util.List;

public class TradeController extends Controller {
    TradeService tradeService;

    public TradeController(TradeService tradeService){
        this.tradeService = tradeService;
    }

    @Override
    public Response handle(Request request) {
        if(request.getMethod().equals(Method.GET)){
            return getTradeDeals(request);
        } else if(request.getMethod().equals(Method.POST)){
            return createTradeDeal(request);
        } else if (request.getMethod().equals(Method.DELETE)){
//            return deleteTradeDeal(request);
        }

        return json(Status.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    private Response getTradeDeals(Request request) {
        try {
            String auth = getAuthorizationToken(request);
            List<TradingDeal> tradeDeals = tradeService.readOpenTradeDeals(auth);
            return json(Status.OK, tradeDeals);
        } catch(UserNotFound e){
            return json(Status.NOT_FOUND, new ErrorResponse(e.getMessage()));
        } catch(MissingAuthorizationHeader e){
            return json(Status.BAD_REQUEST, new ErrorResponse(e.getMessage()));
        }
    }

    private Response createTradeDeal(Request request) {
        try {
            String auth = getAuthorizationToken(request);
            TradingDeal tradingDeal = fromBody(request.getBody(), TradingDeal.class);
            tradingDeal = tradeService.createTradeDeal(auth, tradingDeal);
            return json(Status.CREATED, tradingDeal);
        } catch(UserNotFound e){
            return json(Status.NOT_FOUND, new ErrorResponse(e.getMessage()));
        } catch(MissingAuthorizationHeader | CardIsPartOfDeck | CardsNotFound | CardNotOwned e){
            return json(Status.BAD_REQUEST, new ErrorResponse(e.getMessage()));
        } catch (NotAuthorized e){
            return json(Status.UNAUTHORIZED, new ErrorResponse(e.getMessage()));
        } catch(Exception e){
            return json(Status.INTERNAL_SERVER_ERROR, new ErrorResponse(e.getMessage()));
        }
    }

//    private Response deleteTradeDeal(Request request) {
//        try {
//            String auth = getAuthorizationToken(request);
//            String[] tradePath = request.getPath().split("/");
//            String tradeId = tradePath[tradePath.length - 1];
//            List<TradeDTO> tradeDTOs = tradeService.deleteTradeDeals(auth, tradeId);
//            return json(Status.NO_CONTENT, tradeDTOs);
//        } catch(UserNotFound e){
//            return json(Status.NOT_FOUND, new ErrorResponse(e.getMessage()));
//        } catch(MissingAuthorizationHeader | CardsNotFound | CardNotOwned e){
//            return json(Status.BAD_REQUEST, new ErrorResponse(e.getMessage()));
//        } catch(NotAuthorized e){
//            return json(Status.UNAUTHORIZED, new ErrorResponse(e.getMessage()));
//        } catch(Exception e){
//            return json(Status.INTERNAL_SERVER_ERROR, new ErrorResponse(e.getMessage()));
//        }
//    }
}
