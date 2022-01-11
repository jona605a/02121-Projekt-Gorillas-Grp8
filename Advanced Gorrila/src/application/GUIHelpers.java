package application;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
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
}
