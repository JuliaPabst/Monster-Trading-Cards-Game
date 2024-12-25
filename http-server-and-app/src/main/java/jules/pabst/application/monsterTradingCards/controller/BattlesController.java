package jules.pabst.application.monsterTradingCards.controller;

import jules.pabst.application.monsterTradingCards.DTOs.BattleDTO;
import jules.pabst.application.monsterTradingCards.service.BattleService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BattlesController extends Controller {
    private final BattleService battleService;
    private final ConcurrentHashMap<String, String> battleQueue = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public BattlesController(BattleService battleService) {
        this.battleService = battleService;
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.POST)) {
            return handleBattleRequest(request);
        }

        return json(Status.NOT_FOUND, "Endpoint not found");
    }

    private synchronized Response handleBattleRequest(Request request) {
        String auth = getAuthorizationToken(request);

        if (battleQueue.isEmpty()) {
            battleQueue.put(auth, auth);
            return json(Status.OK, "Waiting for an opponent...");
        } else {
            String opponentAuth = battleQueue.keys().nextElement();
            battleQueue.remove(opponentAuth);

            executor.submit(() -> {
                String battleLog = battleService.battle(auth, opponentAuth);
                System.out.println(battleLog);
            });

            return json(Status.OK, "Battle started with an opponent!");
        }
    }
}
