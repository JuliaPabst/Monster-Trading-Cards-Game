package jules.pabst.application.monsterTradingCards.controller;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.UserNotFound;
import jules.pabst.application.monsterTradingCards.service.CardService;
import jules.pabst.application.monsterTradingCards.service.DeckService;
import jules.pabst.application.monsterTradingCards.service.UserService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

public class DeckController extends Controller {
    private final DeckService deckService;


    public DeckController(DeckService deckService) {
        this.deckService = deckService;
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
        try {
            String authenticationToken = getAuthorizationHeader(request);
            List<Card> cards = deckService.readDeck(authenticationToken);
            return json(Status.OK, cards);
        } catch (UserNotFound e) {
            return json(Status.NOT_FOUND, e);
        } catch (Exception e) {
            return json(Status.INTERNAL_SERVER_ERROR, e);
        }
    }


    public Response changeDeck(Request request) {
        Response response = new Response();

        return response;
    }
}
