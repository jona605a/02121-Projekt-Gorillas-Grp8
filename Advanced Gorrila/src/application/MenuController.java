package application;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MenuController {

    Stage stage;
    GameObject gameObject;
    private AnchorPane menu;
    private Scene menuScene;
    Font menuFont  = Font.loadFont(getClass().getResourceAsStream("/Fonts/Tr2n.ttf"), 14);
    Button button = new Button();
    double height, width;

    Label menuTitle = new Label();

    MenuController(GameObject gameObject){
        this.gameObject = gameObject;
    }


    public void menuSetup(double screenWidth, double screenHeight, Stage mainStage){

        stage = mainStage;
        height = screenHeight;
        width = screenWidth;
        menu = new AnchorPane();
        menuScene = new Scene(menu);

        // add background
        Image img = new Image("/Images/MenuBackground.png");
        BackgroundImage bgi = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.DEFAULT,new BackgroundSize(0,0,false,false,false,true));
        Background bg = new Background(bgi);
        menu.setBackground(bg);


        // add title
        menuTitle.setFont(new Font("TR2N",60));
        menuTitle.setTextFill(Color.web("#ff7b00"));
        menuTitle.setText("Gorillas");
        menuTitle.setLayoutX((width - GUIHelpers.textSize(menuTitle))/2);
        menuTitle.setLayoutY(15);
        menuTitle.setTextAlignment(TextAlignment.CENTER);
        menu.getChildren().add(menuTitle);

        // add button
        button.setLayoutX(width/2);
        button.setLayoutY(100);
        button.setText("Ligegyldigt");
        button.setOnAction(this::goToGame);
        menu.getChildren().add(button);




        stage.setScene(menuScene);
        stage.setMaximized(true);
    }


    public void moveTitle(ActionEvent event){
        menuTitle.setLayoutX(menuTitle.getLayoutX() + 10);
    }


    public void goToGame(ActionEvent event){
        gameObject.getLevel().setupLevel();
        gameObject.setPlayer1Turn(true);
        gameObject.setGameRunning(true);
        stage.setScene(gameObject.getLevel().getGameScene());
        stage.setMaximized(true);
        gameObject.gameLoop();
    }

}
