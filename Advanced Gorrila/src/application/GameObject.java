package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurve;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.EventHandler;

import java.security.Key;
import java.util.ArrayList;


public class GameObject {


    private MenuController menuController = new MenuController(this);
    private Stage mainStage;
    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    private double screenX = screenBounds.getMaxX(), screenY = screenBounds.getMaxY();
    private double gravity = -9.81;
    private Level level = new Level(screenX, screenY);
    Line aimLine = new Line(0,0,0,0);
    QuadCurve jumpLine = new QuadCurve();
    private boolean firing;
    private boolean fired;
    private boolean jumpMode;
    Timeline fireLineTimeline;
    Timeline thrownCastableTimeline;
    Timeline jumpTimeLine;
    private boolean player1Turn;
    private boolean gameRunning;
    private boolean outOfScreen = false;
    private boolean didJump = false;
    private EventHandler<MouseEvent> aimHandler;
    private EventHandler<MouseEvent> firePressed;
    private EventHandler<MouseEvent> fireReleased;
    private EventHandler<KeyEvent> toJumpMode;
    private EventHandler<MouseEvent> drawJump;
    private EventHandler<MouseEvent> jump;
    Robot robot = new Robot();
    Polygon outOfScreenTrackArrow = GUIHelpers.createArrow(0,0,10,40, Math.PI);
    private int aimlineMinRadius = 30;
    private int aimlineMaxRadius = 100;


    GameObject(Stage mainStage) throws Exception {

        this.mainStage = mainStage;
        player1Turn = true;
        gameRunning = false;
        jumpMode = false;
        fired = false;
        outOfScreenTrackArrow.setFill(Color.YELLOW);
        outOfScreenTrackArrow.setStroke(Color.BLACK);
        jumpLine.setFill(Color.TRANSPARENT);
        jumpLine.setStroke(Color.BLACK);

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



    }

    public void playerTurn(Player player){
        aimHandler  = event -> drawAimline(event, player.getPosX(), player.getPosY());
        firePressed = event -> startFireIncrease(event, player);
        fireReleased = event -> fireCastable(event, player);
        toJumpMode = event -> toggleJumpMpde(event, player);
        drawJump = event -> drawJumpLine(event, player);
        jump = event -> playerJump(event, player);
        jumpMode = false;
        didJump =  false;


        level.getGame().addEventFilter(MouseEvent.MOUSE_MOVED, aimHandler);
        level.getGame().addEventFilter(MouseEvent.MOUSE_PRESSED, firePressed);
        //level.getGameScene().addEventFilter(KeyEvent.KEY_PRESSED, toJumpMode);



    }

    public void drawAimline(MouseEvent event, double playerX, double playerY){
        drawAimline(event.getX(), event.getY(), playerX, playerY);

    }
    public void drawAimline(double mouseX, double mouseY, double playerX, double playerY){
        double angle = Math.atan((playerY - mouseY) / (mouseX - playerX));
        if(playerX > mouseX) angle += Math.PI;
        aimLine.setStartX(playerX + aimlineMinRadius * Math.cos(angle));
        aimLine.setStartY(playerY - aimlineMinRadius * Math.sin(angle));
        aimLine.setEndX(playerX + Math.cos(angle) * aimlineMaxRadius);
        aimLine.setEndY(playerY - Math.sin(angle) * aimlineMaxRadius);
    }

    public void startFireIncrease(MouseEvent event, Player player){
        // Triggers when the mouse is pressed:
        // Begins drawing a growing line towards the mouse

        if(firing){
            event.consume();
            return;
        }

        // firing setup
        firing = true;
        double angle = GUIHelpers.getAngleOfLine(player.getPosX(), player.getPosY(), event.getX(), event.getY());
        aimLine.setStartX(player.getPosX() + aimlineMinRadius * Math.cos(angle));
        aimLine.setStartY(player.getPosY() - aimlineMinRadius * Math.sin(angle));
        aimLine.setEndX(player.getPosX() + aimlineMinRadius * Math.cos(angle));
        aimLine.setEndY(player.getPosY() - aimlineMinRadius * Math.sin(angle));
        // When firing, listen for when the mouse is released
        level.getGame().addEventFilter(MouseEvent.MOUSE_RELEASED, fireReleased);

        player.setSprite(player.gorilla2);
        fireLineTimeline = new Timeline(new KeyFrame(Duration.millis(1000.0 / 24), (e) -> {drawFireLine(event, player.getPosX(), player.getPosY());}));
        level.getGame().removeEventFilter(MouseEvent.MOUSE_MOVED, aimHandler);
        fireLineTimeline.setCycleCount(Timeline.INDEFINITE);
        fireLineTimeline.play();


    }

    public void drawFireLine(MouseEvent event, double playerX, double playerY){
        double angle = Math.atan((playerY - event.getY()) / (event.getX() - playerX));
        if(playerX > event.getX()) angle += Math.PI;

        if(Math.abs(aimLine.getEndX() - aimLine.getStartX()) < Math.abs(Math.cos(angle) * 99)){
            aimLine.setEndX(aimLine.getEndX() +  Math.cos(angle) * 3);
            aimLine.setEndY(aimLine.getEndY() -  Math.sin(angle) * 3);
        }else{
            fireLineTimeline.stop();
        }
    }

    public void fireCastable(MouseEvent event, Player player){
        // Triggers when the mouse is released:
        // Pauses the players' interaction and animates the throw of the castable.

        if(fired) {
            event.consume();
            return;
        }
        fired = true;
        player.setSprite(player.gorilla1);
        fireLineTimeline.stop();
        level.getGame().removeEventFilter(MouseEvent.MOUSE_PRESSED,firePressed);
        level.getGame().removeEventFilter(MouseEvent.MOUSE_RELEASED, fireReleased);

        Castable selectedCastable = player.getSelectedCastable();
        thrownCastableTimeline = new Timeline(new KeyFrame(Duration.millis(1000.0/24),(e) -> {animateCastable(selectedCastable);}));
        thrownCastableTimeline.setCycleCount(Timeline.INDEFINITE);

        selectedCastable.setVelocityX((aimLine.getEndX() - aimLine.getStartX()) * 10.0 / 24);
        selectedCastable.setVelocityY((aimLine.getStartY() - aimLine.getEndY()) * 10.0 / 24);
        selectedCastable.setX(aimLine.getStartX());
        selectedCastable.setY(aimLine.getStartY());

        level.getGame().getChildren().add(selectedCastable.getSpriteView());
        //level.getGame().getChildren().add(selectedCastable.getCircle());
        level.getGame().getChildren().remove(aimLine);
        firing = false;
        Sound.play("/Sounds/Throw.mp3");
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

            // end of turn
            // remove all listeners
            level.getGame().getChildren().remove(castable.getSpriteView());
            thrownCastableTimeline.stop();
            level.getGame().removeEventFilter(MouseEvent.MOUSE_RELEASED, fireReleased);
            if(!didJump){
                level.getGameScene().removeEventFilter(KeyEvent.KEY_PRESSED, toJumpMode);
            }



            aimLine.setStartX(0);
            aimLine.setStartY(0);
            aimLine.setEndX(0);
            aimLine.setEndY(0);
            level.getGame().getChildren().remove(outOfScreenTrackArrow);
            outOfScreen = false;
            player1Turn = !player1Turn;
            fired = false;
            gameLoop();
        }

    }

    public void toggleJumpMpde(KeyEvent event, Player player){
        if(event.getCode() == KeyCode.J){
            if(!jumpMode){
                level.getGame().removeEventFilter(MouseEvent.MOUSE_MOVED, aimHandler);
                level.getGame().removeEventFilter(MouseEvent.MOUSE_PRESSED, firePressed);
                level.getGame().addEventFilter(MouseEvent.MOUSE_MOVED, drawJump);
                level.getGame().addEventFilter(MouseEvent.MOUSE_PRESSED, jump);
                level.getGame().getChildren().add(jumpLine);
                level.getGame().getChildren().remove(aimLine);
                drawJumpLine(robot.getMouseX(), player);
                jumpMode = true;
            }else if(!didJump){
                level.getGame().addEventFilter(MouseEvent.MOUSE_MOVED, aimHandler);
                level.getGame().addEventFilter(MouseEvent.MOUSE_PRESSED, firePressed);
                level.getGame().removeEventFilter(MouseEvent.MOUSE_MOVED, drawJump);
                level.getGame().removeEventFilter(MouseEvent.MOUSE_PRESSED, jump);
                level.getGame().getChildren().remove(jumpLine);
                level.getGame().getChildren().add(aimLine);
                jumpMode = false;
            }

        }
    }


    public void drawJumpLine(MouseEvent event, Player player){
        drawJumpLine(event.getX(), player);
    }

    //Overload for single call
    public void drawJumpLine(double mouseX, Player player){
        double jumpSpeed = 38.36 * 1.5;
        double x = player.getPosX();
        double y = player.getPosY();
        double maxX = jumpSpeed * Math.cos(Math.PI/4) * jumpSpeed * Math.sin(Math.PI/4) / (-gravity) * 2;
        double xlength = Math.abs(mouseX - x) > maxX ? maxX : mouseX - x;
        double angle = Math.PI/2 - Math.asin(-gravity * xlength / Math.pow(jumpSpeed, 2)) / 2;
        double t = jumpSpeed * Math.sin(angle) / (-gravity) * 2;
        double ylength = jumpSpeed * Math.sin(angle) * t / 2  + gravity / 2 * Math.pow(t / 2, 2);
        double controlX = (x * 0.5 + 0.25 * xlength) * 2;
        double controlY = y - ylength * 2;

        //System.out.println(angle +  " " + xlength + " " + ylength);


        jumpLine.setStartX(x);
        jumpLine.setStartY(y);
        jumpLine.setEndX(x + xlength);
        jumpLine.setEndY(y);
        jumpLine.setControlX(controlX);
        jumpLine.setControlY(controlY);
        player.setVelocityX(jumpSpeed * Math.cos(angle));
        player.setVelocityY(jumpSpeed * Math.sin(angle));


        StaticEntity intercept = null;
        for(StaticEntity levelObj : getLevel().getStatics()){
            if(intercept == null && levelObj.collision(jumpLine.getBoundsInLocal())){
                intercept = levelObj;
            }else if(intercept != null && levelObj.collision(jumpLine.getBoundsInLocal())){
                if(intercept.getX() > levelObj.getX()){
                    intercept = levelObj;
                }
            }
        }
    }

    public void playerJump(MouseEvent event, Player player){
        System.out.println("HEJ");
        jumpTimeLine = new Timeline(new KeyFrame(Duration.millis(1000.0/24),(e) -> animateJump(player)));
        level.getGame().removeEventFilter(KeyEvent.KEY_PRESSED, toJumpMode);
        level.getGame().removeEventFilter(MouseEvent.MOUSE_MOVED, drawJump);
        level.getGame().removeEventFilter(MouseEvent.MOUSE_PRESSED, jump);
        level.getGame().getChildren().remove(jumpLine);
        jumpTimeLine.setCycleCount(Timeline.INDEFINITE);
        jumpTimeLine.play();
        System.out.println(player.getVelocityX() + " " + player.getVelocityY());
    }

    public void animateJump(Player player){
        boolean stop = false;
        player.setPosX(player.getPosX() + player.getVelocityX() / 24);
        player.setPosY(player.getPosY() - player.getVelocityY() / 24);
        player.setVelocityY(player.getVelocityY() + gravity / 24);

        for(StaticEntity statics : level.getStatics()){
            stop = stop || statics.collision(player.getHitBox().getBoundsInLocal());
        }
        System.out.println("Stop 1 " + stop);
        Player opponent = player1Turn ? level.getPlayer2() : level.getPlayer1();
        System.out.println(player1Turn);

        stop |= GUIHelpers.isOutOfScreen(player.getHitBox().getBoundsInLocal(), screenX, screenY) || opponent.collision(player.getHitBox().getBoundsInLocal());

        if(stop){
            jumpTimeLine.stop();
            level.getGame().addEventFilter(MouseEvent.MOUSE_MOVED, aimHandler);
            level.getGame().addEventFilter(MouseEvent.MOUSE_PRESSED, firePressed);
            level.getGame().getChildren().add(aimLine);
            drawAimline(robot.getMouseX(), robot.getMouseY(), opponent.getPosX(), opponent.getPosY());
        }



    }

    public MenuController getMenuController() {
        return menuController;
    }

    public Level getLevel(){
        return level;
    }

    public boolean isPlayer1Turn() { return this.player1Turn;}

    public void setPlayer1Turn(boolean player1Turn) {
        this.player1Turn = player1Turn;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }
}
