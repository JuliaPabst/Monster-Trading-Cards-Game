package jules.pabst.application.monsterTradingCards.entity;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id = "";
    private String username;
    private String password;
    private String bio = "";
    private String image = "";
    private int elo = 0;
    private int wins = 0;
    private int losses = 0;
    private String token;
    private List<Card> deck;
    private List<Card> stack;

    public User() {
        this.deck = new ArrayList<>();
        this.stack = new ArrayList<>();
    }

    public User(String username, String password) {
        this.deck = new ArrayList<Card>();
        this.stack = new ArrayList<Card>();
        this.username = username;
        this.password = password;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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
}
