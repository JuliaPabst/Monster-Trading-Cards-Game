package jules.pabst.application.monsterTradingCards.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.ErrorResponse;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.CardsNotFound;
import jules.pabst.application.monsterTradingCards.exception.MissingAuthorizationHeader;
import jules.pabst.application.monsterTradingCards.exception.UserNotFound;
import jules.pabst.application.monsterTradingCards.service.CardService;
import jules.pabst.application.monsterTradingCards.service.PackageService;
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
    private final PackageService packageService;

    public CardsController(CardService cardsService, UserService userService, PackageService packageService) {
        this.cardsService = cardsService;
        this.userService = userService;
        this.packageService = packageService;
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.GET)) {
            return getByUserToken(request);
        }

        return null;
    }

    public Response getByUserToken(Request request) {
        try {
            String authenticationToken = getAuthorizationHeader(request);
            Optional<User> user = userService.getUserByAuthenticationToken(authenticationToken);
            List<Card> cards = new ArrayList<>();
            if (user.isPresent()) {
                cards = cardsService.readByUserToken(user.get());
            }

           return json(Status.OK, cards);

        } catch(MissingAuthorizationHeader e){
            return json(Status.UNAUTHORIZED, new ErrorResponse("Missing authorization header"));
        } catch (UserNotFound | CardsNotFound e) {
            return json(Status.NOT_FOUND, e);
        }
    }
}

