package jules.pabst.application.monsterTradingCards.DTOs;

import jules.pabst.application.monsterTradingCards.entity.Card;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {
    private String uuid = "";
    private String username;
    private String bio = "";
    private String image = "";
    private int elo = 0;
    private int wins = 0;
    private int losses = 0;
    private String token = "";
    private int credit;


    public UserDTO(String uuid, String username, String bio, String image, int elo, int wins, int losses, String token, int credit) {
        this.uuid = uuid;
        this.username = username;
        this.bio = bio;
        this.image = image;
        this.elo = elo;
        this.wins = wins;
        this.losses = losses;
        this.token = token;
        this.credit = credit;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }


    public int getCredit() {
        return credit;
    }

    public String getBio() {
        return bio;
    }

    public String getImage() {
        return image;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
