package jules.pabst.application.monsterTradingCards.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jules.pabst.application.monsterTradingCards.controller.Controller;
import jules.pabst.application.monsterTradingCards.exception.JsonParserException;
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
    private final Object battleLock = new Object();
    private String battleLog;

    public BattlesController(BattleService battleService) {
        this.battleService = battleService;
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.POST)) {
            return handleBattleRequest(request);
        }

        return returnMessage(Status.NOT_FOUND, "Endpoint not found");
    }


    private Response handleBattleRequest(Request request) {
        String auth = getAuthorizationToken(request);

        try {
            synchronized (battleLock) {
                // Check if there is already a player waiting
                if (!playerQueue.isEmpty()) {
                    String opponentAuth = playerQueue.poll();

                    // Ensure valid opponent
                    if (opponentAuth != null && !opponentAuth.equals(auth)) {
                        // Player 2 starts the battle
                        this.battleLog = battleService.battle(auth, opponentAuth);

                        // Notify Player 1 to proceed with the response
                        battleLock.notifyAll();

                        return returnMessage(Status.OK, battleLog); // Player 2 returns response
                    }
                }

                // No opponent available, Player 1 waits
                playerQueue.offer(auth);

                // Wait until Player 2 completes the battle or timeout occurs
                battleLock.wait(10000); // 10 seconds timeout


                if (battleLog != null) {
                    return returnMessage(Status.OK, battleLog); // Player 1 returns the same response
                }

                // Timeout occurred
                playerQueue.remove(auth); // Ensure Player 1 is not left in the queue
                return json(Status.REQUEST_TIMEOUT, "No opponent found. Please try again later.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return json(Status.INTERNAL_SERVER_ERROR, "An error occurred while processing your request.");
        }
    }

    protected Response returnMessage(Status status, String string) {
        Response response = new Response();
        response.setStatus(status);
        response.setHeader("Content-Type", "application/json");
        response.setBody(string);
        return response;
    }

}
