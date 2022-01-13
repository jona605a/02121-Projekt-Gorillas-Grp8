package application;

import java.util.Random;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

/*
 * HOW SHOULD IT BE IMPLEMENTED - A POWERUP CHOOSES A RANDOM ACTION OR DIFFERENT TYPES OF POWERUPS?
 * 
 * List of powerup ideas:
 * Make castable faster/slower (just this castable or all castables of this player or all castables of both players?)
 * Make castable(s) deal more dmg (how much?)
 * Current player gets another turn
 * Current player gets extra special ammo (if different types of powerups - different powerups for each ammo or just random?)
 * Current player gets more hp (how much?)
 */

public class PowerUp extends StaticEntity {
	
	private Circle shape;
	
	
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
	
	public void activatePowerUp(Castable castable, Player player) {
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
				secondTurn(player);
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
		castable.setVelocityY(castable.getVelocityY() * 2); // Maybe should not affect y-veloicty??
		
	}
	
	private void slowBullet(Castable castable) {
		// make castable slower
		castable.setVelocityX(castable.getVelocityX() * .5);
		castable.setVelocityY(castable.getVelocityY() * .5); // Maybe should not affect y-veloicty??
	}
	
	private void powerfulBullet(Castable castable) {
		// make castable deal more damage
		
	}
	
	private void secondTurn(Player player) {
		// give the current player another turn
	}
	
	private void extraAmmo(Player player) {
		// gives the current player extra special ammo
		player.addCastable(new Banana(player.getPosX(), player.getPosY()));
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
