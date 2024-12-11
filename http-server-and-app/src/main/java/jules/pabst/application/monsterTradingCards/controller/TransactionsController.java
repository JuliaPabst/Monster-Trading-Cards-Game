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
        return json(Status.INTERNAL_SERVER_ERROR,  new ErrorResponse("Internal Server Error"));
    }

    public Response aquirePackage(Request request){
        try {
            String authenticationToken = getAuthorizationToken(request);
            User user = userService.getUserByAuthenticationToken(authenticationToken);

            AquirePackageDTO aquirePackageDTO =  packageService.checkCreditAndAquire(user);
            if(aquirePackageDTO != null){
                return json(Status.CREATED, aquirePackageDTO);
            }
            return json(Status.INTERNAL_SERVER_ERROR, new ErrorResponse("User does not have enough credit"));
        } catch(NotEnoughCredit e) {
            return json(Status.PAYMENT_REQUIRED, new ErrorResponse("Not enough credit to acquire a package"));
        } catch(NoPackagesOwned e) {
            return json(Status.BAD_REQUEST, new ErrorResponse("No packages owned"));
        } catch(AllPackagesOwned e) {
            return json(Status.CONFLICT, new ErrorResponse("No packages available"));
        } catch(MissingAuthorizationHeader e){
            return json(Status.UNAUTHORIZED, new ErrorResponse("Missing authorization header"));
        } catch(CouldNotAquirePackage e){
            return json(Status.INTERNAL_SERVER_ERROR, new ErrorResponse("Aquiring package failed"));
        } catch(UserNotFound e){
            return json(Status.NOT_FOUND, new ErrorResponse("User not found"));
        }
    }
}
