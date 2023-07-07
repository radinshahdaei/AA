package Model.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import Model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Data {
    public static User currentUser;
    public static User guestUser;
    public static ArrayList<User> users;

    public static boolean newGame = true;
    public static int difficulty = 1;
    public static int formOfAngles = 1;
    public static boolean muted = false;

    static {
        users = loadUsers();
        currentUser = loadCurrentUser();
        guestUser = null;
        if (users == null) users = new ArrayList<>();
    }

    public static ArrayList<User> sortEasyUsers(){
        ArrayList<User> sorted = (ArrayList<User>) users.clone();
        Comparator<User> sorter = Comparator.comparing(User::getEasyHighScore).thenComparing(User::getEasyTime);
        Collections.sort(sorted, sorter);
        return sorted;
    }

    public static ArrayList<User> sortMediumUsers(){
        ArrayList<User> sorted = (ArrayList<User>) users.clone();
        Comparator<User> sorter = Comparator.comparing(User::getMediumHighScore).thenComparing(User::getMediumTime);
        Collections.sort(sorted, sorter);
        return sorted;
    }

    public static ArrayList<User> sortHardUsers(){
        ArrayList<User> sorted = (ArrayList<User>) users.clone();
        Comparator<User> sorter = Comparator.comparing(User::getHardHighScore).thenComparing(User::getHardTime);
        Collections.sort(sorted, sorter);
        return sorted;
    }

    public static void addUser(User user){
        users.add(user);
        currentUser = user;
        saveCurrentUser();
        saveUsers();
    }


    public static User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) return user;
        }
        return null;
    }

    public static void saveCurrentUser() {
        String json = new Gson().toJson(currentUser);
        try (FileWriter writer = new FileWriter("src/main/java/Model/Data/Json/currentUser.json")) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveUsers() {
        String json = new Gson().toJson(users);
        try (FileWriter writer = new FileWriter("src/main/java/Model/Data/Json/users.json")) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static User loadCurrentUser() {
        File file = new File("src/main/java/Model/Data/Json/currentUser.json");
        Scanner reader = null;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert reader != null;
        StringBuilder data = new StringBuilder();
        while (reader.hasNextLine()) {
            data.append(reader.nextLine());
        }
        return new Gson().fromJson(data.toString(), User.class);
    }
    public static ArrayList<User> loadUsers() {
        File file = new File("src/main/java/Model/Data/Json/users.json");
        Scanner reader = null;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert reader != null;
        StringBuilder data = new StringBuilder();
        while (reader.hasNextLine()) {
            data.append(reader.nextLine());
        }
        Type type = new TypeToken<ArrayList<User>>(){}.getType();
        return new Gson().fromJson(data.toString(), type);
    }
    public static String encrypt(String originalString) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean decrypt(String encryptedString, String originalString) {
        String decryptedHash = encrypt(originalString);
        return decryptedHash.equals(encryptedString);
    }
}
