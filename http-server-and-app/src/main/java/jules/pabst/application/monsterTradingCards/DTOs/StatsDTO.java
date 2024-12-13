package jules.pabst.application.monsterTradingCards.DTOs;

public class StatsDTO {
    private String username;
    private int elo;
    private int wins;
    private int losses;

    public StatsDTO(String username, int elo, int wins, int losses) {
        this.username = username;
        this.elo = elo;
        this.wins = wins;
        this.losses = losses;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }
}
