package jules.pabst.application.monsterTradingCards.controller;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.ErrorResponse;
import jules.pabst.application.monsterTradingCards.exception.*;
import jules.pabst.application.monsterTradingCards.service.CardService;
import jules.pabst.application.monsterTradingCards.service.UserService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;

import java.util.List;

public class TransactionsController extends Controller{
    UserService userService;
    CardService cardService;

    public TransactionsController(UserService userservice, CardService cardservice) {
        this.userService = userservice;
        this.cardService = cardService;
    }

    public Response handle(Request request){
        if(request.getMethod().equals(Method.POST)){
            return aquirePackage(request);
        }

        return json(Status.NOT_FOUND, "Endpoint not found");
    }

    public Response aquirePackage(Request request){
        try {
            String authToken = getAuthorizationToken(request);
            List<Card> aquiredCards =  cardService.checkCreditAndAquire(authToken);
            return json(Status.CREATED, aquiredCards);
        } catch(MissingAuthorizationHeader e){
            return json(Status.UNAUTHORIZED, new ErrorResponse(e.getMessage()));
        } catch(UserNotFound e){
            return json(Status.NOT_FOUND, new ErrorResponse(e.getMessage()));
        } catch(NotEnoughCredit e) {
            return json(Status.PAYMENT_REQUIRED, new ErrorResponse(e.getMessage()));
        } catch(AllPackagesOwned e) {
            return json(Status.CONFLICT, new ErrorResponse(e.getMessage()));
        } catch (Exception e){
            return json(Status.INTERNAL_SERVER_ERROR, new ErrorResponse(e.getMessage()));
        }
    }
}
