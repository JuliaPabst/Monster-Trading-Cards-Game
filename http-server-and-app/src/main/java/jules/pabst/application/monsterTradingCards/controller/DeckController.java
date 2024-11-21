package jules.pabst.application.monsterTradingCards.controller;

import jules.pabst.application.monsterTradingCards.service.CardService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeckController extends Controller {
    private final CardService cardsService;


    public DeckController() {
        this.cardsService = new CardService();
    }

    @Override
    public Response handle(Request request) {

        if(request.getMethod().equals(Method.GET)){
            return getDeck();
        } else if(request.getMethod().equals(Method.PUT)){
            return changeDeck(request);
        }
        return null;
    }

    public Response getDeck() {
        Response response = new Response();
        response.setStatus(Status.OK);
        response.setHeader("Content-Type", "application/json");
        response.setBody(
                "{ \"deck\": \"{placeholder}\" }"
        );
        return response;
    }

    public Response changeDeck(Request request) {
        Response response = new Response();
        response.setStatus(Status.OK);
        response.setHeader("Content-Type", "application/json");
        response.setBody(
                "{ \"deck\": \"{placeholder}\" }"
        );
        return response;
    }
}
