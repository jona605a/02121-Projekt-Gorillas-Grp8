package application;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;


import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
        button.setOnAction(this::goToLevel);
        menu.getChildren().add(button);




        stage.setScene(menuScene);
        stage.setMaximized(true);
    }


    public void moveTitle(ActionEvent event){
        menuTitle.setLayoutX(menuTitle.getLayoutX() + 10);
    }


    public void goToLevel(ActionEvent event){
        stage.setScene(gameObject.getLevel().getGameScene());
        stage.setMaximized(true);
    }

}
