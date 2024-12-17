package jules.pabst.application.monsterTradingCards.controller;

import jules.pabst.application.monsterTradingCards.DTOs.TradeDTO;
import jules.pabst.application.monsterTradingCards.entity.ErrorResponse;
import jules.pabst.application.monsterTradingCards.entity.TradingDeal;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.MissingAuthorizationHeader;
import jules.pabst.application.monsterTradingCards.exception.UserNotFound;
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
        }

        return json(Status.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    private Response getTradeDeals(Request request) {
        try {
            String auth = getAuthorizationToken(request);
            List<TradeDTO> tradeDTOs = tradeService.readTradeDeals(auth);
            return json(Status.OK, tradeDTOs);
        } catch(UserNotFound e){
            return json(Status.NOT_FOUND, new ErrorResponse(e.getMessage()));
        } catch(MissingAuthorizationHeader e){
            return json(Status.BAD_REQUEST, new ErrorResponse(e.getMessage()));
        }
    }

    private Response createTradeDeal(Request request) {
        return new Response();
    }
}
