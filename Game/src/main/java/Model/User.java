package Model;

import Controller.UserController;
import Model.Data.Data;
import View.Game;

public class User {
    private String username;
    private String password;
    private GameInfo gameInfo;
    private String imageUrl = Game.class.getResource("/Images/Avatar_1.png").toString();
    private User(String username,String password){
        this.username = username;
        this.password = password;
    }
    private int easyHighScore=0;
    private int easyTime=0;
    private int mediumHighScore=0;
    private int mediumTime=0;
    private int hardHighScore=0;
    private int hardTime=0;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static User createUser(String username, String password) {
        User user = new User(username, Data.encrypt(password));
        Data.addUser(user);
        return user;
    }

    public static User createGuestUser() {
        String username = UserController.generateGuestUsername();
        User user = new User(username,"Password123");
        Data.guestUser = user;
        Data.currentUser = null;
        return user;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
        if (gameInfo.getDifficulty() == 1){
            if (gameInfo.getScore() > easyHighScore || (gameInfo.getScore() == easyHighScore && gameInfo.getTime() < easyTime)){
                easyHighScore = gameInfo.getScore();
                easyTime = gameInfo.getTime();
            }

        }
        else if (gameInfo.getDifficulty() == 2){
            if (gameInfo.getScore() > mediumHighScore || (gameInfo.getScore() == mediumHighScore && gameInfo.getTime() < mediumTime)){
                mediumHighScore = gameInfo.getScore();
                mediumTime = gameInfo.getTime();
            }

        }
        else if (gameInfo.getDifficulty() == 3){
            if (gameInfo.getScore() > hardHighScore || (gameInfo.getScore() == hardHighScore && gameInfo.getTime() < hardTime)){
                hardHighScore = gameInfo.getScore();
                hardTime = gameInfo.getTime();
            }

        }
        Data.saveUsers();
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public int getEasyHighScore() {
        return -easyHighScore;
    }

    public int getEasyTime() {
        return easyTime;
    }

    public int getMediumHighScore() {
        return -mediumHighScore;
    }

    public int getMediumTime() {
        return mediumTime;
    }

    public int getHardHighScore() {
        return -hardHighScore;
    }

    public int getHardTime() {
        return hardTime;
    }
}
