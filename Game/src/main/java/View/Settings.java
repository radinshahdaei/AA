package View;

import Model.Data.Data;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Settings extends Application {
    Label settings = new Label();
    @Override
    public void start(Stage stage) throws Exception {
        Pane root = new Pane();
        Scene scene = new Scene(root,300,300);
        stage.setScene(scene);
        stage.show();

        Data.muted = false;

        settings.setLayoutX(10);
        settings.setLayoutY(100);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(
                "Easy",
                "Medium",
                "Hard"
        );
        comboBox.setValue("Easy");
        comboBox.setOnAction(event -> {
            String selectedOption = comboBox.getValue();
            showSettings(selectedOption);
        });
        comboBox.setLayoutY(10);
        comboBox.setLayoutX(10);

        ComboBox<String> layout = new ComboBox<>();
        layout.getItems().addAll(
                "1",
                "2",
                "3"
        );
        layout.setValue("1");
        layout.setOnAction(event -> {
            String selectedOption = layout.getValue();
            showLayout(selectedOption);
        });
        layout.setLayoutY(40);
        layout.setLayoutX(10);

        Button back = new Button("back");
        back.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                new GameMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        back.layoutXProperty().bind(root.widthProperty().subtract(back.widthProperty()).subtract(10));
        back.setLayoutY(10);

        Button mute = new Button("mute");
        mute.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Data.muted = !Data.muted;
            if (Data.muted) mute.setText("unmute");
            else if (!Data.muted) mute.setText("mute");
        });
        mute.layoutXProperty().bind(root.widthProperty().subtract(mute.widthProperty()).subtract(10));
        mute.layoutYProperty().bind(root.heightProperty().subtract(mute.heightProperty()).subtract(10));

        root.getChildren().add(comboBox);
        root.getChildren().add(back);
        root.getChildren().add(layout);
        root.getChildren().add(mute);
        root.getChildren().add(settings);
    }

    public void showSettings(String selectedOption){
        if (selectedOption.equals("Easy")){
            settings.setText("Rotation speed: 5\t Wind speed: 1.2\nFreeze timer: 7\t Number of Balls: 24");
            Data.difficulty = 1;
        } else if (selectedOption.equals("Medium")){
            settings.setText("Rotation speed: 10\t Wind speed: 1.5\nFreeze timer: 5\t Number of Balls: 32");
            Data.difficulty = 2;
        } else if (selectedOption.equals("Hard")){
            settings.setText("Rotation speed: 15\t Wind speed: 1.8\nFreeze timer: 3\t Number of Balls: 40");
            Data.difficulty = 3;
        }
    }

    public void showLayout(String selectedOption){
        Data.formOfAngles = Integer.parseInt(selectedOption);
    }
}
