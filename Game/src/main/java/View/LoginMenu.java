package View;

import Model.Data.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class LoginMenu extends Application {
    public static void main(String... args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Controller.setStage(stage);
        if (Data.currentUser != null){
            new GameMenu().start(stage);
            return;
        }
        URL url = LoginMenu.class.getResource("/FXML/loginMenu.fxml");
        assert url != null;
        stage.setScene(new Scene(FXMLLoader.load(url)));
        stage.show();
    }
}
