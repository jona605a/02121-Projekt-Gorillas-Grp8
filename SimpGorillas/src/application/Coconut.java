package application;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Coconut extends Castable {
	
	//A harder hitting castable than the regular banana.

	Coconut(double x, double y) {
		super(x, y, 5, 1);
		circle = new Circle(x,y,6.5, Color.BROWN);
        circle.setStroke(Color.BLACK);
        //circle.setVisible(false);
        setSprite(coconut);
	}

}
