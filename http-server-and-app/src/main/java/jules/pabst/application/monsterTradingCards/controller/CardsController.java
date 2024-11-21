package jules.pabst.application.monsterTradingCards.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.service.CardService;
import jules.pabst.application.monsterTradingCards.service.UserService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Status;
import jules.pabst.application.monsterTradingCards.entity.Card;

import java.util.List;

public class CardsController extends Controller {
    private final CardService cardsService;

    public CardsController() {
        this.cardsService = new CardService();
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.GET)) {
            return read();
        }

        return null;
    }

    public Response read() {
        List<Card> cards = cardsService.getAll();

        return json(Status.OK, cards);
    }



}
