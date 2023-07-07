package View;

import Model.Data.Data;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class GameMenuController {
    public void exit(MouseEvent mouseEvent) throws Exception{
        Data.currentUser = null;
        Data.saveCurrentUser();
        Stage stage = Controller.getStage();
        new LoginMenu().start(stage);
    }
    public void profile(MouseEvent mouseEvent) throws Exception{
        if (Data.currentUser == null) {
            setAlert("Error","you don't have an account!","").showAndWait();
        }
        else {
            Stage stage = Controller.getStage();
            new ProfileMenu().start(stage);
        }

    }
    public void startGame(MouseEvent mouseEvent) throws Exception{
        Data.newGame = true;
        Stage stage = Controller.getStage();
        new Game().start(stage);
    }

    public void loadGame(MouseEvent mouseEvent) throws Exception{
        if (Data.currentUser == null) {
            setAlert("Error","you don't have an account!","").showAndWait();
        }
        else {
            Data.newGame = false;
            Stage stage = Controller.getStage();
            new Game().start(stage);
        }

    }

    public void highScores(MouseEvent mouseEvent) throws Exception{
        Stage stage = Controller.getStage();
        new ScoreBoard().start(stage);
    }

    public void settings(MouseEvent mouseEvent) throws Exception{
        Stage stage = Controller.getStage();
        new Settings().start(stage);
    }

    public Alert setAlert(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }
}
