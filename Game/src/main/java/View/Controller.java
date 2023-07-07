package View;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Controller {
    public static Stage stage;

    public static void setStage(Stage stage) {
        Controller.stage = stage;
        stage.setResizable(false);
    }

    public static Stage getStage() {
        return stage;
    }
}

