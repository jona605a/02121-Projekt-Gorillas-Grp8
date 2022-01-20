package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class Level {
    private Player player1;
    private Player player2;
    private double screenX, screenY;
    private AnchorPane game;
    private Scene gameScene;
    private Random r = new Random();
    private int numOfPowerUps = 20;

    private Map level1;
    private Map level2;
    private Map level3;
    private Map randomLevel;
    private Map currentLevel;

    private Image backgroundImg;
    private ImageView bg1;
    private ImageView bg2;
    private boolean bg2IsBack;
    private Timeline backgroundTimeline;

    Level(double x, double y) throws Exception {
        int numOfBuildings = 0;
        Building firstBuilding, lastBuilding;
        screenX = x;
        screenY = y;
        randomLevel = new Map(screenX, screenY, true);
        currentLevel = randomLevel;
        game = randomLevel.getPane();
        gameScene = new Scene(game, screenX, screenY);

        firstBuilding =  getBuildings().get(0);
        lastBuilding =  getBuildings().get(currentLevel.getNumOfBuildings() - 1);

        player1 = new Player(100,"Player 1",currentLevel.getBuildingWidth() / 2 + firstBuilding.getX(), firstBuilding.getY(), screenY);
        player2 = new Player(100,"Player 2",currentLevel.getBuildingWidth() / 2 + lastBuilding.getX(), lastBuilding.getY(), screenY);

        backgroundImg = new Image("/Images/Sky.png", screenX, screenY, false, false);
        bg1 = new ImageView(backgroundImg);
        bg2 = new ImageView(backgroundImg);
        backgroundTimeline = new Timeline(new KeyFrame(Duration.millis(1000.0/24), (e) -> {animateBackground();}));
        backgroundTimeline.setCycleCount(-1);

        player1.addNodes(game);
        player2.addNodes(game);
        game.getChildren().add(bg1);
        game.getChildren().add(bg2);


    }

    public void animateBackground(){
        bg1.setX(bg1.getX() + 1);
        bg2.setX(bg2.getX() + 1);
        if(bg2IsBack){
            if(bg2.getX() == 0){
                bg1.setX(-screenX);
                bg2IsBack = false;
            }
        }else{
            if(bg1.getX() == 0){
                bg2.setX(-screenX);
                bg2IsBack = true;
            }
        }
    }

    public void setupLevel(){
        Building firstBuilding, lastBuilding;

        // setup background
        bg2IsBack = true;
        bg1.toBack();
        bg1.setX(0);
        bg1.setY(0);
        bg2.toBack();
        bg2.setX(-screenX);
        bg2.setY(0);
        backgroundTimeline.play();

        // setup players
        firstBuilding =  getBuildings().get(0);
        lastBuilding =  getBuildings().get(currentLevel.getNumOfBuildings() - 1);

        player1.setHitpoints(100);
        player1.setPosX(currentLevel.getBuildingWidth() / 2 + firstBuilding.getX());
        player1.setPosY(firstBuilding.getY() - player1.gorilla1.getHeight() / 2);
        player1.getPowerUps().clear();
        player1.getCastables().clear();
        player1.addCastable(new Banana(player1.getPosX(), player1.getPosY()));
        player1.setNumOfCoconuts(10);
        player1.addCoconuts(10);

        player2.setHitpoints(100);
        player2.setPosX(currentLevel.getBuildingWidth() / 2 + lastBuilding.getX());
        player2.setPosY(lastBuilding.getY() - player2.gorilla1.getHeight() / 2);
        player2.getPowerUps().clear();
        player2.getCastables().clear();
        player2.addCastable(new Banana(player2.getPosX(), player2.getPosY()));
        player2.setNumOfCoconuts(10);
        player2.addCoconuts(10);

        // remove unused powerups


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

    public ArrayList<MapObject> getMapObjects(){
        return currentLevel.getMapObjects();
    }

    public ArrayList<Building> getBuildings() {
        return currentLevel.getBuildings();
    }

    public ArrayList<PowerUp> getPowerUps() { return currentLevel.getPowerUps();}


    public Timeline getBackgroundTimeline() {
        return backgroundTimeline;
    }
}
