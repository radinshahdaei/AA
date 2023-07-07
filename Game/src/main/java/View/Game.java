package View;

import Model.Data.Data;
import Model.MovingCircle;
import Model.RotatingCircle;
import Model.GameInfo;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Game extends Application {
    Stage mainStage;

    AnimationTimer animationTimer;
    private boolean paused = false;
    private boolean gameOver = false;

    public ProgressBar freezeProgress = new ProgressBar();
    Circle middleCircle = new Circle();
    public Line windLine;
    VBox pauseMenu;
    VBox endMenu;
    public Pane root = new Pane();
    Scene scene = new Scene(root,500,750);

    public int phase = 1;
    public int xWindSpeed = 0;
    public int isFroze = 0;
    public int addedBalls = 0;
    public int tickedFrames = 0;
    public int tickedRandomFrames = -1;
    public int randomFrames = 0;
    public int rotationDirection = 1;
    public int freezedFrames;

    public static int centerOfRotationX = 250;
    public static int centerOfRotationY = 250;
    public double rotationalSpeed;
    public int numberOfBalls = 0;
    public int initialNumberOfBalls;
    private int windSpeed;
    private String sound = "subwaySurfers";

    private int difficulty;
    private int score = 0;
    private ArrayList<Double> angles = new ArrayList<>();
    public int formOfAngles = 3;

    private Timeline timeline;
    private Label timerLabel;
    private int seconds = 0;

    private Label ballsLeftLabel;

    private Label scoreLabel;

    ArrayList<RotatingCircle> rotatingCircles;
    ArrayList<MovingCircle> movingCircles;

    String sound1 = getClass().getResource("/AudioFiles/metalPipe.mp3").toString();
    Media media1 = new Media(sound1);
    MediaPlayer metalPipe = new MediaPlayer(media1);


    String sound2 = getClass().getResource("/AudioFiles/subwaySurfers.mp3").toString();
    Media media2 = new Media(sound2);
    MediaPlayer subwaySurfers = new MediaPlayer(media2);

    String sound3 = getClass().getResource("/AudioFiles/badPiggies.mp3").toString();
    Media media3 = new Media(sound3);
    MediaPlayer badPiggies = new MediaPlayer(media3);

    String sound4 = getClass().getResource("/AudioFiles/mrbeast.mp3").toString();
    Media media4 = new Media(sound4);
    MediaPlayer mrbeast = new MediaPlayer(media4);

    private void showEndMenu(){
        endMenu = new VBox();
        endMenu.setLayoutX(150);
        endMenu.setLayoutY(350);
        endMenu.setPrefSize(200, 200);
        endMenu.setStyle("-fx-background-color: yellow;");
        BorderStroke borderStroke = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT);
        Border border = new Border(borderStroke);
        endMenu.setBorder(border);
        root.getChildren().add(endMenu);

        Text scoreBoard = new Text();
        scoreBoard.setText("score: "+score);

        Text time = new Text();
        time.setText("time: "+seconds);

        Button restart = new Button("restart");
        restart.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            root.getChildren().clear();
            subwaySurfers.stop();
            badPiggies.stop();
            mrbeast.stop();
            try {
                new Game().start(mainStage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Button quit = new Button("quit");
        quit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Stage stage = Controller.getStage();
            subwaySurfers.stop();
            mrbeast.stop();
            badPiggies.stop();
            try {
                new GameMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        quit.setMinWidth(100);
        restart.setMinWidth(100);

        endMenu.setSpacing(10);
        endMenu.setAlignment(Pos.CENTER);
        endMenu.getChildren().addAll(scoreBoard,time,restart,quit);
    }


    private ArrayList<Double> setAngles(){
        angles.clear();
        if (formOfAngles == 1){
            angles.add(0.0);
            angles.add(1.2566);
            angles.add(2.5132);
            angles.add(3.77);
            angles.add(5.026);
        } else if (formOfAngles == 2){
            angles.add(0.0);
            angles.add(0.6283);
            angles.add(2.5132);
            angles.add(1.571);
            angles.add(5.026);
        } else if (formOfAngles == 3){
            angles.add(0.0);
            angles.add(2.094);
            angles.add(2.792);
            angles.add(3.491);
            angles.add(4.189);
        }
        return angles;
    }

    public void loadGame(){
        GameInfo gameInfo = Data.currentUser.getGameInfo();
        angles = gameInfo.getAngles();
        formOfAngles = gameInfo.getFormOfAngles();
        numberOfBalls = gameInfo.getNumberOfMovingCircles();
        phase = gameInfo.getPhase();
        if (gameInfo.getDifficulty() == 1){
            initialNumberOfBalls = 24;
            rotationalSpeed = 2.5;
            windSpeed = 40;
            freezedFrames = 400;
        } else if (gameInfo.getDifficulty() == 2){
            initialNumberOfBalls = 32;
            rotationalSpeed = 3.5;
            windSpeed = 20;
            freezedFrames = 300;
        } else if (gameInfo.getDifficulty() == 3){
            initialNumberOfBalls = 40;
            rotationalSpeed = 4.5;
            windSpeed = 10;
            freezedFrames = 200;
        }
        difficulty = gameInfo.getDifficulty();
        score = gameInfo.getScore();
        seconds = gameInfo.getTime();

    }

    public void loadGameFromSettings(){
        if (Data.difficulty == 1){
            initialNumberOfBalls = 24;
            rotationalSpeed = 2.5;
            windSpeed = 40;
            freezedFrames = 400;
        }else if (Data.difficulty== 2){
            initialNumberOfBalls = 32;
            rotationalSpeed = 3.5;
            windSpeed = 20;
            freezedFrames = 300;
        } else if (Data.difficulty == 3){
            initialNumberOfBalls = 40;
            rotationalSpeed = 4.5;
            windSpeed = 10;
            freezedFrames = 200;
        }
        difficulty = Data.difficulty;
        formOfAngles = Data.formOfAngles;
        setAngles();

    }


    public void showPauseMenu(){
        pauseMenu = new VBox();
        pauseMenu.setLayoutX(150);
        pauseMenu.setLayoutY(150);
        pauseMenu.setPrefSize(200, 200);
        pauseMenu.setStyle("-fx-background-color: yellow;");
        BorderStroke borderStroke = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT);
        Border border = new Border(borderStroke);
        pauseMenu.setBorder(border);
        root.getChildren().add(pauseMenu);

        Button saveGame = new Button("save game");
        Button quit = new Button("quit");
        Button restart = new Button("restart");
        Button mute = new Button();

        saveGame.setMinWidth(100);
        quit.setMinWidth(100);
        restart.setMinWidth(100);
        mute.setMinWidth(100);

        saveGame.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            saveGame(false);
        });
        quit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Stage stage = Controller.getStage();
            subwaySurfers.stop();
            mrbeast.stop();
            badPiggies.stop();
            try {
                new GameMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        restart.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            root.getChildren().clear();
            subwaySurfers.stop();
            mrbeast.stop();
            badPiggies.stop();
            try {
                new Game().start(mainStage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        if (Data.muted) mute.setText("unmute");
        else mute.setText("mute");
        mute.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Data.muted = !Data.muted;
            if (Data.muted) {
                subwaySurfers.stop();
                badPiggies.stop();
                mrbeast.stop();
            }
            else {
                playSound(sound);
            }
            if (Data.muted) mute.setText("unmute");
            else mute.setText("mute");
        });

        ComboBox<String> musicSelection = new ComboBox<>();
        musicSelection.getItems().addAll(
                "subwaySurfers",
                "mrbeast",
                "badPiggies"
        );
        musicSelection.setValue("subwaySurfers");
        musicSelection.setOnAction(event -> {
            sound = musicSelection.getValue();
            playSound(sound);
        });
        musicSelection.setMinWidth(80);

        pauseMenu.setSpacing(10);
        pauseMenu.setAlignment(Pos.CENTER);
        pauseMenu.getChildren().addAll(saveGame,quit,restart,mute,musicSelection);
    }

    public void removePauseMenu(){
        root.getChildren().remove(pauseMenu);
    }

    public void saveGame(boolean finished){
        GameInfo gameInfo = new GameInfo(rotatingCircles,formOfAngles,numberOfBalls,phase,difficulty,score,seconds);
        gameInfo.setFinished(finished);
        if (Data.currentUser != null) Data.currentUser.setGameInfo(gameInfo);
        else Data.guestUser.setGameInfo(gameInfo);
        Data.saveUsers();
        Data.saveCurrentUser();
    }

    //TODO give arraylist of angles instead of amount in initialize function
    public ArrayList<RotatingCircle> initializeRotatingCircles(Pane root, ArrayList<Double> angles,double rotationalSpeed){
        int amount = angles.size();
        ArrayList<RotatingCircle> rotatingCircles = new ArrayList<>();
        for (int i = 0; i < amount; i++){
            rotatingCircles.add(new RotatingCircle(centerOfRotationX,centerOfRotationY,8,120,rotationalSpeed, angles.get(i), new Circle(),new Line()));
            root.getChildren().addAll(rotatingCircles.get(i).getCircle(),rotatingCircles.get(i).getLine());
            rotatingCircles.get(i).setColor(Color.BLACK);
        }
        return rotatingCircles;
    }

    public ArrayList<RotatingCircle> clearRotatingCircles(Pane root, ArrayList<Double> angles,double rotationalSpeed,ArrayList<RotatingCircle> rotatingCircles){
        setAngles();
        for (RotatingCircle rotatingCircle:rotatingCircles){
            if (root.getChildren().contains(rotatingCircle)) root.getChildren().remove(rotatingCircle);
            rotatingCircle.setVisibility(false);
        }
        rotatingCircles.clear();
        int amount = angles.size();
        for (int i = 0; i < amount; i++){
            rotatingCircles.add(new RotatingCircle(centerOfRotationX,centerOfRotationY,8,120,rotationalSpeed, angles.get(i), new Circle(),new Line()));
            root.getChildren().addAll(rotatingCircles.get(i).getCircle(),rotatingCircles.get(i).getLine());
            rotatingCircles.get(i).setColor(Color.BLACK);
        }
        return rotatingCircles;
    }

    public ArrayList<MovingCircle> initializeMovingCircles(Pane root,int amount,int startingY){
        ArrayList<MovingCircle> movingCircles = new ArrayList<>();
        for (int i = 0; i < amount ; i++){
            MovingCircle movingCircle = new MovingCircle(8,0,0,centerOfRotationX,startingY+i*20*compare(startingY,centerOfRotationY),new Circle(),amount-i,(4-phase)*(initialNumberOfBalls/4));
            root.getChildren().addAll(movingCircle.getCircle(),movingCircle.getText());
            movingCircle.setColor();
            movingCircles.add(movingCircle);
        }
        return movingCircles;
    }

    public ArrayList<MovingCircle> addToMovingCircles(Pane root,int amount,int startingY,ArrayList<MovingCircle> movingCircles){
        for (int i = 0; i < amount ; i++){
            MovingCircle movingCircle = new MovingCircle(8,0,0,centerOfRotationX,startingY+i*20*compare(startingY,centerOfRotationY),new Circle(),amount-i,(4-phase)*(initialNumberOfBalls/4));
            root.getChildren().addAll(movingCircle.getCircle(),movingCircle.getText());
            movingCircle.setColor();
            movingCircles.add(movingCircle);
        }
        return movingCircles;
    }

    private int findDistance(int x1,int y1,int x2,int y2){
        return (int) sqrt(pow((x1-x2),2)+pow((y1-y2),2));
    }

    public static int compare(int a, int b) {
        return Integer.compare(a, b);
    }

    public static void updateRadius(ArrayList<RotatingCircle> rotatingCircles,int tickedFrames){
        int amount = (int) (4 * Math.sin((float)tickedFrames/30));
        for (RotatingCircle rotatingCircle:rotatingCircles){
            rotatingCircle.addToRadius(amount);
        }
    }

    public static void changeColor(ArrayList<RotatingCircle> rotatingCircles,int tickedFrames){
        int answer = (tickedFrames/120)%2;
        if(answer == 0) {
            for (RotatingCircle rotatingCircle:rotatingCircles) rotatingCircle.setVisibility(true);
        } else if (answer == 1) {
            for (RotatingCircle rotatingCircle:rotatingCircles) rotatingCircle.setVisibility(false);
        }
    }


    private void setRandomFrames(){
        if (tickedRandomFrames == 0){
            Random random = new Random();
            randomFrames = random.nextInt(101) + 200;
        }
        if (tickedRandomFrames == randomFrames) {
            rotationDirection *= -1;
            tickedRandomFrames = 0;
        }
    }

    public void showWindLine(Line windLine,int tickedFrames,int windSpeed){
        xWindSpeed = (int) (10*Math.sin((float) tickedFrames/windSpeed));
        windLine.setEndX(windLine.getStartX()+xWindSpeed);
    }

    private void handleSpaceKeyPressed(KeyCode keyCode,ArrayList<MovingCircle> movingCircles) {
        if (keyCode.equals(KeyCode.SPACE) && !gameOver){
            if (addedBalls < movingCircles.size()) {
                movingCircles.get(addedBalls).setSpeedX(xWindSpeed);
                movingCircles.get(addedBalls).setSpeedY(-1000);
                addedBalls++;
                for (int i = addedBalls; i < movingCircles.size(); i++){
                    movingCircles.get(i).addStartingY(-20);
                }
            }
        }
    }

    private void handleTabKeyPressed(KeyCode keyCode,ArrayList<RotatingCircle> rotatingCircles){
        if (keyCode.equals(KeyCode.TAB) && !gameOver) {
            if (Math.abs(freezeProgress.getProgress()-1)>0.1) return;
            isFroze = freezedFrames;
            rotationalSpeed = RotatingCircle.maxRotatingSpeed * 1/3;
            for (RotatingCircle rotatingCircle:rotatingCircles){
                rotatingCircle.setRotationalSpeed(rotationalSpeed);
            }
            freezeProgress.setProgress(0);
        }
    }

    private void handleLeftKeyPressed(KeyCode keyCode,ArrayList<MovingCircle> movingCircles){
        if (keyCode.equals(KeyCode.LEFT) && phase == 4 && !gameOver){
            for (int i = addedBalls; i < movingCircles.size(); i++){
                movingCircles.get(i).addStartingX(-5);
            }
            windLine.setStartX(movingCircles.get(0).getStartingX());
        }
    }

    private void handleRightKeyPressed(KeyCode keyCode,ArrayList<MovingCircle> movingCircles){
        if (keyCode.equals(KeyCode.RIGHT) && phase == 4 && !gameOver){
            for (int i = addedBalls; i < movingCircles.size(); i++){
                movingCircles.get(i).addStartingX(5);
            }
            windLine.setStartX(movingCircles.get(0).getStartingX());
        }
    }

    private void updateTimerLabel() {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, remainingSeconds));
    }

    public void updateBallsLeftLabel(){
        ballsLeftLabel.setText(String.format("balls left: %02d",numberOfBalls));
        if (phase == 1) {
            ballsLeftLabel.setStyle("-fx-background-color: red");
        } else if (phase == 2) {
            ballsLeftLabel.setStyle("-fx-background-color: orange");
        } else if (phase == 3) {
            ballsLeftLabel.setStyle("-fx-background-color: yellow");
        } else if (phase == 4) {
            ballsLeftLabel.setStyle("-fx-background-color: green");
        }
    }

    public void updateScoreLabel(){
        scoreLabel.setText("score: "+score);
    }

    public boolean updatePhase(){
        boolean result = false;
        int x0 = initialNumberOfBalls;
        int x1 = initialNumberOfBalls * 3 / 4;
        int x2 = initialNumberOfBalls * 2 / 4;
        int x3 = initialNumberOfBalls * 1 /4;
        if (numberOfBalls == x1){
            phase = 2;
            result = true;
        } else if (numberOfBalls == x2){
            phase = 3;
            result = true;
        } else if (numberOfBalls == x3){
            phase = 4;
            result = true;
        }
        return result;
    }


    public void playSound(String sound){

        if (Data.muted) return;

        if (sound.equals("metalPipe")){
            metalPipe.stop();
            metalPipe.play();
        } else if (sound.equals("subwaySurfers")) {
            mrbeast.stop();
            badPiggies.stop();
            subwaySurfers.play();
        } else if (sound.equals("mrbeast")){
            mrbeast.play();
            badPiggies.stop();
            subwaySurfers.stop();
        } else if (sound.equals("badPiggies")){
            mrbeast.stop();
            badPiggies.play();
            subwaySurfers.stop();
        }
    }



//    public static void main(String[] args) {
//        launch(args);
//    }

    @Override
    public void start(Stage stageNo) throws Exception {
        Stage stage = null;
        if (Controller.getStage() == null) {
            stage = stageNo;
        } else {
            stage = Controller.getStage();
        }
        stage.setScene(scene);
        mainStage = stage;

        metalPipe.setVolume(0.3);

        if (Data.newGame || !Data.newGame && Data.currentUser.getGameInfo().isFinished()) loadGameFromSettings();
        else loadGame();

        if (numberOfBalls == 0) numberOfBalls = initialNumberOfBalls;
        RotatingCircle.setMaxRotatingSpeed(rotationalSpeed);

        rotatingCircles = initializeRotatingCircles(root,angles,rotationalSpeed);
        int number = numberOfBalls%(initialNumberOfBalls/4);
        if (number == 0) number = initialNumberOfBalls/4;
        movingCircles = initializeMovingCircles(root,number,centerOfRotationY+200);

        freezeProgress.setProgress(0);
        freezeProgress.setLayoutX(10);
        freezeProgress.setLayoutY(10);
        root.getChildren().add(freezeProgress);


        middleCircle.setCenterX(centerOfRotationX);
        middleCircle.setCenterY(centerOfRotationY);
        middleCircle.setRadius(50);
        middleCircle.setFill(Color.BLACK);
        root.getChildren().add(middleCircle);

        scoreLabel = new Label("score: 0");
        root.getChildren().add(scoreLabel);
        scoreLabel.setStyle("-fx-background-color: beige");
        scoreLabel.layoutXProperty().bind(root.widthProperty().subtract(scoreLabel.widthProperty()).subtract(10));
        scoreLabel.layoutYProperty().bind(root.heightProperty().subtract(scoreLabel.heightProperty()).subtract(30));

        ballsLeftLabel = new Label("balls left: 00");
        root.getChildren().add(ballsLeftLabel);
        ballsLeftLabel.layoutXProperty().bind(root.widthProperty().subtract(ballsLeftLabel.widthProperty()).subtract(10));
        ballsLeftLabel.layoutYProperty().bind(root.heightProperty().subtract(ballsLeftLabel.heightProperty()).subtract(10));

        timerLabel = new Label("00:00");
        root.getChildren().add(timerLabel);
        timerLabel.setLayoutX(10);
        timerLabel.layoutYProperty().bind(root.heightProperty().subtract(timerLabel.heightProperty()).subtract(10));

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            seconds++;
            updateTimerLabel();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        subwaySurfers.setCycleCount(MediaPlayer.INDEFINITE);
        mrbeast.setCycleCount(MediaPlayer.INDEFINITE);
        badPiggies.setCycleCount(MediaPlayer.INDEFINITE);
        playSound(sound);

        root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode keyCode = event.getCode();
            if (!paused){
                handleSpaceKeyPressed(keyCode,movingCircles);
                handleTabKeyPressed(keyCode,rotatingCircles);
                handleLeftKeyPressed(keyCode,movingCircles);
                handleRightKeyPressed(keyCode,movingCircles);
            }
        });

        Button pauseButton = new Button("Pause");
        pauseButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            paused = !paused;
            if (paused && !gameOver) {
                animationTimer.stop();
                timeline.stop();
                pauseButton.setText("Resume");
                showPauseMenu();
            } else if (!paused && !gameOver){
                animationTimer.start();
                timeline.play();
                pauseButton.setText("Pause");
                removePauseMenu();
            }
        });
        root.getChildren().add(pauseButton);
        pauseButton.layoutXProperty().bind(root.widthProperty().subtract(pauseButton.widthProperty()).subtract(10));
        pauseButton.setLayoutY(10);


        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long currentTime) {
                try {
                    if (!showCircles(rotatingCircles,movingCircles)) {
                        saveGame(true);
                        animationTimer.stop();
                        timeline.stop();
                        gameOver = true;
                        if (numberOfBalls != 0) root.setStyle("-fx-background-color: #8B0000;");
                        if (numberOfBalls == 0) root.setStyle("-fx-background-color: #00ff00;");
                        for (RotatingCircle rotatingCircle:rotatingCircles){
                            rotatingCircle.setVisibility(true);
                        }
                        for (MovingCircle movingCircle:movingCircles){
                            movingCircle.setVisibility(false);
                        }
                        if (phase == 4) windLine.setVisible(false);

                        endAnimation.start();

                        Data.saveUsers();
                        Data.saveCurrentUser();

                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        animationTimer.start();
        stage.show();
    }

    AnimationTimer endAnimation = new AnimationTimer() {
        @Override
        public void handle(long l) {
            try {
                if (!showEndAnimation(rotatingCircles,middleCircle)) {
                    endAnimation.stop();
                    showEndMenu();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    };

    int timer = 25;
    public boolean showEndAnimation(ArrayList<RotatingCircle> rotatingCircles,Circle middleCircle) throws InterruptedException {
        if (timer > 15){
            timer--;
            Thread.sleep(10);
            return true;
        }
        else {
            middleCircle.setRadius(middleCircle.getRadius() + 2);
            for (RotatingCircle rotatingCircle:rotatingCircles){
                rotatingCircle.setRotationalSpeed(0);
                rotatingCircle.getCircle().setRadius(15);
                rotatingCircle.getLine().setStrokeWidth(5);
                rotatingCircle.setRotatingRadius(rotatingCircle.getRotatingRadius()+30);
                rotatingCircle.getCircle().setCenterX(rotatingCircle.findXCoordinates());
                rotatingCircle.getCircle().setCenterY(rotatingCircle.findYCoordinates());
                rotatingCircle.getLine().setEndX(rotatingCircle.findXCoordinates());
                rotatingCircle.getLine().setEndY(rotatingCircle.findYCoordinates());
            }

            timer--;
            Thread.sleep(10);
            return timer != 0;
        }
    }


    public boolean showCircles(ArrayList<RotatingCircle> rotatingCircles,ArrayList<MovingCircle> movingCircles) throws InterruptedException {
        updateBallsLeftLabel();
        updateScoreLabel();

        // handling freeze progress bar
        if (isFroze == 1){
            rotationalSpeed = rotatingCircles.get(0).getMaxRotatingSpeed();
            for (RotatingCircle rotatingCircle:rotatingCircles){
                rotatingCircle.setRotationalSpeed(rotationalSpeed);
            }
        } else if (isFroze > 0) isFroze--;

        if (phase >= 2){
            setRandomFrames();
            updateRadius(rotatingCircles,tickedFrames);
        }

        if (phase >= 3){
            changeColor(rotatingCircles,tickedFrames);
        }

        if (phase >= 4){
            showWindLine(windLine,tickedFrames,windSpeed);
        }

        for (RotatingCircle rotatingCircle: rotatingCircles){
            updateRotatingCircles(rotatingCircle,0.01,rotationDirection);
        }
        for (MovingCircle movingCircle:movingCircles){
            updateMovingCircles(movingCircle,0.01);
        }

        if (!checkIntersection(rotatingCircles,movingCircles)) return false;


        Thread.sleep(10);
        if (phase >= 2) tickedRandomFrames++;
        tickedFrames++;

        return true;
    }
    public void updateRotatingCircles(RotatingCircle rotatingCircle,double time,int direction){
        rotatingCircle.getCircle().setCenterX(rotatingCircle.findXCoordinates());
        rotatingCircle.getCircle().setCenterY(rotatingCircle.findYCoordinates());
        rotatingCircle.getLine().setEndX(rotatingCircle.findXCoordinates());
        rotatingCircle.getLine().setEndY(rotatingCircle.findYCoordinates());
        rotatingCircle.updateAngle(time,direction);

    }

    public void updateMovingCircles(MovingCircle movingCircle,double time){
        movingCircle.updateCoordinates(time);
        movingCircle.getCircle().setCenterX(movingCircle.getStartingX());
        movingCircle.getCircle().setCenterY(movingCircle.getStartingY());
        movingCircle.getText().setX(movingCircle.getStartingX()- movingCircle.getText().getLayoutBounds().getWidth() / 2);
        movingCircle.getText().setY(movingCircle.getStartingY()+ movingCircle.getText().getLayoutBounds().getHeight() / 4);
    }

    public boolean checkIntersection(ArrayList<RotatingCircle> rotatingCircles,ArrayList<MovingCircle> movingCircles) throws InterruptedException {
        for (RotatingCircle rotatingCircle1:rotatingCircles){
            for (RotatingCircle rotatingCircle2:rotatingCircles){
                if (rotatingCircle1.equals(rotatingCircle2)) continue;
                int x2 = rotatingCircle1.findXCoordinates();
                int y2 = rotatingCircle1.findYCoordinates();
                int x3 = rotatingCircle2.findXCoordinates();
                int y3 = rotatingCircle2.findYCoordinates();
                if (findDistance(x3,y3,x2,y2)!=0 && findDistance(x3,y3,x2,y2) < 2*rotatingCircles.get(0).getCircle().getRadius()) {
                    return false;
                }
            }
        }

        for (int i = 0; i < movingCircles.size();i++){
            MovingCircle movingCircle = movingCircles.get(i);
            int x1 = (int) movingCircle.getStartingX();
            int y1 = (int) movingCircle.getStartingY();
            if (findDistance(x1,y1,centerOfRotationX,centerOfRotationY) > 120) continue;

            for (RotatingCircle rotatingCircle:rotatingCircles){
                int x2 = rotatingCircle.findXCoordinates();
                int y2 = rotatingCircle.findYCoordinates();
                if (findDistance(x1,y1,x2,y2) < rotatingCircle.getCircleRadius()+rotatingCircle.getCircle().getRadius()) {
                    return false;

                }
            }

            root.getChildren().remove(movingCircle.getCircle());
            root.getChildren().remove(movingCircle.getText());
            movingCircles.remove(movingCircle);
            numberOfBalls--;
            addedBalls--;
            i--;

            score += phase;

            playSound("metalPipe");

            int deltaY = y1 - centerOfRotationY;
            int deltaX = x1 - centerOfRotationX;
            double angle = Math.atan2(deltaY,deltaX);
            RotatingCircle rotatingCircle = new RotatingCircle(centerOfRotationX,centerOfRotationY,8,120,rotationalSpeed, angle,new Circle(),new Line());
            root.getChildren().addAll(rotatingCircle.getCircle(),rotatingCircle.getLine());
            rotatingCircles.add(rotatingCircle);
            rotatingCircle.setColor(Color.BLACK);

            if (Math.abs(freezeProgress.getProgress()-1)>0.1) freezeProgress.setProgress(freezeProgress.getProgress()+0.1999);

            if (updatePhase()){
                movingCircles = addToMovingCircles(root,initialNumberOfBalls/4,centerOfRotationY+200,movingCircles);
                rotatingCircles = clearRotatingCircles(root,angles,rotationalSpeed,rotatingCircles);
                if (phase == 4){
                    windLine = new Line();
                    windLine.setStartX(centerOfRotationX);
                    windLine.setStartY(centerOfRotationY+200);
                    windLine.setOpacity(0.5);
                    windLine.setEndX(centerOfRotationX);
                    windLine.setEndY(centerOfRotationY + 150);
                    root.getChildren().add(windLine);
                }
            }

            if (numberOfBalls == 0){
                windLine.setVisible(false);
                return false;
            }
        }
        return true;
    }
}
