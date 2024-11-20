package jules.pabst.application.monsterTradingCards.controller;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.service.CardService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Status;
import jules.pabst.application.monsterTradingCards.entity.Card;

import java.util.List;

public class CardsController extends Controller {
    private final CardService cardsService = new CardService();

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.GET)) {
            return read();
        }

        return null;
    }

    public Response read() {
        List<Card> cards = cardsService.read();
        StringBuilder builder = new StringBuilder();

        Response response = new Response();
        response.setStatus(Status.OK);
        response.setHeader("Content-Type", "application/json");

        for (Card card : cards) {
            builder.append("\"id\": \"%s\"\n\"name\": \"%s\"\n\"damage\": \"%.2f\"\n".formatted(
                    card.getId(),
                    card.getName().getName(),
                    card.getDamage()
            ));
        }
        response.setBody(
                "{ \n[\n%s]\n }"
                        .formatted(builder)
        );
        return response;
    }



}
