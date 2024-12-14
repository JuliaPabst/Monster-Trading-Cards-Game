package jules.pabst.application.monsterTradingCards.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.exception.InvalidUserCredentials;
import jules.pabst.application.monsterTradingCards.exception.NotAuthorized;
import jules.pabst.application.monsterTradingCards.exception.UserAlreadyExists;
import jules.pabst.application.monsterTradingCards.service.CardService;
import jules.pabst.application.monsterTradingCards.service.PackageService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;

import java.util.List;
import java.util.UUID;

public class PackageController extends Controller {
    private final CardService cardService;
    private final PackageService packageService;

    public PackageController(CardService cardService, PackageService packageService) {
        this.cardService = cardService;
        this.packageService = packageService;
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

            cards = packageService.createPackage(authToken, cards);

            return json(Status.CREATED, cards);
        } catch (NotAuthorized e){
            return json(Status.UNAUTHORIZED, e.getMessage());
        } catch(Exception e){
            return json(Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
}
