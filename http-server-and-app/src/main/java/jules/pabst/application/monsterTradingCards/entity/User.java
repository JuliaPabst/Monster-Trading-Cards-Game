package jules.pabst.application.monsterTradingCards.entity;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String uuid = "";
    private String username;
    private String password;
    private String bio = "";
    private String image = "";
    private int elo = 0;
    private int wins = 0;
    private int losses = 0;
    private String token;
    private int credit;
    private List<Card> deck;
    private List<Card> stack;

    public User() {
        this.deck = new ArrayList<>();
        this.stack = new ArrayList<>();
    }

    public User(String uuid, String username, String password) {
        this.deck = new ArrayList<Card>();
        this.stack = new ArrayList<Card>();
        this.username = username;
        this.password = password;
        this.uuid = uuid;
        this.credit = 20;
    }


    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
