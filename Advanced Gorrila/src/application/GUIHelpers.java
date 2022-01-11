package application;

import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class GUIHelpers {

    public static int textSize(Label label){
        Text tmp = new Text(label.getText());
        tmp.setFont(label.getFont());
        return (int) tmp.getBoundsInLocal().getWidth();
    }
}
