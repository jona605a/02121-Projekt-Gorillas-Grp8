package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    QuadCurve quad;

    double screenY, screenX;




    MenuController(GameObject gameObject){
        this.gameObject = gameObject;
    }


    public void menuSetup(double screenWidth, double screenHeight, Stage mainStage){

        stage = mainStage;
        screenY = screenHeight;
        screenX = screenWidth;
        mainMenu = new Menu("Gorrilas");
        playerNamesMenu = new Menu("Change player names");
        quad = new QuadCurve();

        quad.setStartX(0.0f);
        quad.setStartY(50.0f);
        quad.setEndX(50.0f);
        quad.setEndY(50.0f);
        quad.setControlX(25.0f);
        quad.setControlY(0.0f);
        quad.setFill(Color.TRANSPARENT);
        quad.setStroke(Color.BLACK);
        quad.setOpacity(0.5);

        mainMenu.menuPane.getChildren().add(quad);



        // add main menu buttons

        mainMenu.createButton("Enter Game", this::goToGame);
        mainMenu.createButton("Change player names", this::goToPlayerNameMenu);
        mainMenu.createButton("Select level", this::goToGame);

        // add player name menu buttons
        playerNamesMenu.createButton("Change player 1 name", this::changePlayer1Name);
        playerNamesMenu.createButton("Change player 2 name", this::changePlayer2Name);
        playerNamesMenu.createButton("Return to main menu", this::goToMainMenu);

        stage.setTitle("Gorillas");
        stage.getIcons().add(new Image(("/Images/Gorilla1.png")));
        Music music = new Music(new String[]{"/Sounds/RetroFunk.mp3", "/Sounds/BossTime.mp3"});
        stage.setScene(mainMenu.menuScene);
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
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

    public void changePlayer1Name(ActionEvent event) {createChangePlayerNamePopUp(gameObject.getLevel().getPlayer1());}

    public void changePlayer2Name(ActionEvent event) {createChangePlayerNamePopUp(gameObject.getLevel().getPlayer2());}

    public void createChangePlayerNamePopUp(Player player){
        double width = screenX * 0.4, height = screenY * 0.6;
        double x = screenX * 0.3, y = screenY * 0.2;
        Stage popUp = new Stage();
        AnchorPane popUpPane = new AnchorPane();
        Scene popUpScene = new Scene(popUpPane, width, height, Color.DARKBLUE);
        TextField playerNameChange = new TextField(player.getName());
        Button cancelButton = new Button("Cancel");
        Label messageLabel = new Label("Enter player name");

        // message label setup
        messageLabel.setMinSize(width * 0.6, height * 0.2);
        messageLabel.setMaxSize(width * 0.7, height * 0.3);
        messageLabel.setLayoutX(width * 0.15);
        messageLabel.setLayoutY(height * 0.1);
        messageLabel.setFont(new Font("TR2N", 30));
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setTextAlignment(TextAlignment.CENTER);
        messageLabel.setTextFill(Color.WHITESMOKE);
        messageLabel.setWrapText(true);

        // cancel button setup
        cancelButton.setOnAction(e -> popUp.close());
        cancelButton.setMinSize(width * 0.4, height * 0.2);
        cancelButton.setLayoutX(width * 0.3);
        cancelButton.setLayoutY(height * 0.75);
        cancelButton.setFont(new Font("TR2N", 20));

        // textField setup
        playerNameChange.setOnAction(e -> {changePlayerName(e, player, messageLabel);});
        playerNameChange.setMinSize(width * 0.5, height * 0.2);
        playerNameChange.setLayoutX(width * 0.25);
        playerNameChange.setLayoutY(height * 0.4);
        playerNameChange.setFont(new Font("TR2N",20));
        playerNameChange.setId("popUpText");

        // scene setup
        popUpScene.getStylesheets().add(getClass().getResource("/Menu.css").toExternalForm());
        popUpPane.getChildren().add(cancelButton);
        popUpPane.getChildren().add(playerNameChange);
        popUpPane.getChildren().add(messageLabel);
        popUpPane.setId("popUpPane");

        // stage setup
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.initStyle(StageStyle.UNDECORATED);
        popUp.setScene(popUpScene);
        popUp.setX(x);
        popUp.setY(y);
        popUp.show();



    }

    public void changePlayerName(ActionEvent event, Player player, Label label){
        TextField input = (TextField) event.getSource();
        if(input.getText().length() > 20){
            label.setText("Name must be 20 characters or less");
        }else if(input.getText().length() < 4){
            label.setText("Name must at least 4 characters");
        }else{
            player.setName(input.getText());
            Stage temp = (Stage) input.getParent().getScene().getWindow();
            temp.close();
        }
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
            menuScene = new Scene(menuPane, screenX, screenY);
            menuButtons = new ArrayList<>();
            nextButtonY = 0.25 * screenY;
            middleX = screenX / 2;
            buttonWidth = screenX * 0.3;
            buttonHeight = screenY * 0.1;

            // set background
            menuPane.setBackground(bg);

            // set css
            menuScene.getStylesheets().add(getClass().getResource("/Menu.css").toExternalForm());

            // setup title
            menuTitle.setFont(new Font("TR2N",100));
            menuTitle.setTextFill(Color.web("#ff7b00"));
            menuTitle.setText(title);
            menuTitle.setLayoutX((screenX - GUIHelpers.textSize(menuTitle))/2);
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
        }

        public Button getButton(int index){
            return menuButtons.get(index);
        }


    }

}
