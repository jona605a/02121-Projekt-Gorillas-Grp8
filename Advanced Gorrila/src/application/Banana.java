package application;

import javafx.scene.image.Image;
import javafx.scene.shape.Circle;

public class Banana extends Castable{

    public Image banana = new Image(ClassLoader.getSystemResource("Images/Banan.png").toString(), 20, 20, true, false);

    Banana(double x, double y) {
        super(x, y);
        weight = 1;
        hitBox = new Circle(x,y,4);
        damage = 30;

        setSprite(banana);
    }
}

