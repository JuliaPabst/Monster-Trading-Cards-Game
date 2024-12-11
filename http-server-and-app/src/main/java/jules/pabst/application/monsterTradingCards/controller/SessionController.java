package jules.pabst.application.monsterTradingCards.controller;

import jules.pabst.application.monsterTradingCards.DTOs.LoginTokenDTO;
import jules.pabst.application.monsterTradingCards.entity.ErrorResponse;
import jules.pabst.application.monsterTradingCards.entity.TokenRequest;
import jules.pabst.application.monsterTradingCards.exception.InvalidUserCredentials;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;
import jules.pabst.application.monsterTradingCards.service.TokenService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;

import java.util.Optional;

public class SessionController extends Controller{
    private final TokenService tokenService;

    public SessionController(UserRepository userRepository) {
        this.tokenService = new TokenService(userRepository);
    }
    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.POST)) {
            return authenticate(request);
        }
        return null;
    }

    private Response authenticate(Request request){
        try {
            TokenRequest tokenRequest = fromBody(request.getBody(), TokenRequest.class);
            LoginTokenDTO authenticatedTokenRequest = tokenService.authenticate(tokenRequest);

            return json(Status.OK, authenticatedTokenRequest);
        } catch (InvalidUserCredentials e) {
            return json(Status.UNAUTHORIZED, new ErrorResponse("Invalid User Credentials"));
        }
    }
}
