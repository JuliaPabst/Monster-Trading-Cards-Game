package jules.pabst.application.monsterTradingCards.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.ErrorResponse;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.CardsNotFound;
import jules.pabst.application.monsterTradingCards.exception.MissingAuthorizationHeader;
import jules.pabst.application.monsterTradingCards.exception.NoPackagesOwned;
import jules.pabst.application.monsterTradingCards.exception.UserNotFound;
import jules.pabst.application.monsterTradingCards.service.CardService;
import jules.pabst.application.monsterTradingCards.service.UserService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Status;
import jules.pabst.application.monsterTradingCards.entity.Card;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardsController extends Controller {
    private final CardService cardsService;
    private final UserService userService;

    public CardsController(CardService cardsService, UserService userService) {
        this.cardsService = cardsService;
        this.userService = userService;
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.GET)) {
            return getByUserToken(request);
        }

        return json(Status.NOT_FOUND, "Endpoint not found");
    }

    public Response getByUserToken(Request request) {
        try {
            String authenticationToken = getAuthorizationToken(request);
            User user = userService.getUserByAuthenticationToken(authenticationToken);
            List<Card> cards = new ArrayList<>();

            cards = cardsService.readByUserToken(user);


           return json(Status.OK, cards);

        } catch(MissingAuthorizationHeader e){
            return json(Status.UNAUTHORIZED, new ErrorResponse(e.getMessage()));
        } catch (UserNotFound | CardsNotFound e) {
            return json(Status.NOT_FOUND, new ErrorResponse(e.getMessage()));
        } catch(NoPackagesOwned e){
            return json(Status.NOT_FOUND, new ErrorResponse(e.getMessage()));
        }
    }
}

