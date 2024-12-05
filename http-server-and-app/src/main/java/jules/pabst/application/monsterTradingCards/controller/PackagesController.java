package jules.pabst.application.monsterTradingCards.controller;

import jules.pabst.application.monsterTradingCards.DTOs.UserCreationDTO;
import jules.pabst.application.monsterTradingCards.entity.ErrorResponse;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.exception.UserAlreadyExists;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;

public class PackagesController extends Controller {
    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.POST)) {
            return create(request);
        }

        return json(Status.OK, null);
    }

    public Response create(Request request){
        Card card = fromBody(request.getBody(), Card.class);
        card = cardService.create(card);
        return json(Status.CREATED, card);
    }
}
