package application;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Scanner;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the screen width");
        int n = sc.nextInt();
        System.out.println("Please enter the screen height");
        int m = sc.nextInt();
        GameObject gameObject = new GameObject(stage, n, m);
        gameObject.start_game();


    }

    public static void main(String[] args) {

        launch(args);

    }


}
