package jules.pabst.application.monsterTradingCards.controller;

import jules.pabst.application.monsterTradingCards.controller.Controller;
import jules.pabst.application.monsterTradingCards.service.BattleService;
import jules.pabst.server.http.Method;
import jules.pabst.server.http.Request;
import jules.pabst.server.http.Response;
import jules.pabst.server.http.Status;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class BattlesController extends Controller {
    private final BattleService battleService;
    private final BlockingQueue<String> playerQueue = new LinkedBlockingQueue<>();

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

    private Response handleBattleRequest(Request request) {
        String auth = getAuthorizationToken(request);

        try {
            // Attempt to pair with another player
            String opponentAuth = playerQueue.poll(10, TimeUnit.SECONDS);

            if (opponentAuth == null) {
                // No opponent found, add the current player to the queue and wait
                playerQueue.put(auth);
                opponentAuth = playerQueue.poll(10, TimeUnit.SECONDS);

                if (opponentAuth == null || opponentAuth.equals(auth)) {
                    // Timeout or self-pairing occurred, clean up and notify the player
                    playerQueue.remove(auth); // Ensure the current player is not left in the queue
                    return json(Status.REQUEST_TIMEOUT, "No opponent found. Please try again later.");
                }
            }

            // Run the battle logic
            String battleLog = battleService.battle(auth, opponentAuth);

            // Respond with the battle log
            return json(Status.OK, battleLog);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return json(Status.INTERNAL_SERVER_ERROR, "An error occurred while processing your request.");
        }
    }
}
