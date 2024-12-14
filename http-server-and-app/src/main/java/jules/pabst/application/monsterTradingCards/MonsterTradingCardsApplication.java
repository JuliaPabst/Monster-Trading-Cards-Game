package jules.pabst.application.monsterTradingCards;

import jules.pabst.application.monsterTradingCards.controller.*;
import jules.pabst.application.monsterTradingCards.repository.*;
import jules.pabst.application.monsterTradingCards.routing.ControllerNotFound;
import jules.pabst.application.monsterTradingCards.routing.Router;
import jules.pabst.application.monsterTradingCards.service.*;
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
            System.out.println(request.getPath());
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
        PackageRepository packageRepository = new PackageDbRepository(connectionPool);
        CardDbRepository cardRepository = new CardDbRepository(connectionPool);
        CardService cardService = new CardService(cardRepository, packageRepository);
        PackageService packageService = new PackageService(packageRepository, userRepository, cardService, userService);
        DeckService deckService = new DeckService(cardRepository, userRepository, packageRepository);
        StatsService statsService = new StatsService(userRepository);
        ScoreboardService scoreboardService = new ScoreboardService(userRepository);
        BattleService battleService = new BattleService(userRepository, cardRepository, userService);

        this.router.addRoute("/cards", new CardsController(cardService, userService, packageService));
        this.router.addRoute("/deck", new DeckController(deckService, cardService));
        this.router.addRoute("/sessions", new SessionController(userRepository));
        this.router.addRoute("/transactions/packages", new TransactionsController(userService, packageService));
        this.router.addRoute("/packages", new PackageController(cardService, packageService));
        this.router.addRoute("/users", new UsersController(userService));
        this.router.addRoute("/stats", new StatsController(statsService));
        this.router.addRoute("/scoreboard", new ScoreboardController(scoreboardService));
        this.router.addRoute("/battles", new BattlesController(battleService));
    }
}