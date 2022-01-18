package application;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Platform extends StaticEntity {
	
	private Rectangle shape;
	private Image sprite;
	private ImageView spriteView;
	
	public Platform(double x, double y, double width) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = 20;
		sprite = new Image("/Images/Platform.png", this.width, this.height, true, false);
		spriteView = new ImageView(sprite);
		shape = new Rectangle(this.x, this.y, this.width, this.height);
	}

	@Override
	boolean collision(double x, double y) {
		return shape.contains(x, y);
	}

	@Override
	boolean collision(Bounds localBounds) {
		return shape.intersects(localBounds);
	}

	@Override
	Node getShape() {
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
