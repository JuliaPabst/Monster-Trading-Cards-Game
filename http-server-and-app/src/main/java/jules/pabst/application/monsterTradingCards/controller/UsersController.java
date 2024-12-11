package jules.pabst.application.monsterTradingCards.controller;

import jules.pabst.application.monsterTradingCards.DTOs.LoginTokenDTO;
import jules.pabst.application.monsterTradingCards.DTOs.UserCreationDTO;
import jules.pabst.application.monsterTradingCards.DTOs.UserDTO;
import jules.pabst.application.monsterTradingCards.DTOs.UserUpdateDTO;
import jules.pabst.application.monsterTradingCards.entity.ErrorResponse;
import jules.pabst.application.monsterTradingCards.entity.TokenRequest;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.*;
import jules.pabst.application.monsterTradingCards.service.UserService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;

import java.util.Optional;

public class UsersController extends Controller {

    private final UserService userService;

    public UsersController(UserService UserService) {
        this.userService = UserService;
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.POST)) {
            return create(request);
        } else if(request.getMethod().equals(Method.PUT)) {
            return putUserData(request);
        } else if (request.getMethod().equals(Method.GET)) {
            return readUserByName(request);
        }

        return json(Status.INTERNAL_SERVER_ERROR,  new ErrorResponse("Internal Server Error"));
    }

    private Response create(Request request) {
        try {
            User user = fromBody(request.getBody(), User.class);
            UserCreationDTO userCreationDTO = userService.create(user);
            return json(Status.CREATED, userCreationDTO);
        } catch (UserAlreadyExists e) {
            return json(Status.CONFLICT, new ErrorResponse("User already exists"));
        }
    }

    private Response readUserByName(Request request) {
        try {
            String[] pathParts = request.getPath().split("/");
            String name = "";
            if(pathParts.length < 3) {
                name = pathParts[1];
            } else {
                name = pathParts[2];
            }

            String authenticationToken = getAuthorizationHeader(request);
            UserDTO user2 = userService.getUserByName(name, authenticationToken);
            return json(Status.OK, user2);
        } catch(UserNotFound e){
            return json(Status.NOT_FOUND, new ErrorResponse(e.getMessage()));
        } catch(MissingAuthorizationHeader | InvalidUserCredentials e){
            return json(Status.UNAUTHORIZED, new ErrorResponse(e.getMessage()));
        }
    }

    private Response putUserData(Request request) {
        try{
            String authenticationToken = getAuthorizationHeader(request);
            UserUpdateDTO user = fromBody(request.getBody(), UserUpdateDTO.class);
            UserDTO userDTO = userService.updateUserData(user, authenticationToken);
            return json(Status.OK, userDTO);
        } catch(UserNotFound e){
            return json(Status.NOT_FOUND, new ErrorResponse(e.getMessage()));
        } catch(MissingAuthorizationHeader | InvalidUserCredentials e){
            return json(Status.UNAUTHORIZED, new ErrorResponse(e.getMessage()));
        } catch(UpdatingUserFailed e){
            return json(Status.INTERNAL_SERVER_ERROR, new ErrorResponse(e.getMessage()));
        }

    }
}
