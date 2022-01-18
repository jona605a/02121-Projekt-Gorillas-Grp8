package application;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.util.Random;

public class PowerUp extends StaticEntity {
	
	private Circle shape;
	private Image sprite;
	private ImageView spriteView;
	
	
	PowerUp(double x, double y, double r) {
		shape = new Circle(x, y, r);
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
		return new Node[0];
	}

	@Override
	Node getSprite() {
		return spriteView;
	}

	public void activatePowerUp(Castable castable, Player player, GameObject gameObject) {
		int powerUp = randomNumber(6);
		
		switch(powerUp) { 
			case 0:
				fastBullet(castable);
				break;
			case 1:
				slowBullet(castable);
				break;
			case 2:
				powerfulBullet(castable);
				break;
			case 3:
				secondTurn(gameObject);
				 break;
			case 4:
				extraAmmo(player);
				break;
			case 5:
				moreHP(player);
				break;
		}
		
		deletePowerUp();
	}
	
	private void fastBullet(Castable castable) {
		// make castable faster
		castable.setVelocityX(castable.getVelocityX() * 2);
		castable.setVelocityY(castable.getVelocityY() * 2);
		
	}
	
	private void slowBullet(Castable castable) {
		// make castable slower
		castable.setVelocityX(castable.getVelocityX() * .5);
		castable.setVelocityY(castable.getVelocityY() * .5);
	}
	
	private void powerfulBullet(Castable castable) {
		// make castable deal twice as much damage
		castable.setDamage(castable.getDamage()*2);
		
	}
	
	private void secondTurn(GameObject gameObject) {
		// give the current player another turn
		// changes the turn to the other player so that when the current turn ends the current player will have another turn
		gameObject.setPlayer1Turn(!gameObject.isPlayer1Turn());
	}
	
	private void extraAmmo(Player player) {
		// gives the current player extra special ammo
		player.addCastable(new Coconut(player.getPosX(), player.getPosY()));
	}
	
	private void moreHP(Player player) {
		// gives the current player a HP boost
		player.addHitPoints(20);
	}
	
	private void deletePowerUp() {
		// remove powerup
	}
	
	private int randomNumber(int max) {
		Random random = new Random();
		return random.nextInt(max);
	}
	
}
