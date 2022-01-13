package application;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;




public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        GameObject gameObject = new GameObject(stage);
        gameObject.enterMenu();
    }

    public static void main(String[] args) {

        launch(args);

    }


}
