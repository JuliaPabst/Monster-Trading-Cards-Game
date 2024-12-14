package jules.pabst.application.monsterTradingCards.controller;

import jules.pabst.application.monsterTradingCards.DTOs.BattleDTO;
import jules.pabst.application.monsterTradingCards.service.BattleService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;

public class BattlesController extends Controller {
    BattleService battleService;
    
    public BattlesController(BattleService battleService) {
        this.battleService = battleService;
    }
    
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.POST)) {
            return getBattleLog(request);
        }

        return json(Status.NOT_FOUND, "Endpoint not found");
    }

    private Response getBattleLog(Request request) {
        return new Response();
    }
}
