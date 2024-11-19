package jules.pabst;

import jules.pabst.application.monsterTradingCards.MonsterTradingCardsApplication;
import jules.pabst.server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(new MonsterTradingCardsApplication());
        server.start();
    }
}