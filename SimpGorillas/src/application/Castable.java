package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class Castable {
    protected double x, y;
    protected double velocityX, velocityY;
    protected Circle circle;
    private int damage;
    private double weight;
    private ImageView spriteView;
    public Image banana = new Image("/Images/Banan.png", 20, 20, true, false);
    public Image coconut = new Image("/Images/Coconut.png", 20, 20, true, false);
    private Image currentImage;


    Castable(double x, double y, int damage, double weight){
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.weight = weight;
        spriteView = new ImageView();
        spriteView.setPreserveRatio(true);
    }

    public void setX(double x) {
        circle.setCenterX(x);
        spriteView.setX(x - currentImage.getWidth()/2);
        this.x = x;
    }

    public void setY(double y) {
        circle.setCenterY(y);
        spriteView.setY(y - currentImage.getHeight()/2);
        this.y = y;
    }

    public void setSprite(Image image) {
        spriteView.setImage(image);
        currentImage = image;
        spriteView.setX(x - image.getWidth()/2);
        spriteView.setY(y - image.getHeight()/2);
        //System.out.println(image.getHeight());
    }

    double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public ImageView getSpriteView() {
        return spriteView;
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
    
    public int getDamage() {
    	return damage;
    }
    
    public double getWeight() {
    	return weight;
    }

    public void setDamage(int newDamage) {
        this.damage = newDamage;
    }
}