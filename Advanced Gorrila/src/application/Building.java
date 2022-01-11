package application;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Building extends StaticEntity{
    private Rectangle shape;

    Building(double x1, double y1, double width, double height, Color color){
        shape = new Rectangle(x1,y1,width,height);
        x = x1;
        y = y1;
        this.width = width;
        this.height = height;

        shape.setFill(color);
        shape.setStroke(Color.BLACK);

    }

    @Override
    public boolean collision(double x, double y) {
        return shape.contains(x, y);
    }

    @Override
    public Node getShape(){
        return shape;
    }
}
