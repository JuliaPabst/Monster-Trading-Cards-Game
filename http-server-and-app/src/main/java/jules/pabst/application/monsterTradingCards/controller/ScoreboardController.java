package jules.pabst.application.monsterTradingCards.controller;

import jules.pabst.application.monsterTradingCards.DTOs.ScoreboardDTO;
import jules.pabst.application.monsterTradingCards.DTOs.StatsDTO;
import jules.pabst.application.monsterTradingCards.service.ScoreboardService;
import jules.pabst.application.monsterTradingCards.service.UserService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;

import java.util.List;

public class ScoreboardController extends Controller {
    private ScoreboardService scoreboardService;
    public ScoreboardController(ScoreboardService scoreBoardService) {
        this.scoreboardService = scoreBoardService;
    }



    public Response handle(Request request) {
        if(request.getMethod().equals(Method.GET)) {
            return getUserData(request);
        }

        return json(Status.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    public Response getUserData(Request request) {
        try{
            List<ScoreboardDTO> scoreboardDTO = scoreboardService.readSortedScoreboard();
            return json(Status.OK, scoreboardDTO);
        } catch (Exception e) {
            return json(Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
