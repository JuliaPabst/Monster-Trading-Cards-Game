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

    public User(String uuid, String username, String password) {
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

    public void setUsername(String username) {
        this.username = username;
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


    public String getBio() {
        return bio;
    }

    public int getElo() {
        return elo;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getImage() {
        return image;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
