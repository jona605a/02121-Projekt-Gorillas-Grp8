package application;

import javafx.application.Application;
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
