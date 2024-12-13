package jules.pabst.application.monsterTradingCards.controller;

import jules.pabst.application.monsterTradingCards.DTOs.StatsDTO;
import jules.pabst.application.monsterTradingCards.entity.ErrorResponse;
import jules.pabst.application.monsterTradingCards.exception.UserNotFound;
import jules.pabst.application.monsterTradingCards.service.StatsService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;

public class StatsController extends Controller {
    StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

 @Override
    public Response handle(Request request) {
        if(request.getMethod().equals(Method.GET)) {
            return getStats(request);
        }

        return null;
    }

    private Response getStats(Request request) {
        try {
            String authenticationToken = getAuthorizationToken(request);

            StatsDTO statsDTO = statsService.readStats(authenticationToken);

            return json(Status.OK, statsDTO);
        } catch(UserNotFound e ){
            return json(Status.NOT_FOUND, new ErrorResponse(e.getMessage()));
        }

    }

}
