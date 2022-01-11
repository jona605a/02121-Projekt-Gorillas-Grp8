package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Banana extends Castable{


    Banana(double x, double y) {
        super(x, y);
        circle = new Circle(x,y,5, Color.YELLOW);
        circle.setStroke(Color.BLACK);
    }
}
