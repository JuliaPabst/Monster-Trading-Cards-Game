package jules.pabst.application.monsterTradingCards;

import jules.pabst.application.monsterTradingCards.controller.*;
import jules.pabst.application.monsterTradingCards.repository.UserDbRepository;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;
import jules.pabst.application.monsterTradingCards.routing.ControllerNotFound;
import jules.pabst.application.monsterTradingCards.routing.Router;
import jules.pabst.application.monsterTradingCards.service.UserService;
import jules.pabst.server.Application;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;
import jules.pabst.application.monsterTradingCards.data.ConnectionPool;


public class MonsterTradingCardsApplication implements Application {

    private final Router router;

    public MonsterTradingCardsApplication() {
        this.router = new Router();

        this.initializeRoutes();
    }

    @Override
    public Response handle(Request request) {

        try {
            Controller controller
                    = this.router.getController(request.getPath());
            return controller.handle(request);

        } catch (ControllerNotFound e) {
            Response response = new Response();
            response.setStatus(Status.NOT_FOUND);

            response.setHeader("Content-Type", "application/json");
            response.setBody(
                    "{\"error\": \"Path: %s not found\" }"
                            .formatted(e.getMessage())
            );

            return response;
        }
    }

    private void initializeRoutes() {
        ConnectionPool connectionPool = new ConnectionPool();
        UserRepository userRepository = new UserDbRepository(connectionPool);
        UserService userService = new UserService(userRepository);

        this.router.addRoute("/cards", new CardsController());
        this.router.addRoute("/deck", new DeckController());
        this.router.addRoute("/sessions", new SessionController(userRepository));

        this.router.addRoute("/users", new UsersController(userService));
    }
}