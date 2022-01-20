package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventType;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.EventHandler;

import java.util.Random;


public class GameObject {


    private MenuController menuController = new MenuController(this);
    private Stage mainStage;
    private boolean muted = false;
    private double volume = 0.2;
    private double prevVolume;
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
    Timeline powerUpFallTimeline = new Timeline();
    private boolean jumping;
    private boolean falling;

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
    private EventHandler<KeyEvent> changePowerUps;
    private EventHandler<KeyEvent> usePowerUp;
    private EventHandler<KeyEvent> changeCastable;
    Robot robot = new Robot();
    Random r = new Random();
    Polygon outOfScreenTrackArrow = Helpers.createArrow(0,0,10,40, Math.PI);
    private int aimlineMinRadius = 35;
    private int aimlineMaxRadius = 100;
    private int turnsTillPowerUp = 0;
    private double powerUpVelocity = 0;


    GameObject(Stage mainStage) throws Exception {
        this.mainStage = mainStage;
        player1Turn = true;
        gameRunning = false;
        jumpMode = false;
        fired = false;
        jumping = false;
        falling = false;
        outOfScreenTrackArrow.setFill(Color.YELLOW);
        outOfScreenTrackArrow.setStroke(Color.BLACK);
        jumpLine.setFill(Color.TRANSPARENT);
        jumpLine.setStroke(Color.BLACK);
        enterPause = event -> enterPauseMenu(event);
        level.getGameScene().addEventFilter(KeyEvent.KEY_PRESSED, enterPause);
        mainStage.addEventFilter(KeyEvent.KEY_PRESSED, (e) -> toggleMute(e));

    }


    public void enterMenu() throws Exception {
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
            if(falling){
                powerUpFallTimeline.pause();
            }
            level.getBackgroundTimeline().pause();
            menuController.goToPause();
        }
    }

    public boolean gameOver(){
        return level.getPlayer2().getHitPoints() <= 0 || level.getPlayer1().getHitPoints() <= 0;
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
        falling = false;

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
                volume = prevVolume;
                menuController.getMusic().changeVolume(volume);
            }else{
                muted = true;
                prevVolume = volume;
                volume = menuController.getMusic().getCurrentSong().getMediaPlayer().getVolume();
                volume = 0;
                menuController.getMusic().changeVolume(0);
            }
        }

    }

    public void gameLoop(){
        level.getGame().getChildren().add(aimLine);
        if(turnsTillPowerUp == 0){
            powerUpVelocity = 0;
            turnsTillPowerUp = 2 + r.nextInt(3);
            double x = 50 + r.nextDouble(screenX - 100);
            double y = 0;
            int radius = 15;
            int powR = r.nextInt(6);
            PowerUp p = switch (powR) {
                case 0 -> (new FastBulletPowerUp(x, y, radius));
                case 1 -> (new SlowBulletPowerUp(x, y, radius));
                case 2 -> (new ExtraAmmoPowerUp(x, y, radius));
                case 3 -> (new ExtraHPPowerUp(x, y, radius)); //Missing sprite
                case 4 -> (new PowerfulBulletPowerUp(x, y, radius));
                case 5 -> (new ExtraTurnPowerUp(x, y, radius));
                default -> new ExtraAmmoPowerUp(x,y,radius);
            };
            addNode(p.getSprite());
            level.getPowerUps().add(p);
            falling = true;
            powerUpFallTimeline = new Timeline(new KeyFrame(Duration.millis(1000.0/24),e -> animatePowerUp(p)));
            powerUpFallTimeline.setCycleCount(-1);
            powerUpFallTimeline.play();
        }else{
            turnsTillPowerUp--;
        }
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
        toJumpMode = event -> toggleJumpMode(event, player);
        drawJump = event -> drawJumpLine(event, player);
        jump = event -> playerJump(event, player);
        changeCastable = event -> changeAmmoType(event, player);
        usePowerUp = event -> usePUp(event, player);
        changePowerUps = event -> changePowerUp(event, player);
        jumpMode = false;
        didJump =  false;


        addFilter(MouseEvent.MOUSE_MOVED, aimHandler);
        addFilter(MouseEvent.MOUSE_PRESSED, firePressed);
        addSceneFilter(KeyEvent.KEY_PRESSED, changeCastable);
        addSceneFilter(KeyEvent.KEY_PRESSED, toJumpMode);
        addSceneFilter(KeyEvent.KEY_PRESSED, usePowerUp);
        addSceneFilter(KeyEvent.KEY_PRESSED, changePowerUps);



    }

    public void animatePowerUp(PowerUp pu){
        boolean stop = false;
        pu.setY(pu.getY() - powerUpVelocity);
        powerUpVelocity += gravity / 100;
        for(MapObject obj : level.getMapObjects()){
            stop = stop || pu.collision(obj.getShape().getLayoutBounds());
        }
        if(pu.collision(level.getPlayer1().getHitBox().getLayoutBounds())){
            stop = true;
            int removeIndex = level.getPowerUps().indexOf(pu);
            removeNode(pu.getSprite());
            if(pu instanceof ExtraTurnPowerUp){
                ((ExtraTurnPowerUp) pu).onUse(this);
            }else{
                level.getPlayer1().addPowerUp(pu);
            }
            level.getPowerUps().remove(removeIndex);
        }
        if(pu.collision(level.getPlayer2().getHitBox().getLayoutBounds())){
            stop = true;
            int removeIndex = level.getPowerUps().indexOf(pu);
            removeNode(pu.getSprite());
            if(pu instanceof ExtraTurnPowerUp){
                ((ExtraTurnPowerUp) pu).onUse(this);
            }else{
                level.getPlayer1().addPowerUp(pu);
            }
            level.getPowerUps().remove(removeIndex);
        }
        if(stop){
            powerUpFallTimeline.stop();
        }
    }

    public void changePowerUp(KeyEvent event, Player player){
        if(event.getCode() == KeyCode.S){
            player.switchPowerUp();
        }
    }

    public void usePUp(KeyEvent event, Player player){
        if(event.getCode() == KeyCode.U){
            player.usePowerUp();
        }
    }

    public void changeAmmoType(KeyEvent event, Player player){
        if(event.getCode() == KeyCode.A){
            player.switchCastable();
        }
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
        removeSceneFilter(KeyEvent.KEY_PRESSED, enterPause);
        removeSceneFilter(KeyEvent.KEY_PRESSED, toJumpMode);
        removeSceneFilter(KeyEvent.KEY_PRESSED, changeCastable);
        removeSceneFilter(KeyEvent.KEY_PRESSED, usePowerUp);
        double angle = Helpers.getAngleOfLine(player.getPosX(), player.getPosY(), event.getX(), event.getY());
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
        player.fireCastable();
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
        Sound.play("/Sounds/Throw.mp3", volume);
        thrownCastableTimeline.play();
    }

    public void animateCastable(Castable castable){
        boolean stop = false;
        castable.setX(castable.getX() + castable.getVelocityX() / 24);
        castable.setY(castable.getY() - castable.getVelocityY() / 24);
        castable.setVelocityY(castable.getVelocityY() + gravity / 24);
        for(MapObject obj : level.getMapObjects()){
            stop = stop || obj.collision(castable.getHitBox().getLayoutBounds());
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

        stop = stop || Helpers.isOutOfGame(castable.getHitBox().getBoundsInLocal(), screenX, screenY);
        stop = stop || level.getPlayer1().collision(castable);
        stop = stop || level.getPlayer2().collision(castable);
        if(stop){

            // end of turn
            // remove all listeners
            removeNode(castable.getSpriteView());
            thrownCastableTimeline.stop();
            removeFilter(MouseEvent.MOUSE_RELEASED, fireReleased);
            if(!didJump){
                removeSceneFilter(KeyEvent.KEY_PRESSED, toJumpMode);
                removeSceneFilter(KeyEvent.KEY_PRESSED, changeCastable);
                removeSceneFilter(KeyEvent.KEY_PRESSED, usePowerUp);
                removeSceneFilter(KeyEvent.KEY_PRESSED, changePowerUps);
            }



            aimLine.setStartX(0);
            aimLine.setStartY(0);
            aimLine.setEndX(0);
            aimLine.setEndY(0);
            removeNode(outOfScreenTrackArrow);
            outOfScreen = false;
            player1Turn = !player1Turn;
            fired = false;
            if(!gameOver()){
                gameLoop();
            }else{
                mainStage.setScene(menuController.getGameOverMenuScene());
                Player winner = level.getPlayer1().getHitPoints() <= 0 ? level.getPlayer2() : level.getPlayer1();
                Label tmp = menuController.getGameOverMenuTitel();
                tmp.setText(winner.getName() + " wins!");
                tmp.setLayoutX((screenX - Helpers.textSize(tmp))/2);
                menuController.getGameOverMenuTitel().setTextAlignment(TextAlignment.CENTER);

            }

        }

    }

    public void toggleJumpMode(KeyEvent event, Player player){
        if(event.getCode() == KeyCode.J){
            if(!jumpMode){
                removeFilter(MouseEvent.MOUSE_MOVED, aimHandler);
                removeFilter(MouseEvent.MOUSE_PRESSED, firePressed);
                removeSceneFilter(KeyEvent.KEY_PRESSED, changeCastable);
                removeSceneFilter(KeyEvent.KEY_PRESSED, usePowerUp);
                removeSceneFilter(KeyEvent.KEY_PRESSED, changePowerUps);
                addFilter(MouseEvent.MOUSE_MOVED, drawJump);
                addFilter(MouseEvent.MOUSE_PRESSED, jump);
                addNode(jumpLine);
                removeNode(aimLine);
                drawJumpLine(robot.getMouseX(), player);
                jumpMode = true;
            }else if(!didJump){
                addFilter(MouseEvent.MOUSE_MOVED, aimHandler);
                addFilter(MouseEvent.MOUSE_PRESSED, firePressed);
                addSceneFilter(KeyEvent.KEY_PRESSED, changeCastable);
                addSceneFilter(KeyEvent.KEY_PRESSED, usePowerUp);
                addSceneFilter(KeyEvent.KEY_PRESSED, changePowerUps);
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


        for(MapObject mapObject : level.getMapObjects()){
            stop = stop || mapObject.collision(player.getHitBox().getBoundsInLocal());
        }
        int removeIndex = -1;
        for(PowerUp p : level.getPowerUps()){
            if(p.collision(player.getHitBox().getLayoutBounds())){

                removeIndex = level.getPowerUps().indexOf(p);
                removeNode(p.getSprite());
                if(p instanceof ExtraTurnPowerUp){
                    ((ExtraTurnPowerUp) p).onUse(this);
                }else{
                    player.addPowerUp(p);
                }
            }
        }
        if(removeIndex > -1){
            level.getPowerUps().remove(removeIndex);
        }


        stop |= Helpers.isOutOfGame(player.getHitBox().getBoundsInLocal(), screenX, screenY);

        if(stop){
            jumpTimeLine.stop();
            addFilter(MouseEvent.MOUSE_MOVED, aimHandler);
            addFilter(MouseEvent.MOUSE_PRESSED, firePressed);
            addSceneFilter(KeyEvent.KEY_PRESSED, changeCastable);
            addSceneFilter(KeyEvent.KEY_PRESSED, usePowerUp);
            addSceneFilter(KeyEvent.KEY_PRESSED, changePowerUps);
            addNode(aimLine);
            drawAimline(robot.getMouseX(), robot.getMouseY(), player.getPosX(), player.getPosY());
            jumping = false;
        }



    }

    public boolean jumpLineIntersect(MapObject obj, double jumpSpeed, double angle){


        double objX1 = obj.getX() - jumpLine.getStartX();
        objX1 = obj.getX() < jumpLine.getStartX() ? jumpLine.getStartX() : objX1;
        double objY1 = obj.getY() - jumpLine.getStartY();
        double objX2 = obj.getX() + obj.getWidth();
        double objY2 = objY1 - obj.getHeight();

        if(angle > Math.PI / 2){

        }

        double lineY1 = objX1 * Math.tan(angle) + gravity/2 * Math.pow(objX1 / (jumpSpeed * Math.cos(angle)),2);
        double lineY2 = objX2 * Math.tan(angle) + gravity/2 * Math.pow(objX2 / (jumpSpeed * Math.cos(angle)),2);

        boolean onlyForwardCheck = ((lineY1 > lineY2) || lineY1 > 0);


        return !((lineY2 > objY1 && lineY1 > objY1) || (lineY2 < objY2 && lineY1 < objY2)) && ((lineY1 > lineY2) || lineY1 > 0);


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

    public void addSceneFilter(EventType e, EventHandler eH){
        level.getGameScene().addEventFilter(e, eH);
    }

    public void removeSceneFilter(EventType e, EventHandler eH){
        level.getGameScene().removeEventFilter(e, eH);
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

    public Timeline getPowerUpFallTimeline() {
        return powerUpFallTimeline;
    }

    public double getVolume() {
        return volume;
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

    public boolean isFalling() {
        return falling;
    }

    public void setVolume(double volume) {
        if(muted){
            prevVolume = volume;
        }else{
            this.volume = volume;
        }

    }
}
