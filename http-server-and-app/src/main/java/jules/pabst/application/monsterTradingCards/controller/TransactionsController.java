package jules.pabst.application.monsterTradingCards.controller;

import jules.pabst.application.monsterTradingCards.DTOs.AquirePackageDTO;
import jules.pabst.application.monsterTradingCards.entity.ErrorResponse;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.*;
import jules.pabst.application.monsterTradingCards.service.PackageService;
import jules.pabst.application.monsterTradingCards.service.UserService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;

import java.util.Optional;

public class TransactionsController extends Controller{
    UserService userService;
    PackageService packageService;

    public TransactionsController(UserService userservice, PackageService packageService){
        this.userService = userservice;
        this.packageService = packageService;
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
            AquirePackageDTO aquirePackageDTO =  packageService.checkCreditAndAquire(authToken);
            return json(Status.CREATED, aquirePackageDTO);
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
