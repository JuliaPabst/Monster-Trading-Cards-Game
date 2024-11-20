package jules.pabst.application.monsterTradingCards.controller;

import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;

public abstract class Controller {
    public abstract Response handle(Request request);
}
