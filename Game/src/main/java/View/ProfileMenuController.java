package View;

import Controller.UserController;
import Controller.UserError;
import Model.Data.Data;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class ProfileMenuController implements Initializable {
    @FXML
    private PasswordField oldPass;

    @FXML
    private PasswordField newPass;

    @FXML
    private TextField username;

    @FXML
    private Button changeUsername;

    @FXML
    private Button changePassword;

    @FXML
    private ComboBox<String> changeAvatar;

    @FXML
    private ImageView imageView;

    @FXML
    private Label currentUsername;

    public static String showCurrentUsername(){
        return "Username:\n"+Data.currentUser.getUsername();
    }

    public void usernameMarked(MouseEvent mouseEvent) throws Exception{
        String usernameText = username.getText();
        UserError result = UserController.changeUsername(usernameText);
        if (Data.currentUser == null) {
            setAlert("Error","You don't have an account!","").showAndWait();
            clear();
        } else if (usernameText.trim().equals("")){
            setAlert("Error","Empty fields!","").showAndWait();
            clear();
        } else if (result.equals(UserError.USERNAME_EXISTS)) {
            setAlert("Username Change Failed!","Username already exists!","").showAndWait();
            clear();
        } else if (result.equals(UserError.SUCCESS)) {
            setAlert("Username Change Successful","Your username has been changed!","Hello "+usernameText+"!").showAndWait();
            clear();
        }
    }

    public void passwordMarked(MouseEvent mouseEvent) throws Exception{
        String oldPassText = oldPass.getText();
        String newPassText = newPass.getText();
        UserError result = UserController.changePassword(oldPassText,newPassText);
        if (Data.currentUser == null) {
            setAlert("Error","You don't have an account!","").showAndWait();
            clear();
        } else if (oldPassText.trim().equals("") || newPassText.trim().equals("")){
            setAlert("Error","Empty fields!","").showAndWait();
            clear();
        } else if (result.equals(UserError.WRONG_PASSWORD)) {
            setAlert("Password Change Failed!", "Your password doesn't match!", "").showAndWait();
            clear();
        } else if (result.equals(UserError.WEAK_PASSWORD)) {
            setAlert("Password Change Failed!", "Your password is weak!","Password requirements: numbers, uppercase and lowercase letters.").showAndWait();
            clear();
        } else if (result.equals(UserError.SUCCESS)) {
            setAlert("Password Change Successful!", "Your password has been changed!", "").showAndWait();
            clear();
        }

    }

    public void avatarMarked(MouseEvent mouseEvent) throws Exception{}


    public Alert setAlert(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    public void clear() {
        username.setText("");
        oldPass.setText("");
        newPass.setText("");
    }

    public void logout(MouseEvent mouseEvent) throws Exception{
        Data.currentUser = null;
        Data.saveCurrentUser();
        Stage stage = Controller.getStage();
        new LoginMenu().start(stage);
    }


    public void back(MouseEvent mouseEvent) throws Exception{
        Stage stage = Controller.getStage();
        new GameMenu().start(stage);
    }

    public void changeAvatar(ActionEvent actionEvent){
        Stage stage = Controller.getStage();
        String option = changeAvatar.getValue();
        Image image = null;
        String url = null;
        if (option.equals("Optional")){
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );

            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                image = new Image(selectedFile.toURI().toString());
                url = selectedFile.toURI().toString();
            }
        } else if (option.equals("Random")){
            Random random = new Random();
            int randomNumber = random.nextInt(3) + 1;
            String buffer = "/Images/Avatar_"+randomNumber+".png";
            image =  new Image(Game.class.getResource(buffer).toString());
            url = Game.class.getResource(buffer).toString();
        } else{
           image =  new Image(Game.class.getResource("/Images/"+option+".png").toString());
           url = Game.class.getResource("/Images/"+option+".png").toString();
        }
        Data.currentUser.setImageUrl(url);
        Data.saveCurrentUser();
        Data.saveUsers();
        imageView.setImage(image);
    }

    public void remove(MouseEvent mouseEvent) throws Exception{
        Data.users.remove(Data.currentUser);
        Data.currentUser = null;
        Data.saveCurrentUser();
        Data.saveUsers();
        Stage stage = Controller.getStage();
        new LoginMenu().start(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentUsername.setText(showCurrentUsername());
        if (Data.currentUser.getImageUrl() != null)
            imageView.setImage(new Image(Data.currentUser.getImageUrl()));
    }
}
