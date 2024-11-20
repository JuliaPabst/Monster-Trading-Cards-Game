package jules.pabst.application.monsterTradingCards.controller;

import jules.pabst.application.monsterTradingCards.entity.Card;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.service.CardService;
import jules.pabst.application.monsterTradingCards.service.UserService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;

import java.util.List;

public class UsersController extends Controller {

    private final UserService userService = new UserService();

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.POST)) {
            return create(request);
        }

        return null;
    }

    public Response create(Request request) {
        User user = extractFromBody(request.getBody());
        user = userService.create(user);

        Response response = new Response();
        response.setStatus(Status.CREATED);
        response.setHeader("Content-Type", "application/json");
        response.setBody(
                "{ \"userId\": \"%s\", \n \"username\": \"%s\", \n\"password\": \"%s\" \n}"
                        .formatted(user.getId(), user.getUsername(), user.getPassword())
        );

        return response;
    }


    private User extractFromBody(String body) {
        String username = "";
        String password = "";

        String[] lines = body.split("\n");
        for (String line : lines) {
            if (!line.contains(":")) {
                continue;
            }

            String[] keyValue = line.split(":");
            String key = keyValue[0].trim().replace("\"", "");
            String value = keyValue[1]
                    .trim()
                    .replace("\"", "")
                    .replace(",", "");

            if (key.equals("Username")) {
                username = value;
            }
            if (key.equals("Password")) {
                password = value;
            }
        }

        return new User(username, password);
    }
}
