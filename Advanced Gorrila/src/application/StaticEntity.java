package application;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

public abstract class StaticEntity{

    double x, y, width, height;

    abstract boolean collision(double x, double y);

    abstract boolean collision(Bounds localBounds);

    abstract Node getShape();

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
