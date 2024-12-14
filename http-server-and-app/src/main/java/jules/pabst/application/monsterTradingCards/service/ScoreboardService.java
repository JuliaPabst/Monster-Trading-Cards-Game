package jules.pabst.application.monsterTradingCards.service;

import jules.pabst.application.monsterTradingCards.DTOs.ScoreboardDTO;
import jules.pabst.application.monsterTradingCards.DTOs.StatsDTO;
import jules.pabst.application.monsterTradingCards.entity.User;
import jules.pabst.application.monsterTradingCards.repository.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreboardService {
    UserRepository userRepository;

    public ScoreboardService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<ScoreboardDTO> readSortedScoreboard(){
        List<ScoreboardDTO> scoreboard = new ArrayList<>();
        List<User> users = userRepository.findAllUsers();

        if(!users.isEmpty()){
            int place = 0;
            for (User user : users) {
                place++;
                ScoreboardDTO stats = new ScoreboardDTO(place, user.getUsername(), user.getElo(), user.getWins(), user.getLosses());
                scoreboard.add(stats);
            }

            scoreboard.sort(Comparator.comparingInt(ScoreboardDTO::getElo));
        }

        return scoreboard;
    }

}
