package jules.pabst.application.monsterTradingCards.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.ErrorResponse;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.CardNotOwned;
import jules.pabst.application.monsterTradingCards.exception.CardsNotFound;
import jules.pabst.application.monsterTradingCards.exception.NotRightAmountOfCards;
import jules.pabst.application.monsterTradingCards.exception.UserNotFound;
import jules.pabst.application.monsterTradingCards.service.CardService;
import jules.pabst.application.monsterTradingCards.service.DeckService;
import jules.pabst.application.monsterTradingCards.service.UserService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DeckController extends Controller {
    private final DeckService deckService;
    private final CardService cardService;


    public DeckController(DeckService deckService, CardService cardService) {
        this.deckService = deckService;
        this.cardService = cardService;
    }

    @Override
    public Response handle(Request request) {

        if(request.getMethod().equals(Method.GET)){
            return getDeck(request);
        } else if(request.getMethod().equals(Method.PUT)){
            return configureDeck(request);
        }
        return json(Status.NOT_FOUND, "Endpoint not found");
    }

    public Response getDeck(Request request) {
        try {
            String authenticationToken = getAuthorizationToken(request);
            List<Card> cards = deckService.readDeck(authenticationToken);
            return json(Status.OK, cards);
        } catch (UserNotFound e) {
            return json(Status.NOT_FOUND, e);
        } catch (Exception e) {
            return json(Status.INTERNAL_SERVER_ERROR, e);
        }
    }


    public Response configureDeck(Request request) {
        try{
            String authenticationToken = getAuthorizationToken(request);
            List<String> cardIds =  arrayFromBody(request.getBody(), new TypeReference<List<String>>() {});
            List<Card> cards = deckService.configureDeck(authenticationToken, cardIds);
            return json(Status.OK, cards);
        } catch(UserNotFound | CardsNotFound e){
            return json(Status.NOT_FOUND, new ErrorResponse(e.getMessage()));
        } catch(NotRightAmountOfCards | CardNotOwned e){
            return json(Status.BAD_REQUEST, new ErrorResponse(e.getMessage()));
        } catch(JsonProcessingException e){
            return json(Status.INTERNAL_SERVER_ERROR, new ErrorResponse(e.getMessage()));
        }
    }
}
