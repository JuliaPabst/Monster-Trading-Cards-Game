package jules.pabst.application.monsterTradingCards;

import jules.pabst.application.monsterTradingCards.controller.*;
import jules.pabst.server.Application;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;
import jules.pabst.server.http.Method;

public class MonsterTradingCardsApplication implements Application {
    private final CardsController cardsController = new CardsController();
    private final GameController gameController = new GameController();
    private final PackagesController packagesController = new PackagesController();
    private final TradingController tradingController = new TradingController();
    private final UsersController usersController = new UsersController();

    @Override
    public Response handle(Request request) {
        if (
                request.getPath().startsWith("/cards")
                        && request.getMethod() == Method.GET
        ) {
            return cardsController.getCards();
        } else if (
                request.getPath().startsWith("/deck")
                        && request.getMethod() == Method.GET
        ){
            return cardsController.getDeck();
        } else if (
                request.getPath().startsWith("/deck")
                        && request.getMethod() == Method.PUT
        ){
            return cardsController.changeDeck(request);
        }  else if (
                request.getPath().startsWith("/stats")
                        && request.getMethod() == Method.GET
        ){
            return gameController.getStats();
        } else if (
                request.getPath().startsWith("/scoreboard")
                        && request.getMethod() == Method.GET
        ){
            return gameController.getScoreboard();
        } else if (
                request.getPath().startsWith("/battles")
                        && request.getMethod() == Method.POST
        ){
            return gameController.startBattle();
        } else if (
                request.getPath().startsWith("/packages")
                        && request.getMethod() == Method.POST
        ){
            return packagesController.createPackage(request);
        } else if (
                request.getPath().startsWith("/transactions/packages")
                        && request.getMethod() == Method.POST
        ){
            return packagesController.aquirePackage();
        } else if (
                request.getPath().startsWith("/tradings")
                        && request.getMethod() == Method.GET
        ){
            return tradingController.getTradingDeals();
        } else if (
                request.getPath().startsWith("/tradings/")
                        && request.getMethod() == Method.POST
        ){
            return tradingController.makeTradingDeal();
        } else if (
                request.getPath().startsWith("/tradings")
                        && request.getMethod() == Method.POST
        ){
            return tradingController.createTradingDeal();
        } else if (
                request.getPath().startsWith("/tradings")
                        && request.getMethod() == Method.DELETE
        ){
            return tradingController.deleteTradingDeal();
        } else if (
                request.getPath().startsWith("/users")
                        && request.getMethod() == Method.POST
        ){
            return usersController.createUser(request);
        } else if (
                request.getPath().startsWith("/users")
                        && request.getMethod() == Method.GET
        ){
            return usersController.getUserByUsername();
        } else if (
                request.getPath().startsWith("/users")
                        && request.getMethod() == Method.PUT
        ){
            return usersController.updateUser(request);
        } else if (
                request.getPath().startsWith("/sessions")
                        && request.getMethod() == Method.POST
        ){
            return usersController.logIn(request);
        }

        return new Response();
    }
}