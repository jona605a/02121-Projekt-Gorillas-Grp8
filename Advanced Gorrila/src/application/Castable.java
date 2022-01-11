package application;

public class Castable {
    protected double layoutX, layoutY;
    protected double velocityX, velocityY;


    Castable(double x, double y){
        layoutX = x;
        layoutY = y;
    }


    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocity(double vx){
        velocityX = vx;
    }

    public void setLayoutY(double vy){
        velocityY = vy;
    }

}
