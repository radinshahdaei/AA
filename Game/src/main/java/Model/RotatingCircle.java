package Model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class RotatingCircle {
    public static double maxRotatingSpeed;
    private int rotatingX;
    private int rotatingY;
    private int circleRadius;
    private int rotatingRadius;
    private double rotationalSpeed;
    private double angle;
    private Circle circle;
    private Line line;




    public static void setMaxRotatingSpeed(double maxRotatingSpeed) {
        RotatingCircle.maxRotatingSpeed = maxRotatingSpeed;
    }

    public RotatingCircle(int rotatingX, int rotatingY, int circleRadius, int rotatingRadius, double rotationalSpeed, double angle, Circle circle, Line line) {
        this.rotatingX = rotatingX;
        this.rotatingY = rotatingY;
        this.circleRadius = circleRadius;
        this.rotatingRadius = rotatingRadius;
        this.rotationalSpeed = rotationalSpeed;
        this.angle = angle;
        this.circle = circle;
        this.line = line;
        this.getCircle().setRadius(circleRadius);
        this.getLine().setStartX(250);
        this.getLine().setStartY(250);
        this.getCircle().setCenterX(findXCoordinates());
        this.getCircle().setCenterY(findYCoordinates());
        this.getLine().setEndX(findXCoordinates());
        this.getLine().setEndY(findYCoordinates());
        this.getLine().setStrokeWidth(2);

    }

    public void setColor(Color color){
        this.getCircle().setFill(color);
        this.getLine().setFill(color);
    }

    public void setVisibility(boolean set){
        this.getCircle().setVisible(set);
        this.getLine().setVisible(set);
    }


    public void setRotatingRadius(int rotatingRadius) {
        this.rotatingRadius = rotatingRadius;
    }

    public int findXCoordinates(){

        return (int) (rotatingX + Math.cos(angle)*rotatingRadius);

    }

    public int findYCoordinates(){
        return (int) (rotatingY + Math.sin(angle)*rotatingRadius);
    }

    public void updateAngle(double time,int direction){
        if (direction > 0)angle+=rotationalSpeed * time;
        else if (direction < 0) angle-=rotationalSpeed * time;
    }

    public int getRotatingX() {
        return rotatingX;
    }

    public int getRotatingY() {
        return rotatingY;
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    public int getRotatingRadius() {
        return rotatingRadius;
    }

    public double getRotationalSpeed() {
        return rotationalSpeed;
    }

    public double getAngle() {
        return angle;
    }

    public Circle getCircle() {
        return circle;
    }

    public Line getLine() {
        return line;
    }

    public double getMaxRotatingSpeed() {
        return maxRotatingSpeed;
    }

    public void setRotationalSpeed(double rotationalSpeed) {
        this.rotationalSpeed = rotationalSpeed;
    }

    public void addToRadius(int amount){
        this.getCircle().setRadius(circleRadius + amount);
    }
}
