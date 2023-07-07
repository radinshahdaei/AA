package Model;

import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class MovingCircle {
    private int radius;
    private double speedX;
    private double speedY;
    private double startingX;
    private double startingY;
    private Circle circle;
    private Text text;


    public MovingCircle(int radius, double speedX, double speedY, double startingX, double startingY, Circle circle,int number,int number2) {
        this.radius = radius;
        this.speedX = speedX;
        this.speedY = speedY;
        this.startingX = startingX;
        this.startingY = startingY;
        this.circle = circle;

        this.circle.setRadius(radius);
        this.text = new Text(String.valueOf(number+number2));
    }

    public void setVisibility(boolean set){
        this.getCircle().setVisible(set);
        this.getText().setVisible(set);
    }

    public void setColor(){
        this.circle.setFill(Color.BLACK);
        this.text.setFill(Color.WHITE);
    }

    public int getRadius() {
        return radius;
    }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public double getStartingX() {
        return startingX;
    }

    public double getStartingY() {
        return startingY;
    }

    public Circle getCircle() {
        return circle;
    }

    public void updateCoordinates(double time){
        startingX += speedX * time;
        startingY += speedY * time;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public void setStartingX(double startingX) {
        this.startingX = startingX;
    }

    public void setStartingY(double startingY) {
        this.startingY = startingY;
    }

    public void addStartingY(double amount){
        this.startingY += amount;
    }

    public void addStartingX(double amount){
        this.startingX += amount;
    }

    public Text getText() {
        return text;
    }
}
