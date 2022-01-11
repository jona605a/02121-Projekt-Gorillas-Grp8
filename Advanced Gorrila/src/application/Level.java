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
    private Group game;
    private Scene gameScene;
    private ArrayList<StaticEntity> statics = new ArrayList<StaticEntity>();
    private double buildingWidth;
    private Random r = new Random();
    private int numOfBuildings = 10;

    Level(double x, double y){
        screenX = x;
        screenY = y;
        game = new Group();
        gameScene = new Scene(game, screenX, screenY);
        buildingWidth = x / numOfBuildings;
        for(int i = 0; i < numOfBuildings; i++){
            double offset =  y * (0.4 + r.nextDouble(-0.2,0.2));
            statics.add(new Building(buildingWidth * i, screenY - offset , buildingWidth,offset, Color.BEIGE));
            game.getChildren().add(statics.get(i).getShape());
        }
        System.out.println(statics.get(0).getY());
        player1 = new Player(10,"Player 1",buildingWidth / 2, statics.get(0).getY());
        player2 = new Player(10,"Player 2",buildingWidth / 2 + buildingWidth * (numOfBuildings - 1), statics.get((numOfBuildings - 1)).getY());
        game.getChildren().add(player1.getHitBox());
        game.getChildren().add(player2.getHitBox());

    }

    public Scene getGameScene() {
        return gameScene;
    }

}
