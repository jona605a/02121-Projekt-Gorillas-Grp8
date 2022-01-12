package application;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class GUIHelpers {

    public static int textSize(Label label){
        Text tmp = new Text(label.getText());
        tmp.setFont(label.getFont());
        return (int) tmp.getBoundsInLocal().getWidth();
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
        System.out.println(sin + " " + cos);
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
}
