package jules.pabst.application.monsterTradingCards.DTOs;

import jules.pabst.application.monsterTradingCards.entity.Card;

import java.util.ArrayList;
import java.util.List;

public class UserCreationDTO {
        private String username;
        private String token;

        public UserCreationDTO(String username, String token) {
            this.username = username;
            this.token = token;
        }

        public String getUsername() {
            return username;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

}
