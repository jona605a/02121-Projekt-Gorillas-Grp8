package application;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import javax.swing.text.html.ImageView;
import java.util.ArrayList;

public class Player {
    private int hitPoints;
    private String name;
    private double posX, posY;
    private ArrayList<Castable> castables = new ArrayList<Castable>();
    private Circle hitBox = new Circle();
    private Castable selectedCastable;
    private ImageView sprite;
    private Image castingSprite;

    Player(int hp, String name, double x, double y){
        hitPoints = hp;
        this.name = name;
        posX = x;
        posY = y;
        castables.add(new Banana(x, y));
        hitBox.setRadius(10);
        hitBox.setCenterX(x);
        hitBox.setCenterY(y);
        selectedCastable = new Banana(posX + 15, posY - 15);

    }

    public boolean collision(double x, double y){
        return hitBox.contains(x, y);
    }

    boolean collision(Bounds localBounds) {
        return hitBox.intersects(localBounds);
    }


    public Circle getHitBox() {
        return hitBox;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public Castable getSelectedCastable() {
        return selectedCastable;
    }
}
