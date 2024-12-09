package jules.pabst.application.monsterTradingCards.controller;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.service.CardService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class DeckController extends Controller {
    private final CardService cardsService;


    public DeckController(CardService cardsService) {
        this.cardsService = cardsService;
    }

    @Override
    public Response handle(Request request) {

        if(request.getMethod().equals(Method.GET)){
            return getDeck(request);
        } else if(request.getMethod().equals(Method.PUT)){
            return changeDeck(request);
        }
        return null;
    }

    public Response getDeck(Request request) {
        String authentificationToken = getAuthorizationHeader(request);
        Response response = new Response();


        return response;
    }

    public Response changeDeck(Request request) {
        Response response = new Response();

        return response;
    }
}
