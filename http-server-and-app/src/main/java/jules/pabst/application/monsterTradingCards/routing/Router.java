package jules.pabst.application.monsterTradingCards.routing;

import jules.pabst.application.monsterTradingCards.controller.Controller;

import java.util.*;

public class Router {

    private final List<Route> routes;

    public Router() {
        this.routes = new ArrayList<>();
    }

    public Controller getController(String path) {
        for (Route route: this.routes) {
            if (!path.contains(route.getRoute())) {
                continue;
            }

            return route.getController();
        }

        throw new ControllerNotFound(path);
    }

    public void addRoute(String route, Controller controller) {
        this.routes.add(new Route(route, controller));
    }
}
