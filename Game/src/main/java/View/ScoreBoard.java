package View;

import Model.Data.Data;
import Model.User;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ScoreBoard extends Application {
    Stage mainStage;
    VBox scoreBox = new VBox();
    @Override
    public void start(Stage stage) throws Exception {
        Pane root = new Pane();
        Scene scene = new Scene(root,300,300);
        stage.setScene(scene);
        stage.show();

        scoreBox.setLayoutX(10);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(
                "Easy",
                "Medium",
                "Hard"
        );
        comboBox.setValue("Easy");
        comboBox.setOnAction(event -> {
            String selectedOption = comboBox.getValue();
            showScoreBoard(selectedOption,root);
        });
        comboBox.setLayoutY(10);
        comboBox.setLayoutX(10);

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

        root.getChildren().add(comboBox);
        root.getChildren().add(back);


    }

    public void showScoreBoard(String selectedOption,Pane root){
        root.getChildren().remove(scoreBox);
        scoreBox.getChildren().clear();
        scoreBox.setAlignment(Pos.CENTER);
        scoreBox.setSpacing(5);
        ArrayList<User> sortedUsers;
        if (selectedOption.equals("Easy")) sortedUsers = Data.sortEasyUsers();
        else if (selectedOption.equals("Medium")) sortedUsers = Data.sortMediumUsers();
        else sortedUsers = Data.sortHardUsers();
        int number = Math.min(10,sortedUsers.size());
        for (int i = 0; i < number;i++){
            int rank = i+1;
            String username = sortedUsers.get(i).getUsername();
            int highScore;
            int time;
            if (selectedOption.equals("Easy")) {
                highScore = -sortedUsers.get(i).getEasyHighScore();
                time = sortedUsers.get(i).getEasyTime();
            }
            else if (selectedOption.equals("Medium")) {
                highScore = -sortedUsers.get(i).getMediumHighScore();
                time = sortedUsers.get(i).getMediumTime();
            }
            else {
                highScore = -sortedUsers.get(i).getHardHighScore();
                time = sortedUsers.get(i).getHardTime();
            }
            Text text = new Text();
            text.setText(rank+") "+username+", High Score: "+highScore+", time: "+time);
            scoreBox.getChildren().add(text);
            if (i == 0){
                text.setFill(Color.GOLD);
            } else if (i == 1){
                text.setFill(Color.SILVER);
            } else if (i == 2){
                text.setFill(Color.BROWN);
            }
        }
        scoreBox.setLayoutY(100);
        root.getChildren().add(scoreBox);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
