package application;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public abstract class PowerUp extends StaticEntity {

    private Circle shape;
    private Image sprite;
    private ImageView spriteView;
    private String type;

    public PowerUp(double x, double y, int r, String imagePath) {
        this.shape = new Circle(x, y, r);
        this.sprite = new Image(imagePath, r, r, true, false);
        this.spriteView = new ImageView(this.sprite);

    }

    public abstract void onCollision(GameObject gameObject);

    @Override
    public boolean collision(double x, double y) {
        return shape.contains(x, y);
    }

    @Override
    public boolean collision(Bounds localBounds) {
        return shape.intersects(localBounds);
    }

    @Override
    public Node getShape() {
        return shape;
    }

    @Override
    Node[] getSprites() {
        return new Node[0];
    }

    @Override
    Node getSprite() {
        return spriteView;
    }
}
