package Controller;

import Model.Data.Data;
import Model.User;

import java.util.Random;

public class UserController {
    public static UserError register(String username,String password){
        if (Data.findUserByUsername(username) != null) return UserError.USERNAME_EXISTS;
        if (!checkPasswordStrength(password)) return UserError.WEAK_PASSWORD;
        User.createUser(username,password);
        Data.saveCurrentUser();
        return UserError.SUCCESS;
    }
    public static UserError login(String username,String password){
        if (Data.findUserByUsername(username) == null) return UserError.USERNAME_NOT_FOUND;
        if (!checkUsernameAndPassword(username,password)) return UserError.WRONG_PASSWORD;
        Data.currentUser = Data.findUserByUsername(username);
        Data.saveCurrentUser();
        return UserError.SUCCESS;
    }

    public static boolean checkPasswordStrength(String password){
        return (password.length() >= 8
        && password.matches(".*[a-z].*")
        && password.matches(".*[A-Z].*")
        && password.matches(".*[0-9].*"));
    }

    public static boolean checkUsernameAndPassword(String username,String password){
        User user = Data.findUserByUsername(username);
        assert user != null;
        return user.getPassword().equals(Data.encrypt(password));
    }

    public static UserError changeUsername(String username) {
        if (Data.currentUser == null) return null;
        if (Data.findUserByUsername(username) != null)  return UserError.USERNAME_EXISTS;
        Data.currentUser.setUsername(username);
        Data.saveCurrentUser();
        Data.saveUsers();
        return UserError.SUCCESS;
    }

    public static UserError changePassword(String oldPass,String newPass){
        if (Data.currentUser == null) return null;
        if (!Data.currentUser.getPassword().equals(Data.encrypt(oldPass))) return UserError.WRONG_PASSWORD;
        if (!checkPasswordStrength(newPass)) return UserError.WEAK_PASSWORD;
        Data.currentUser.setPassword(Data.encrypt(newPass));
        Data.saveCurrentUser();
        Data.saveUsers();
        return UserError.SUCCESS;
    }

    public static String generateRandomUsername() {
        String[] adjectives = {"Happy", "Silly", "Crazy", "Funny", "Curious", "Brave", "Gentle", "Wild", "Creative", "Witty"};
        String[] nouns = {"Cat", "Dog", "Elephant", "Monkey", "Tiger", "Lion", "Dolphin", "Butterfly", "Dragon", "Penguin"};
        Random random = new Random();
        String adjective = adjectives[random.nextInt(adjectives.length)];
        String noun = nouns[random.nextInt(nouns.length)];
        int number = random.nextInt(1000);
        return adjective + noun + number;
    }

    public static String generateGuestUsername() {
        String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        sb.append("Guest_");
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(ALPHANUMERIC.length());
            char randomChar = ALPHANUMERIC.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }



}
