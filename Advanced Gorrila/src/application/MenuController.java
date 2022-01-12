package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MenuController {

    Stage stage;
    GameObject gameObject;
    private AnchorPane menu;
    private Scene menuScene;
    Font menuFont  = Font.loadFont(getClass().getResourceAsStream("/Fonts/Tr2n.ttf"), 14);
    private Menu mainMenu;

    double height, width;




    MenuController(GameObject gameObject){
        this.gameObject = gameObject;
    }


    public void menuSetup(double screenWidth, double screenHeight, Stage mainStage){

        stage = mainStage;
        height = screenHeight;
        width = screenWidth;
        menu = new AnchorPane();
        menuScene = new Scene(menu);
        mainMenu = new Menu("Gorrilas");

        // add background
        Image img = new Image("/Images/MenuBackground.png");
        BackgroundImage bgi = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.DEFAULT,new BackgroundSize(0,0,false,false,false,true));
        Background bg = new Background(bgi);
        menu.setBackground(bg);
        menuScene.getStylesheets().add(getClass().getResource("/Menu.css").toExternalForm());



        // add menu buttons

        mainMenu.createButton("Enter Game", this::goToGame);
        mainMenu.createButton("Enter Game 2", this::goToGame);
        mainMenu.addAllToGraph(menu);



        stage.setScene(menuScene);
        stage.setMaximized(true);
    }



    public void goToGame(ActionEvent event){
        gameObject.getLevel().setupLevel(); // does nothing?
        gameObject.setPlayer1Turn(true);
        gameObject.setGameRunning(true);
        stage.setScene(gameObject.getLevel().getGameScene());
        stage.setMaximized(true);
        gameObject.gameLoop();
    }

    private class Menu{
        ArrayList<MenuButton> menuButtons;
        double nextButtonY, middleX;
        double buttonWidth, buttonHeight;
        Label menuTitle = new Label();
        Menu(String title){
            menuTitle.setFont(new Font("TR2N",100));
            menuTitle.setTextFill(Color.web("#ff7b00"));
            menuTitle.setText(title);
            menuTitle.setLayoutX((width - GUIHelpers.textSize(menuTitle))/2);
            menuTitle.setLayoutY(15);
            menuTitle.setTextAlignment(TextAlignment.CENTER);

            menuButtons = new ArrayList<>();
            nextButtonY = 0.25 * height;
            middleX = width / 2;
            buttonWidth = width * 0.2;
            buttonHeight = height * 0.1;

        }

        public void createButton(String text, EventHandler<ActionEvent> onClick){
            MenuButton button = new MenuButton(text, onClick);
            button.button.setMinSize(buttonWidth, buttonHeight);
            button.button.setLayoutX(middleX - button.getWidth() / 2);
            button.button.setLayoutY(nextButtonY);
            menuButtons.add(button);
            nextButtonY += buttonHeight * 1.5;
            button.button.setFont(new Font("TR2n", 30));
            button.button.setTextFill(Color.WHITESMOKE);
        }

        public Button getButton(int index){
            return menuButtons.get(index).button;
        }
        public void addAllToGraph(AnchorPane anchor){
            anchor.getChildren().add(menuTitle);
            for(MenuButton button : menuButtons){
                anchor.getChildren().add(button.button);
            }
        }
    }

    private class MenuButton{
        Button button;

        MenuButton(String text, EventHandler<ActionEvent> onClick){
            button = new Button(text);
            button.setOnAction(onClick);
            button.setWrapText(true);

        }

        double getWidth(){
            System.out.println(GUIHelpers.textSize(button));
            return GUIHelpers.textSize(button);
        }
    }

}
