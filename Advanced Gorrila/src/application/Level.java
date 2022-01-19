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
    private ArrayList<StaticEntity> statics = new ArrayList<StaticEntity>();
    private double buildingWidth, buildingStoryHeight;
    private Random r = new Random();
    private int numOfBuildings = 10;
    private ArrayList<PowerUp> powerUps;
    private int numOfPowerUps = 20;
    private Image backgroundImg;
    private ImageView bg1;
    private ImageView bg2;
    private boolean bg2IsBack;
    private Timeline backgroundTimeline;

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
        powerUps = PowerUpSpawner.spawnPowerUps(0,  0, screenX, screenY-buildingStoryHeight*2, numOfPowerUps, statics);
        for (PowerUp powerUp : powerUps ) {
            game.getChildren().add(powerUp.getSprite());
        }
        player1 = new Player(10,"Player 1",buildingWidth / 2, statics.get(0).getY());
        player2 = new Player(10,"Player 2",buildingWidth / 2 + buildingWidth * (numOfBuildings - 1), statics.get((numOfBuildings - 1)).getY());
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
        bg2IsBack = true;
        bg1.toBack();
        bg1.setX(0);
        bg1.setY(0);
        bg2.toBack();
        bg2.setX(-screenX);
        bg2.setY(0);
        backgroundTimeline.play();
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

    public ArrayList<PowerUp> getPowerUps() { return powerUps;}

    public void setPowerUps(ArrayList<PowerUp> newPowerUps) {
        this.powerUps = newPowerUps;
    }
}
