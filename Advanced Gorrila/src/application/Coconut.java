package application;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Coconut extends Castable {
	
	//A harder hitting castable than the regular banana.

	Coconut(double x, double y) {
		super(x, y);
		damage = 20;
		weight = 2;
		hitBox = new Circle(x,y,6.5, Color.BROWN);
        hitBox.setStroke(Color.BLACK);
        //circle.setVisible(false);
        setSprite(coconut);
	}

}
