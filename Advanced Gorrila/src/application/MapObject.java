package application;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

public abstract class MapObject {

    protected double x, y, width, height;
    protected double shapeX, shapeY;

    abstract boolean collision(double x, double y);

    abstract boolean collision(Bounds localBounds);

    abstract Node getShape();

    abstract Node[] getSprites();

    abstract ImageView getSprite();

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }



    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
