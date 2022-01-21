package application;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public abstract class PowerUp extends MapObject {

    private Circle shape;
    private Image sprite;
    private ImageView spriteView;
    private double r;

    public PowerUp(double x, double y, int r, String imagePath) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.shape = new Circle(x, y, r);
        this.sprite = new Image(imagePath, r, r, true, false);
        this.spriteView = new ImageView(this.sprite);
        this.spriteView.setX(x - r);
        this.spriteView.setY(y - r);
        this.spriteView.setFitHeight(r * 2);
        this.spriteView.setFitWidth(r * 2);

    }

    public abstract void onUse(Player player);


    public void setX(double x) {
        this.x = x;
        spriteView.setX(x - r);
        shape.setCenterX(x);
    }

    public void setY(double y) {
        this.y = y;
        spriteView.setY(y - r);
        shape.setCenterY(y);
    }

    @Override
    public boolean collision(double x, double y) {
        return shape.contains(x, y);
    }

    @Override
    public boolean collision(Bounds localBounds) {
        if(shape.intersects(localBounds)) setY(localBounds.getMinY() - r);
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
    ImageView getSprite() {
        return spriteView;
    }
}
