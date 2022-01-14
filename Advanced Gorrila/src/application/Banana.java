package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Banana extends Castable{


    Banana(double x, double y) {
        super(x, y, 2, 0.2);
        circle = new Circle(x,y,6.5, Color.YELLOW);
        circle.setStroke(Color.BLACK);
        //circle.setVisible(false);
        setSprite(banana);
    }
}

