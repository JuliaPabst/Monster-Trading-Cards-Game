package jules.pabst.application.monsterTradingCards.DTOs;

public class AquirePackageDTO {
    private String packageId;
    private String username;

    public AquirePackageDTO(String packageId, String username) {
        this.packageId = packageId;
        this.username = username;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
