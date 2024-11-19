package jules.pabst.application.monsterTradingCards.controller;

import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;

public class GameController {

    public Response getStats() {
        Response response = new Response();
        response.setStatus(Status.OK);
        response.setHeader("Content-Type", "application/json");
        response.setBody(
                "{ \"stats\": \"{placeholder}\" }"
        );
        return response;
    }

    public Response getScoreboard() {
        Response response = new Response();
        response.setStatus(Status.OK);
        response.setHeader("Content-Type", "application/json");
        response.setBody(
                "{ \"scoreboard\": \"{placeholder}\" }"
        );
        return response;
    }

    public Response startBattle() {
        Response response = new Response();
        response.setStatus(Status.OK);
        response.setHeader("Content-Type", "application/json");
        response.setBody(
                "{ \"scoreboard\": \"{placeholder}\" }"
        );
        return response;
    }
}
