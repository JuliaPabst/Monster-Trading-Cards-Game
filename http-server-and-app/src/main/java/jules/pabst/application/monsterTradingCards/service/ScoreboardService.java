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

        users.sort(Comparator.comparingInt(User::getElo).reversed());

        if(!users.isEmpty()){
            int place = 0;
            for (int i = 0; i < users.size(); i++) {
                if(i != 0 && users.get(i).getElo() != users.get(i-1).getElo()){
                    place++;
                } else if(i == 0){
                    place++;
                }
                ScoreboardDTO stats = new ScoreboardDTO(place, users.get(i).getUsername(), users.get(i).getElo(), users.get(i).getWins(), users.get(i).getLosses());
                scoreboard.add(stats);
            }

            scoreboard.sort(Comparator.comparingInt(ScoreboardDTO::getElo));
        }

        return scoreboard;
    }

}
