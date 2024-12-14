package jules.pabst.application.monsterTradingCards.DTOs;

public class BattleDTO {
    String battleLog;
    ScoreboardDTO scoreboard;

    BattleDTO(String battleLog, ScoreboardDTO scoreboard) {
        this.battleLog = battleLog;
        this.scoreboard = scoreboard;
    }

    public String getBattleLog() {
        return battleLog;
    }

    public void setBattleLog(String battleLog) {
        this.battleLog = battleLog;
    }

    public ScoreboardDTO getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(ScoreboardDTO scoreboard) {
        this.scoreboard = scoreboard;
    }
}
