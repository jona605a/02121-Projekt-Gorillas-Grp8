package application;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;

public class Player {
    private int hitPoints;
    private String name;
    private double posX, posY;
    private ArrayList<Castable> castables = new ArrayList<Castable>();
    private Circle hitBox = new Circle();

    Player(int hp, String name, double x, double y){
        hitPoints = hp;
        this.name = name;
        posX = x;
        posY = y;
        castables.add(new Banana(x, y));
        hitBox.setRadius(10);
        hitBox.setCenterX(x - 10);
        hitBox.setCenterY(y - 10);


    }

    public Circle getHitBox() {
        return hitBox;
    }
}
