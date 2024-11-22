package jules.pabst.application.monsterTradingCards.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.ErrorResponse;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.exception.UserAlreadyExists;
import jules.pabst.application.monsterTradingCards.repository.UserMemoryRepository;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;
import jules.pabst.application.monsterTradingCards.service.CardService;
import jules.pabst.application.monsterTradingCards.service.UserService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;

import java.util.List;
import java.util.Optional;

public class UsersController extends Controller {

    private final UserService userService;

    public UsersController(UserRepository userRepository) {
        this.userService = new UserService(userRepository);
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.POST)) {
            return create(request);
        } else if (request.getMethod().equals(Method.GET)) {
            return readUserByName(request);
        }

        return json(Status.OK, null);
    }

    private Response create(Request request) {
        try {
            User user = fromBody(request.getBody(), User.class);
            user = userService.create(user);

            // to do: do not send back password
            return json(Status.CREATED, user);
        } catch (UserAlreadyExists e) {
            return json(Status.BAD_REQUEST, new ErrorResponse("User already exists"));
        }

    }

    private Response readUserByName(Request request) {
        String[] pathParts = request.getPath().split("/");
        String name = pathParts[1];
        Optional<User> user = userService.getUserByName(name);
        if (user.isPresent()) {
            return json(Status.OK, user);
        } else {
            return json(Status.NOT_FOUND, new ErrorResponse("User not found"));
        }
    }
}
