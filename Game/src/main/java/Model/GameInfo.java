package Model;

import Model.Data.Data;

import java.util.ArrayList;

public class GameInfo {
    private ArrayList<Double> angles = new ArrayList<>();
    private int formOfAngles;
    private int numberOfMovingCircles;
    private int phase;
    private int difficulty;
    private int score;
    private int time;
    private boolean isFinished;

    public GameInfo(ArrayList<RotatingCircle> rotatingCircles,int formOfAngles, int numberOfMovingCircles, int phase,int difficulty,int score,int time) {
        this.angles = getAngles(rotatingCircles);
        this.formOfAngles = formOfAngles;
        this.numberOfMovingCircles = numberOfMovingCircles;
        this.phase = phase;
        this.difficulty = difficulty;
        this.score = score;
        this.time = time;
        Data.saveUsers();
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public ArrayList<Double> getAngles() {
        return angles;
    }

    public int getFormOfAngles() {
        return formOfAngles;
    }

    public int getNumberOfMovingCircles() {
        return numberOfMovingCircles;
    }

    public int getPhase() {
        return phase;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getScore() {
        return score;
    }

    public int getTime() {
        return time;
    }

    private ArrayList<Double> getAngles(ArrayList<RotatingCircle> rotatingCircles){
        ArrayList<Double> angles = new ArrayList<>();
        for (RotatingCircle rotatingCircle:rotatingCircles){
            angles.add(rotatingCircle.getAngle());
        }
        return angles;
    }

}
