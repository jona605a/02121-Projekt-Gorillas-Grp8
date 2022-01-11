package application;

import javafx.scene.shape.Circle;

public class Castable {
    protected double x, y;
    protected double velocityX, velocityY;
    protected Circle circle;


    Castable(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        circle.setCenterX(x);
        this.x = x;
    }


    public void setY(double y) {
        circle.setCenterY(y);
        this.y = y;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityX(double vx){
        velocityX = vx;
    }

    public void setVelocityY(double vy){
        velocityY = vy;
    }

    public Circle getCircle() {
        return circle;
    }
}
