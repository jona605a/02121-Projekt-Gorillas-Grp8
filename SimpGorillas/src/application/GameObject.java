package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurve;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Scanner;


public class GameObject {

    private Stage mainStage;
    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    private double screenX = screenBounds.getMaxX(), screenY = screenBounds.getMaxY();
    private double gravity = -9.81;
    private Level level;
    Timeline thrownCastableTimeline;
    private boolean player1Turn;
    Polygon outOfScreenTrackArrow = GUIHelpers.createArrow(0,0,10,40, Math.PI);
    private boolean outOfScreen = false;
    int player1_points;
    int player2_points;
    Scanner console = new Scanner(System.in);

    GameObject(Stage mainStage, double width, double height) throws Exception {

        this.mainStage = mainStage;
        mainStage.setTitle("Gorillas");
        mainStage.setResizable(false);
        mainStage.show();

        player1Turn = true;
        outOfScreenTrackArrow.setFill(Color.YELLOW);
        outOfScreenTrackArrow.setStroke(Color.BLACK);
        level = new Level(width, height);
        screenX = width;
        screenY = height;
    }

    public void start_game(){
        mainStage.setScene(level.getGameScene());
        //System.out.println("Entering gameloop...");
        gameLoop();
    }


    public void gameLoop(){
        if(player1Turn){
            playerTurn(level.getPlayer1(), console);
        }else{
            playerTurn(level.getPlayer2(), console);
        }
        player1Turn = !player1Turn;
    }

    public void playerTurn(Player player, Scanner console) {

        System.out.println(player.getName()+"'s turn!");
        System.out.print("Please enter an angle to shoot: ");
        int angle = inputInt(console, 0, 90);
        System.out.print("\nPlease enter a velocity: ");
        int v = inputInt(console, 1, 25);

        fireCastable(player, angle, v);
    }

    public static int inputInt(Scanner console, int min, int max) {
        int n;
        while (true) {
            if (console.hasNextInt()) {
                n = console.nextInt();
                if (n >= min && n <= max){
                    break;
                } else {
                    System.out.print("Please enter an integer between "+min+" and "+max+": ");
                }
            } else {
                System.out.print("Please enter an integer between "+min+" and "+max+": ");
                console.next();
            }
        }
        return n;
    }

    public void fireCastable(Player player, double angle, int v) {
        Sound.play("/Sounds/Throw.mp3");

        Castable selectedCastable = player.getSelectedCastable();
        thrownCastableTimeline = new Timeline(new KeyFrame(Duration.millis(1000.0/24),(e) -> {animateCastable(selectedCastable);}));
        thrownCastableTimeline.setCycleCount(Timeline.INDEFINITE);

        if (player1Turn) {
            angle = angle/180*Math.PI;
        } else {
            angle = (180-angle)/180*Math.PI;
        }

        selectedCastable.setVelocityX(Math.cos(angle)*v);
        selectedCastable.setVelocityY(Math.sin(angle)*v);
        selectedCastable.setX(player.getPosX() + (player1Turn ? 15 : -15));//+ Math.cos(angle)*aimlineMinRadius);
        selectedCastable.setY(player.getPosY() - 15);//- Math.sin(angle)*aimlineMinRadius);

        //level.getGame().getChildren().add(selectedCastable.getSpriteView());
        level.getGame().getChildren().add(selectedCastable.getCircle());

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
        //stop = stop || level.getPlayer1().collision(castable.getCircle().getBoundsInLocal());
        //stop = stop || level.getPlayer2().collision(castable.getCircle().getBoundsInLocal());
        if(stop){
            //level.getGame().getChildren().remove(castable.getSpriteView());
            level.getGame().getChildren().remove(castable.getCircle());

            double dx1 = level.getPlayer1().getPosX() - castable.getX();
            double dx2 = level.getPlayer2().getPosX() - castable.getX();
            double dy1 = level.getPlayer1().getPosY() - castable.getY();
            double dy2 = level.getPlayer2().getPosY() - castable.getY();
            //System.out.printf("D p1: %f, %f, %f.   D p2: %f, %f, %f\n",dx1,dy1,Math.sqrt(dx1*dx1 + dy1*dy1),dx2,dy2,Math.sqrt(dx2*dx2 + dy2*dy2));
            if (Math.sqrt(dx1*dx1 + dy1*dy1) <= screenX/50) {
                player2_points++;
                System.out.printf(level.getPlayer2().getName() + " hit! You now have %d points.\n", player2_points);
            }
            if (Math.sqrt(dx2*dx2 + dy2*dy2) <= screenX/50) {
                player1_points++;
                System.out.printf(level.getPlayer1().getName() + " hit! You now have %d points.\n", player1_points);
            }


            thrownCastableTimeline.stop();
            //level.getGame().removeEventFilter(MouseEvent.MOUSE_RELEASED, fireReleased);

            level.getGame().getChildren().remove(outOfScreenTrackArrow);
            outOfScreen = false;
            gameLoop();
        }

    }

    public Level getLevel(){
        return level;
    }

    public boolean isPlayer1Turn() { return this.player1Turn;}

    public void setPlayer1Turn(boolean player1Turn) {
        this.player1Turn = player1Turn;
    }
}
