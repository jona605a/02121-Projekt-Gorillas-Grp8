package application;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public abstract class PowerUp extends MapObject {

    private Circle shape;
    private Image sprite;
    private ImageView spriteView;

    public PowerUp(double x, double y, int r, String imagePath) {
        this.x = x;
        this.y = y;
        this.shape = new Circle(x, y, r);
        this.sprite = new Image(imagePath, r, r, true, false);
        this.spriteView = new ImageView(this.sprite);
        this.spriteView.setX(x);
        this.spriteView.setY(y);
        this.spriteView.setFitHeight(r);
        this.spriteView.setFitWidth(r);

    }

    public abstract void onCollision(GameObject gameObject);

    public void delete(GameObject gameObject) {
        // Removing the power up from the game
        gameObject.getLevel().getGame().getChildren().remove(this.getSprite());
        ArrayList<PowerUp> powerUps = gameObject.getLevel().getPowerUps();
        powerUps.remove(this);
        gameObject.getLevel().setPowerUps(powerUps);
    }

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
        return null;
    }

    @Override
    Node getSprite() {
        return spriteView;
    }
}
