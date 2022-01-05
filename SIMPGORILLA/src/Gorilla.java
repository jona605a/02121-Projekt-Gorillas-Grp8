
public class Gorilla {
	
	private double posX;
	private double posY;
	private double height;
	private double width;
	
	Gorilla(double posX, double posY, double height, double width, View view) {
		this.posX = posX;
		this.posY = posY;
		this.height = height;
		this.width = width;
		view.drawBox(posX, posY, width, height);
		
	}
	
	public boolean isHit(double projectileX, double projectileY, double screenWidth) {
		if (Math.sqrt( (this.posX - projectileX) * (this.posX - projectileX) + (this.posY - projectileY) * (this.posY - projectileY) ) <= screenWidth / 50) {
			return true;
		}
		return false;
	}
	
	public double getX() {
		return this.posX;
	}
	
	public double getY() {
		return this.posY;
	}
}
