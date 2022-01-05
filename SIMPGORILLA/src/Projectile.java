import javafx.scene.Group;
import javafx.scene.shape.Circle;

public class Projectile {
	

	private double velocity;
	private Gorilla target;
	private double angle;
	private double gravity = 9.81;
	private Circle banana;
	
	Projectile(double startX, double startY, double velocity, double angle, Gorilla target, double screenWidth, double screenHeight, Group root, boolean flyEast) {
		this.velocity = velocity;
		this.angle = angle;
		this.target = target;
		banana = new Circle(startX, startY, 2);
		root.getChildren().add(banana);
		simulateThrow(screenWidth, screenHeight, flyEast);
		
	}
	
	public void simulateThrow(double screenWidth, double screenHeight, boolean flyEast) {
		
		new Thread(() -> {
			for (int i = 0; i < screenWidth; i++) {
				// Calculating the new position of the projectile
				
				if(flyEast) {
					banana.setCenterX(i);
				} else {
					banana.setCenterX(500-i);
				}
				
				double newY = -this.gravity / (2 * this.velocity * this.velocity * Math.cos(this.angle) * Math.cos(this.angle)) * i * i + Math.tan(angle) * i;
				banana.setCenterY(screenHeight-newY);
				// Check if the target is hit
				if (target.isHit(banana.getCenterX(), banana.getCenterY(), screenWidth)) {
					Main.endGame();
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			banana.setVisible(false);
		}).start();
	}
		
	
}
 