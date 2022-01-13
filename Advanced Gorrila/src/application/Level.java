package application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
        buildingWidth = x / numOfBuildings;
        buildingStoryHeight = 0.19 * screenY;
        for(int i = 0; i < numOfBuildings; i++){
            int stories = 2 + r.nextInt(3);
            statics.add(new Building(buildingWidth * i, screenY - buildingStoryHeight * stories, buildingWidth,buildingStoryHeight, stories));
            game.getChildren().addAll(statics.get(i).getSprites());
        }
        player1 = new Player(10,"Player 1",buildingWidth / 2, statics.get(0).getY());
        player2 = new Player(10,"Player 2",buildingWidth / 2 + buildingWidth * (numOfBuildings - 1), statics.get((numOfBuildings - 1)).getY());
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
