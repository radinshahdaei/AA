package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class GameMenu extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL url = LoginMenu.class.getResource("/FXML/gameMenu.fxml");
        assert url != null;
        stage.setScene(new Scene(FXMLLoader.load(url)));
        stage.show();
    }
}
