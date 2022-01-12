package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    private Stage stage;
    private GameObject gameObject;
    private Font menuFont  = Font.loadFont(getClass().getResourceAsStream("/Fonts/Tr2n.ttf"), 14);
    private Menu mainMenu;
    private Menu playerNamesMenu;
    private Image img = new Image("/Images/MenuBackground.png");
    private BackgroundImage bgi = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.DEFAULT,new BackgroundSize(0,0,false,false,false,true));
    private Background bg = new Background(bgi);

    double height, width;




    MenuController(GameObject gameObject){
        this.gameObject = gameObject;
    }


    public void menuSetup(double screenWidth, double screenHeight, Stage mainStage){

        stage = mainStage;
        height = screenHeight;
        width = screenWidth;
        mainMenu = new Menu("Gorrilas");
        playerNamesMenu = new Menu("Change player names");








        // add main menu buttons

        mainMenu.createButton("Enter Game", this::goToGame);
        mainMenu.createButton("Change player names", this::goToPlayerNameMenu);
        mainMenu.createButton("Select level", this::goToGame);

        // add player name menu buttons
        playerNamesMenu.createButton("Return to main menu", this::goToMainMenu);

        stage.setTitle("Gorillas");
        stage.getIcons().add(new Image(("/Images/Gorilla1.png")));
        stage.setScene(mainMenu.menuScene);
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
        System.out.println(mainMenu.getButton(0).getWidth());
        System.out.println(mainMenu.getButton(0).getHeight());
        System.out.println(mainMenu.getButton(1).getHeight());
    }



    public void goToGame(ActionEvent event){
        gameObject.getLevel().setupLevel(); // does nothing?
        gameObject.setPlayer1Turn(true);
        gameObject.setGameRunning(true);
        stage.setScene(gameObject.getLevel().getGameScene());
        gameObject.gameLoop();
    }

    public void goToMainMenu(ActionEvent event){
        stage.setScene(mainMenu.menuScene);

    }

    public void goToPlayerNameMenu(ActionEvent event){
        stage.setScene(playerNamesMenu.menuScene);
    }


    private class Menu{
        ArrayList<Button> menuButtons;
        AnchorPane menuPane;
        double nextButtonY, middleX;
        double buttonWidth, buttonHeight;
        Label menuTitle = new Label();
        private Scene menuScene;

        Menu(String title){
            menuPane = new AnchorPane();
            menuScene = new Scene(menuPane, width, height);
            menuButtons = new ArrayList<>();
            nextButtonY = 0.25 * height;
            middleX = width / 2;
            buttonWidth = width * 0.3;
            buttonHeight = height * 0.1;

            // set background
            menuPane.setBackground(bg);

            // set css
            menuScene.getStylesheets().add(getClass().getResource("/Menu.css").toExternalForm());

            // setup title
            menuTitle.setFont(new Font("TR2N",100));
            menuTitle.setTextFill(Color.web("#ff7b00"));
            menuTitle.setText(title);
            menuTitle.setLayoutX((width - GUIHelpers.textSize(menuTitle))/2);
            menuTitle.setLayoutY(15);
            menuTitle.setTextAlignment(TextAlignment.CENTER);
            menuPane.getChildren().add(menuTitle);





        }

        public void createButton(String text, EventHandler<ActionEvent> onClick){
            Button button = new Button(text);
            menuPane.getChildren().add(button);

            button.setOnAction(onClick);
            button.setWrapText(true);

            button.setMinSize(buttonWidth, buttonHeight);
            button.setMaxWidth(buttonWidth);
            button.setLayoutX(middleX - buttonWidth / 2);
            button.setLayoutY(nextButtonY);
            menuButtons.add(button);
            nextButtonY += buttonHeight * 1.5;
            button.setFont(new Font("TR2n", 20));
            button.setTextFill(Color.WHITESMOKE);
            System.out.println(GUIHelpers.textSize(button));
        }

        public Button getButton(int index){
            return menuButtons.get(index);
        }


    }

}
