package application;

import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class Helpers {

    public static double textSize(Label label){
        Text tmp = new Text(label.getText());
        tmp.setFont(label.getFont());
        return tmp.getBoundsInLocal().getWidth();
    }

    public static double textSize(Button button){
        Text tmp = new Text(button.getText());
        tmp.setFont(button.getFont());
        return tmp.getBoundsInLocal().getWidth();
    }

    public static boolean isOutOfScreen(Bounds localBounds, double screenX, double screenY){
        return (localBounds.getMaxX() >= screenX || localBounds.getMinX() <= 0) || (localBounds.getMaxY() >= screenY || localBounds.getMinY() <= 0);
    }
    public static boolean isOutOfGame(Bounds localBounds, double screenX, double screenY) {
        return (localBounds.getMaxX() >= screenX || localBounds.getMinX() <= 0) || (localBounds.getMaxY() >= screenY);
    }

    public static Polygon createArrow(double x, double y, double width, double height, double angle){
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double arrowPercentage = 0.7;
        Polygon arrow = new Polygon();
        double arrowWidth = width / 2;
        Double[] points = new Double[] {
                x, y,
                x + cos * width, y + sin * width,
                x + cos * width + sin * height * arrowPercentage, y + sin * width + cos * height * arrowPercentage,
                x + cos * (width + arrowWidth) + sin * height * arrowPercentage, y + sin * (width + arrowWidth) + cos * height * arrowPercentage,
                x + cos * arrowWidth + sin * height, y + sin * arrowWidth + cos * height,
                x - cos * arrowWidth + sin * height * arrowPercentage, y - sin * arrowWidth + cos * height * arrowPercentage,
                x + sin * height, y + cos * height * arrowPercentage};
        arrow.getPoints().addAll(points);
        return arrow;
    }

    public static double getAngleOfLine(double x1, double y1, double x2, double y2){
        double angle = Math.atan((y1 - y2) / (x2 - x1));

        return x1 > x2 ? angle + Math.PI : angle;
    }

    public boolean below(Bounds obj1, Bounds obj2){
        return obj1.getMinY() > obj2.getMaxY();
    }

    public boolean above(Bounds obj1, Bounds obj2){
        return obj1.getMaxY() < obj2.getMinY();
    }

    public boolean leftOf(Bounds obj1, Bounds obj2){
        return obj1.getMaxX() < obj2.getMinX();
    }

    public boolean rightOf(Bounds obj1, Bounds ojb2){
        return obj1.getMinX() > obj1.getMaxX();
    }


}