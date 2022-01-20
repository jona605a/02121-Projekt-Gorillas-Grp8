package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
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


public class GameObject {


    private MenuController menuController = new MenuController(this);
    private Stage mainStage;
    private boolean muted = false;
    private double volume;
    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    private double screenX = screenBounds.getMaxX(), screenY = screenBounds.getMaxY();

    private double pixelPerMeter = screenY / 30;
    private double gravity = -9.81 * pixelPerMeter;
    private Level level = new Level(screenX, screenY);
    Line aimLine = new Line(0,0,0,0);
    QuadCurve jumpLine = new QuadCurve();
    private boolean firing;
    private boolean fired;
    private boolean jumpMode;

    Timeline fireLineTimeline = new Timeline();
    Timeline thrownCastableTimeline = new Timeline();;
    Timeline jumpTimeLine = new Timeline();
    private boolean jumping;

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
    private EventHandler<KeyEvent> enterPause;
    Robot robot = new Robot();
    Polygon outOfScreenTrackArrow = GUIHelpers.createArrow(0,0,10,40, Math.PI);
    private int aimlineMinRadius = 35;
    private int aimlineMaxRadius = 100;


    GameObject(Stage mainStage) throws Exception {

        this.mainStage = mainStage;
        player1Turn = true;
        gameRunning = false;
        jumpMode = false;
        fired = false;
        jumping = false;
        outOfScreenTrackArrow.setFill(Color.YELLOW);
        outOfScreenTrackArrow.setStroke(Color.BLACK);
        jumpLine.setFill(Color.TRANSPARENT);
        jumpLine.setStroke(Color.BLACK);
        enterPause = event -> enterPauseMenu(event);
        level.getGameScene().addEventFilter(KeyEvent.KEY_PRESSED, enterPause);
        mainStage.addEventFilter(KeyEvent.KEY_PRESSED, (e) -> toggleMute(e));
    }


    public void enterMenu(){
        menuController.menuSetup(screenX, screenY, mainStage);
    }

    public void enterPauseMenu(KeyEvent event){
        if(event.getCode() == KeyCode.P){
            if(jumping){
                jumpTimeLine.pause();
            }
            if(fired){
                thrownCastableTimeline.pause();
            }
            level.getBackgroundTimeline().pause();
            menuController.goToPause();
        }
    }

    public void endGame(){
        thrownCastableTimeline.stop();
        jumpTimeLine.stop();
        fireLineTimeline.stop();
        level.getBackgroundTimeline().stop();
        mainStage.setScene(menuController.getMainMenuScene());
        gameRunning = false;
        jumping = false;
        fired = false;
        firing = false;
        outOfScreen = false;
        didJump = false;

        removeNode(aimLine);
        removeNode(jumpLine);
        removeNode(level.getPlayer1().getSelectedCastable().getSpriteView());
        removeNode(level.getPlayer2().getSelectedCastable().getSpriteView());
        level.getGameScene().removeEventFilter(KeyEvent.KEY_PRESSED, toJumpMode);
        removeFilter(MouseEvent.MOUSE_MOVED, aimHandler);
        removeFilter(MouseEvent.MOUSE_PRESSED,firePressed);
        removeFilter(MouseEvent.MOUSE_RELEASED, fireReleased);
        removeFilter(MouseEvent.MOUSE_MOVED, drawJump);
        removeFilter(MouseEvent.MOUSE_PRESSED, jump);




    }

    public void toggleMute(KeyEvent event){
        if(event.getCode() == KeyCode.M){
            if(muted){
                muted = false;
                menuController.getMusic().changeVolume(volume);
            }else{
                muted = true;
                volume = menuController.getMusic().getCurrentSong().getMediaPlayer().getVolume();
                menuController.getMusic().changeVolume(0);
            }
        }

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


        addFilter(MouseEvent.MOUSE_MOVED, aimHandler);
        addFilter(MouseEvent.MOUSE_PRESSED, firePressed);
        level.getGameScene().addEventFilter(KeyEvent.KEY_PRESSED, toJumpMode);



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
        level.getGameScene().removeEventFilter(KeyEvent.KEY_PRESSED, enterPause);
        level.getGameScene().removeEventFilter(KeyEvent.KEY_PRESSED, toJumpMode);
        double angle = GUIHelpers.getAngleOfLine(player.getPosX(), player.getPosY(), event.getX(), event.getY());
        aimLine.setStartX(player.getPosX() + aimlineMinRadius * Math.cos(angle));
        aimLine.setStartY(player.getPosY() - aimlineMinRadius * Math.sin(angle));
        aimLine.setEndX(player.getPosX() + aimlineMinRadius * Math.cos(angle));
        aimLine.setEndY(player.getPosY() - aimlineMinRadius * Math.sin(angle));
        // When firing, listen for when the mouse is released
        addFilter(MouseEvent.MOUSE_RELEASED, fireReleased);

        player.setSprite(player.gorilla2);
        fireLineTimeline = new Timeline(new KeyFrame(Duration.millis(1000.0 / 24), (e) -> {drawFireLine(event, player.getPosX(), player.getPosY());}));
        removeFilter(MouseEvent.MOUSE_MOVED, aimHandler);
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

        level.getGameScene().addEventFilter(KeyEvent.KEY_PRESSED, enterPause);
        player.setSprite(player.gorilla1);
        fireLineTimeline.stop();
        removeFilter(MouseEvent.MOUSE_PRESSED,firePressed);
        removeFilter(MouseEvent.MOUSE_RELEASED, fireReleased);

        Castable selectedCastable = player.getSelectedCastable();
        thrownCastableTimeline = new Timeline(new KeyFrame(Duration.millis(1000.0/24),(e) -> {animateCastable(selectedCastable);}));
        thrownCastableTimeline.setCycleCount(Timeline.INDEFINITE);

        selectedCastable.setVelocityX((aimLine.getEndX() - aimLine.getStartX()) * 10.0 / selectedCastable.getWeight());
        selectedCastable.setVelocityY((aimLine.getStartY() - aimLine.getEndY()) * 10.0 / selectedCastable.getWeight());
        selectedCastable.setX(aimLine.getStartX());
        selectedCastable.setY(aimLine.getStartY());


        addNode(selectedCastable.getSpriteView());
        //level.getGame().getChildren().add(selectedCastable.getCircle());
        removeNode(aimLine);
        firing = false;
        Sound.play("/Sounds/Throw.mp3");
        thrownCastableTimeline.play();
    }

    public void animateCastable(Castable castable){
        boolean stop = false;
        castable.setX(castable.getX() + castable.getVelocityX() / 24);
        castable.setY(castable.getY() - castable.getVelocityY() / 24);
        castable.setVelocityY(castable.getVelocityY() + gravity / 24);
        for(MapObject statics : level.getBuildings()){
            stop = stop || statics.collision(castable.getHitBox().getLayoutBounds());
        }

        for (PowerUp powerUp : level.getPowerUps()) {
            if (powerUp.collision(castable.getHitBox().getLayoutBounds())) {
                powerUp.onCollision(this);
                return;
            }
        }

        if(!outOfScreen && (castable.getHitBox().getCenterY() < 0)){
            outOfScreen = true;
            addNode(outOfScreenTrackArrow);
        }
        if(outOfScreen){
            outOfScreenTrackArrow.setLayoutX(castable.getX() - outOfScreenTrackArrow.getBoundsInLocal().getWidth()/2);
            outOfScreenTrackArrow.setLayoutY(outOfScreenTrackArrow.getBoundsInLocal().getHeight());
        }
        if(outOfScreen && (castable.getHitBox().getCenterY() > 0)){
            removeNode(outOfScreenTrackArrow);
            outOfScreen = false;
        }

        stop = stop || GUIHelpers.isOutOfGame(castable.getHitBox().getBoundsInLocal(), screenX, screenY);
        stop = stop || level.getPlayer1().collision(castable);
        stop = stop || level.getPlayer2().collision(castable);
        if(stop){

            // end of turn
            // remove all listeners
            removeNode(castable.getSpriteView());
            thrownCastableTimeline.stop();
            removeFilter(MouseEvent.MOUSE_RELEASED, fireReleased);
            if(!didJump){
                level.getGameScene().removeEventFilter(KeyEvent.KEY_PRESSED, toJumpMode);
            }



            aimLine.setStartX(0);
            aimLine.setStartY(0);
            aimLine.setEndX(0);
            aimLine.setEndY(0);
            removeNode(outOfScreenTrackArrow);
            outOfScreen = false;
            player1Turn = !player1Turn;
            fired = false;
            gameLoop();
        }

    }

    public void toggleJumpMpde(KeyEvent event, Player player){
        if(event.getCode() == KeyCode.J){
            if(!jumpMode){
                removeFilter(MouseEvent.MOUSE_MOVED, aimHandler);
                removeFilter(MouseEvent.MOUSE_PRESSED, firePressed);
                addFilter(MouseEvent.MOUSE_MOVED, drawJump);
                addFilter(MouseEvent.MOUSE_PRESSED, jump);
                addNode(jumpLine);
                removeNode(aimLine);
                drawJumpLine(robot.getMouseX(), player);
                jumpMode = true;
            }else if(!didJump){
                addFilter(MouseEvent.MOUSE_MOVED, aimHandler);
                addFilter(MouseEvent.MOUSE_PRESSED, firePressed);
                removeFilter(MouseEvent.MOUSE_MOVED, drawJump);
                removeFilter(MouseEvent.MOUSE_PRESSED, jump);
                removeNode(jumpLine);
                addNode(aimLine);
                jumpMode = false;
            }

        }
    }


    public void drawJumpLine(MouseEvent event, Player player){
        drawJumpLine(event.getX(), player);
    }

    //Overload for single call
    public void drawJumpLine(double mouseX, Player player){
        double jumpSpeed = 15 * pixelPerMeter;
        double x = player.getPosX();
        double y = player.getPosY();
        double sign = mouseX < player.getPosX() ? -1 : 1;
        double maxX = 11 * pixelPerMeter;
        double xlength = Math.abs(mouseX - x) > maxX ? maxX * sign : mouseX - x;
        double angle = Math.PI/2 - Math.asin(-gravity * xlength / Math.pow(jumpSpeed, 2)) / 2;
        double t = jumpSpeed * Math.sin(angle) / (-gravity) * 2;
        double ylength = jumpSpeed * Math.sin(angle) * t / 2  + gravity / 2 * Math.pow(t / 2, 2);
        double controlX = (x * 0.5 + 0.25 * xlength) * 2;
        double controlY = y - ylength * 2;

        double endY = screenY;
        double endT = (Math.sin(angle) * jumpSpeed + Math.sqrt(Math.pow(Math.sin(angle) * jumpSpeed, 2) + 2 * gravity * (y - screenY))) / (-gravity);
        double endX = x + jumpSpeed * Math.cos(angle) * endT;


        jumpLine.setStartX(x);
        jumpLine.setStartY(y);
        jumpLine.setEndX(endX);
        jumpLine.setEndY(endY);
        jumpLine.setControlX(controlX);
        jumpLine.setControlY(controlY);
        player.setVelocityX(jumpSpeed * Math.cos(angle));
        player.setVelocityY(jumpSpeed * Math.sin(angle));

        MapObject intercept = null;
        for(MapObject levelObj : getLevel().getMapObjects()){
            if(intercept == null && jumpLineIntersect(levelObj, jumpSpeed, angle)){
                intercept = levelObj;

            }else if(intercept != null && jumpLineIntersect(levelObj, jumpSpeed, angle)){
                if(intercept.getX() > levelObj.getX()){
                    intercept = levelObj;
                }
            }
        }
        if(intercept != null){
            System.out.println(intercept.getX());
        }
    }

    public void playerJump(MouseEvent event, Player player){
        if(jumping){
            event.consume();
            return;
        }
        jumping = true;
        jumpTimeLine = new Timeline(new KeyFrame(Duration.millis(1000.0/24),(e) -> animateJump(player)));
        level.getGameScene().removeEventFilter(KeyEvent.KEY_PRESSED, toJumpMode);
        removeFilter(MouseEvent.MOUSE_MOVED, drawJump);
        removeFilter(MouseEvent.MOUSE_PRESSED, jump);
        removeNode(jumpLine);
        jumpTimeLine.setCycleCount(Timeline.INDEFINITE);
        jumpTimeLine.play();
    }

    public void animateJump(Player player){
        boolean stop = false;
        player.setPosX(player.getPosX() + player.getVelocityX() / 24);
        player.setPosY(player.getPosY() - player.getVelocityY() / 24);
        player.setVelocityY(player.getVelocityY() + gravity / 24);


        for(MapObject statics : level.getBuildings()){
            stop = stop || statics.collision(player.getHitBox().getBoundsInLocal());
        }
        Player opponent = player1Turn ? level.getPlayer2() : level.getPlayer1();

        stop |= GUIHelpers.isOutOfGame(player.getHitBox().getBoundsInLocal(), screenX, screenY) || opponent.collision(player.getHitBox().getBoundsInLocal());

        if(stop){
            jumpTimeLine.stop();
            addFilter(MouseEvent.MOUSE_MOVED, aimHandler);
            addFilter(MouseEvent.MOUSE_PRESSED, firePressed);
            addNode(aimLine);
            drawAimline(robot.getMouseX(), robot.getMouseY(), player.getPosX(), player.getPosY());
            jumping = false;
        }



    }

    public boolean jumpLineIntersect(MapObject obj, double jumpSpeed, double angle){
        double objX1 = obj.getX() - jumpLine.getStartX();
        double objY1 = obj.getY() - jumpLine.getStartY();
        double objX2 = objX1 + obj.getWidth();
        double objY2 = objY1 + obj.getHeight();

        double lineY1 = objX1 * Math.tan(angle) + gravity/2 * Math.pow(objX1 / (jumpSpeed * Math.cos(angle)),2);
        double lineY2 = objX2 * Math.tan(angle) + gravity/2 * Math.pow(objX2 / (jumpSpeed * Math.cos(angle)),2);

        return (lineY2 > objY2 && lineY1 > objY1) || (lineY1 > objY1);


    }

    public void addNode(Node node){
        level.getGame().getChildren().add(node);
    }

    public void removeNode(Node node){
        level.getGame().getChildren().remove(node);
    }

    public void addFilter(EventType e, EventHandler eH){
        level.getGame().addEventFilter(e, eH);
    }

    public void removeFilter(EventType e, EventHandler eH){
        level.getGame().removeEventFilter(e, eH);
    }

    public MenuController getMenuController() {
        return menuController;
    }

    public Level getLevel(){
        return level;
    }

    public Timeline getJumpTimeLine() {
        return jumpTimeLine;
    }

    public Timeline getThrownCastableTimeline() {
        return thrownCastableTimeline;
    }

    public boolean isPlayer1Turn() { return this.player1Turn;}

    public void setPlayer1Turn(boolean player1Turn) {
        this.player1Turn = player1Turn;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public boolean isFired() {
        return fired;
    }

    public boolean isJumping() {
        return jumping;
    }

}
