package jules.pabst.application.monsterTradingCards.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.exception.NotAuthorized;
import jules.pabst.application.monsterTradingCards.service.CardService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;

import java.util.List;

public class PackageController extends Controller {
    private final CardService cardService;

    public PackageController(CardService cardService) {
        this.cardService = cardService;
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.POST)) {
            return create(request);
        }

        return json(Status.NOT_FOUND, "Endpoint not found");
    }

    public Response create(Request request){
        try{
            String authToken = getAuthorizationToken(request);
            List<Card> cards = arrayFromBody(request.getBody(), new TypeReference<List<Card>>() {});

            cards = cardService.createPackage(authToken, cards);

            return json(Status.CREATED, cards);
        } catch (NotAuthorized e){
            return json(Status.UNAUTHORIZED, e.getMessage());
        } catch(Exception e){
            return json(Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
}
