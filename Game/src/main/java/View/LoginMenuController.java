package View;

import Controller.UserController;
import Controller.UserError;
import Model.Data.Data;
import Model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginMenuController {
    @FXML
    private PasswordField password;
    @FXML
    private TextField username;

    public void exit(MouseEvent mouseEvent) throws Exception{
        Data.saveCurrentUser();
        Data.saveUsers();
        Platform.exit();
    }

    public void register(MouseEvent mouseEvent) throws Exception{
        String usernameText = username.getText();
        String passwordText = password.getText();
        UserError result = UserController.register(usernameText,passwordText);
        if (usernameText.trim().equals("") ||passwordText.trim().equals("")){
            setAlert("Error","Empty fields!","").showAndWait();
            clear();
        } else if (result.equals(UserError.USERNAME_EXISTS)) {
            setAlert("Register Failed!","Username already exists!","").showAndWait();
            clear();
        } else if (result.equals(UserError.WEAK_PASSWORD)) {
            setAlert("Register Failed!","Your password is weak!","Password requirements: numbers, uppercase and lowercase letters.").showAndWait();
            clear();
        } else if (result.equals(UserError.SUCCESS)) {
            setAlert("Register Successful","Welcome "+usernameText+"!","").showAndWait();
            clear();
            Stage stage = Controller.getStage();
            new GameMenu().start(stage);
        }
    }
    public void login(MouseEvent mouseEvent) throws Exception{
        String usernameText = username.getText();
        String passwordText = password.getText();
        UserError result = UserController.login(usernameText,passwordText);
        if (usernameText.trim().equals("") ||passwordText.trim().equals("")){
            setAlert("Error","Empty fields!","").showAndWait();
            clear();
        } else if (result.equals(UserError.USERNAME_NOT_FOUND)) {
            setAlert("Login Failed!","Username not found!","").showAndWait();
            clear();
        } else if (result.equals(UserError.WRONG_PASSWORD)) {
            setAlert("Login Failed","Your password doesn't match!","").showAndWait();
            clear();
        } else if (result.equals(UserError.SUCCESS)) {
            setAlert("Login Successful","Welcome "+usernameText+"!","").showAndWait();
            clear();
            Stage stage = Controller.getStage();
            new GameMenu().start(stage);
        }
    }

    public void guest(MouseEvent mouseEvent) throws Exception{
        String randomUsername = User.createGuestUser().getUsername();
        setAlert("Guest Login Successful","Welcome "+randomUsername+"!","").showAndWait();
        clear();
        Stage stage = Controller.getStage();
        new GameMenu().start(stage);
    }


    public Alert setAlert(String title,String header,String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    public void clear() {
        username.setText("");
        password.setText("");
    }


}
