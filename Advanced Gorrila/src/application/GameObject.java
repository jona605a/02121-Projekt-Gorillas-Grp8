package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.EventHandler;

import java.util.ArrayList;


public class GameObject {


    private MenuController menuController = new MenuController(this);
    private Stage mainStage;
    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    private double screenX = screenBounds.getMaxX(), screenY = screenBounds.getMaxY();
    private double gravity = -9.81;
    private Level level = new Level(screenX, screenY);
    Line aimLine = new Line(0,0,0,0);
    private boolean firing;
    Timeline fireLineTimeline;
    Timeline thrownCastableTimeline;
    private boolean player1Turn;
    private boolean gameRunning;
    EventHandler<MouseEvent> aimHandler;
    EventHandler<MouseEvent> firePressed;
    EventHandler<MouseEvent> fireReleased;
    Robot robot = new Robot();
    Polygon outOfScreenTrackArrow = GUIHelpers.createArrow(0,0,10,40, Math.PI);
    private boolean outOfScreen = false;


    GameObject(Stage mainStage){

        this.mainStage = mainStage;
        player1Turn = true;
        gameRunning = false;
        outOfScreenTrackArrow.setFill(Color.YELLOW);
        outOfScreenTrackArrow.setStroke(Color.BLACK);

    }


    public void enterMenu(){
        menuController.menuSetup(screenX, screenY, mainStage);
    }



    public void gameLoop(){
        level.getGame().getChildren().add(aimLine);

        if(player1Turn){
            playerTurn(level.getPlayer1());
            drawAimline(robot.getMouseX(), robot.getMouseY(), level.getPlayer1().getPosX(),level.getPlayer1().getPosY());
        }else{
            playerTurn(level.getPlayer2());
            drawAimline(robot.getMouseX(), robot.getMouseY(), level.getPlayer2().getPosX(),level.getPlayer2().getPosY());
        }
        player1Turn = !player1Turn;


    }

    public void playerTurn(Player player){
        aimHandler  = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                drawAimline(event, player.getPosX(), player.getPosY());
            }
        };
        firePressed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startFireIncrease(event, player.getPosX(), player.getPosY());
            }
        };

        fireReleased = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                fireCastable(event, player);
            }
        };

        level.getGame().addEventFilter(MouseEvent.MOUSE_MOVED, aimHandler);
        level.getGame().addEventFilter(MouseEvent.MOUSE_PRESSED, firePressed);
        level.getGame().addEventFilter(MouseEvent.MOUSE_RELEASED, fireReleased);


    }

    public void startFireIncrease(MouseEvent event, double playerX, double playerY){
        fireLineTimeline = new Timeline(new KeyFrame(Duration.millis(1000 / 24), (e) -> {drawFireLine(event, playerX, playerY);}));
        level.getGame().removeEventFilter(MouseEvent.MOUSE_MOVED, aimHandler);
        fireLineTimeline.setCycleCount(Timeline.INDEFINITE);
        fireLineTimeline.play();
    }

    public void fireCastable(MouseEvent event, Player player){
        fireLineTimeline.stop();
        level.getGame().removeEventFilter(MouseEvent.MOUSE_PRESSED,firePressed);
        level.getGame().removeEventFilter(MouseEvent.MOUSE_RELEASED, fireReleased);

        thrownCastableTimeline = new Timeline(new KeyFrame(Duration.millis(1000/24),(e) -> {animateCastable(player.getSelectedCastable());}));
        thrownCastableTimeline.setCycleCount(Timeline.INDEFINITE);

        player.getSelectedCastable().setVelocityX((aimLine.getEndX() - aimLine.getStartX()) * 10 / 24);
        player.getSelectedCastable().setVelocityY((aimLine.getStartY() - aimLine.getEndY()) * 10 / 24);
        player.getSelectedCastable().setX(player.getPosX() + 15);
        player.getSelectedCastable().setY(player.getPosY() - 15);

        level.getGame().getChildren().add(player.getSelectedCastable().getCircle());
        level.getGame().getChildren().remove(aimLine);
        firing = false;
        thrownCastableTimeline.play();


    }

    public void animateCastable(Castable castable){
        boolean stop = false;
        castable.setX(castable.getX() + castable.getVelocityX());
        castable.setY(castable.getY() - castable.getVelocityY());
        castable.setVelocityY(castable.getVelocityY() + gravity / 24);
        for(StaticEntity statics : level.getStatics()){
            stop = stop || statics.collision(castable.getCircle().getLayoutBounds());


        }

        if(!outOfScreen && (castable.getCircle().getCenterY() < 0)){
            outOfScreen = true;
            level.getGame().getChildren().add(outOfScreenTrackArrow);
        }
        if(outOfScreen){
            outOfScreenTrackArrow.setLayoutX(castable.getX() - outOfScreenTrackArrow.getBoundsInLocal().getWidth()/2);
            outOfScreenTrackArrow.setLayoutY(outOfScreenTrackArrow.getBoundsInLocal().getHeight());
        }
        if(outOfScreen && (castable.getCircle().getCenterY() > 0)){
            level.getGame().getChildren().remove(outOfScreenTrackArrow);
            outOfScreen = false;
        }

        stop = stop || GUIHelpers.isOutOfGame(castable.getCircle().getBoundsInLocal(), screenX, screenY);
        stop = stop || level.getPlayer1().collision(castable.getCircle().getBoundsInLocal());
        stop = stop || level.getPlayer2().collision(castable.getCircle().getBoundsInLocal());
        if(stop){
            level.getGame().getChildren().remove(castable.getCircle());
            thrownCastableTimeline.stop();
            level.getGame().removeEventFilter(MouseEvent.MOUSE_RELEASED, fireReleased);
            aimLine.setStartX(0);
            aimLine.setStartY(0);
            aimLine.setEndX(0);
            aimLine.setEndY(0);
            level.getGame().getChildren().remove(outOfScreenTrackArrow);
            outOfScreen = false;
            gameLoop();
        }

    }

    public void drawAimline(MouseEvent event, double playerX, double playerY){

        double angle = Math.atan((playerY - event.getY()) / (event.getX() - playerX));
        if(playerX > event.getX()) angle += Math.PI;
        aimLine.setStartX(playerX + 15 * Math.cos(angle));
        aimLine.setStartY(playerY - 15 * Math.sin(angle));
        aimLine.setEndX(playerX + Math.cos(angle) * 100);
        aimLine.setEndY(playerY - Math.sin(angle) * 100);

    }

    public void drawAimline(double mouseX, double mouseY, double playerX, double playerY){
        double angle = Math.atan((playerY - mouseY) / (mouseX - playerX));
        if(playerX > mouseX) angle += Math.PI;
        aimLine.setStartX(playerX + 15 * Math.cos(angle));
        aimLine.setStartY(playerY - 15 * Math.sin(angle));
        aimLine.setEndX(playerX + Math.cos(angle) * 100);
        aimLine.setEndY(playerY - Math.sin(angle) * 100);

    }



    public void drawFireLine(MouseEvent event, double playerX, double playerY){
        double angle = Math.atan((playerY - event.getY()) / (event.getX() - playerX));
        if(playerX > event.getX()) angle += Math.PI;

        if(!firing){
            aimLine.setStartX(playerX + 15 * Math.cos(angle));
            aimLine.setStartY(playerY - 15 * Math.sin(angle));
            aimLine.setEndX(playerX + 15 * Math.cos(angle));
            aimLine.setEndY(playerY - 15 * Math.sin(angle));
            firing = true;
        }

        if(Math.abs(aimLine.getEndX() - aimLine.getStartX()) < Math.abs(Math.cos(angle) * 99)){
            aimLine.setEndX(aimLine.getEndX() +  Math.cos(angle) * 3);
            aimLine.setEndY(aimLine.getEndY() -  Math.sin(angle) * 3);
        }else{
            fireLineTimeline.stop();
        }




    }

    public MenuController getMenuController() {
        return menuController;
    }

    public Level getLevel(){
        return level;
    }

    public void setPlayer1Turn(boolean player1Turn) {
        this.player1Turn = player1Turn;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }
}