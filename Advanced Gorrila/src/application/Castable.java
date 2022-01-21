package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class Castable {
    protected double x, y;
    protected double velocityX, velocityY;
    double damage;
    protected Circle hitBox;
    protected double weight;
    protected ImageView spriteView;
    protected Image coconut = new Image(ClassLoader.getSystemResource("Images/Coconut.png").toString(), 20, 20, true, false);
    protected Image currentImage;


    Castable(double x, double y){
        this.x = x;
        this.y = y;
        spriteView = new ImageView();
        spriteView.setPreserveRatio(true);
    }

    public void setX(double x) {
        hitBox.setCenterX(x);
        spriteView.setX(x - currentImage.getWidth()/2);
        this.x = x;
    }

    public void setY(double y) {
        hitBox.setCenterY(y);
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

    public Circle getHitBox() {
        return hitBox;
    }

    public double getDamage() {
        return damage;
    }

    public double getWeight() {
        return weight;
    }

    public void setVelocityX(double vx){
        velocityX = vx;
    }

    public void setVelocityY(double vy){
        velocityY = vy;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setDamage(double newDamage) {
        this.damage = newDamage;
    }
}
