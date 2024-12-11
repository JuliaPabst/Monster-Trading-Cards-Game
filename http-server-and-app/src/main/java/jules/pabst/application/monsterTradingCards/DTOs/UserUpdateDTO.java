package jules.pabst.application.monsterTradingCards.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserUpdateDTO {
    private final String name;
    private final String bio;
    private final String image;

    UserUpdateDTO(@JsonProperty("Name") String name, @JsonProperty("Bio") String bio, @JsonProperty("Image") String image) {
        this.name = name;
        this.bio = bio;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getImage() {
        return image;
    }
}
