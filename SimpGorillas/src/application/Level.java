package application;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Random;

public class Level {
    private Player player1;
    private Player player2;
    private double screenX, screenY;
    private AnchorPane game;
    private Scene gameScene;
    private ArrayList<StaticEntity> statics = new ArrayList<StaticEntity>();
    private double buildingWidth, buildingStoryHeight;
    private Random r = new Random();
    private int numOfBuildings = 10;

    Level(double x, double y) throws Exception {
        screenX = x;
        screenY = y;
        game = new AnchorPane();
        gameScene = new Scene(game, screenX, screenY);

        player1 = new Player(10,"Player 1",0,y,20);
        player2 = new Player(10,"Player 2",x-1,y,-20);
        game.getChildren().add(player1.getNameLabel());
        game.getChildren().add(player2.getNameLabel());
        game.getChildren().add(player1.getSpriteView());
        game.getChildren().add(player2.getSpriteView());
    }

    public void setupLevel(){

    }
    public Scene getGameScene() {
        return gameScene;
    }

    public AnchorPane getGame() {
        return game;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public ArrayList<StaticEntity> getStatics() {
        return statics;
    }
}
