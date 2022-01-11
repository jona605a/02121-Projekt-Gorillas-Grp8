package application;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class GameObject {


    private MenuController menuController = new MenuController(this);
    private Stage mainStage;
    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    private double screenX = screenBounds.getMaxX(), screenY = screenBounds.getMaxY();
    private double gravity = 9.81;
    private Level level = new Level(screenX, screenY);

    Line line = new Line(0,0,1,1);

    GameObject(Stage mainStage){
        this.mainStage = mainStage;
    }


    public void enterMenu(){
        menuController.menuSetup(screenX, screenY, mainStage);
    }



    public MenuController getMenuController() {
        return menuController;
    }

    public Level getLevel(){
        return level;
    }
}
