package jules.pabst.application.monsterTradingCards.controller;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.service.CardService;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Status;

public class CardsController {
    private final CardService cardsService = new CardService();
    public Response getCards() {
        Response response = new Response();
        response.setStatus(Status.OK);
        response.setHeader("Content-Type", "application/json");
        response.setBody(
                "{ \"cards\": \"{placeholder}\" }"
        );
        return response;
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
