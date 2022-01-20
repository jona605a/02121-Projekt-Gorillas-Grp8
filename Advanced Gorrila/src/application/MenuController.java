package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
    private Menu pauseMenu;
    private Menu optionsMenu;
    private Menu selectMenu;
    private Menu gameOverMenu;
    private Image img = new Image("/Images/MenuBackground.png");
    private BackgroundImage bgi = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.DEFAULT,new BackgroundSize(0,0,false,false,false,true));
    private Background bg = new Background(bgi);
    Music music;

    double screenY, screenX;




    MenuController(GameObject gameObject){
        this.gameObject = gameObject;
    }


    public void menuSetup(double screenWidth, double screenHeight, Stage mainStage) throws Exception{

        stage = mainStage;
        screenY = screenHeight;
        screenX = screenWidth;
        mainMenu = new Menu("Gorrilas");
        playerNamesMenu = new Menu("Change player names");
        pauseMenu = new Menu("Game Paused");
        optionsMenu = new Menu("Options");
        selectMenu = new Menu("Select Level");
        gameOverMenu = new Menu("fill later");



        // add main menu buttons

        mainMenu.createButton("Enter Game", e->goToGame());
        mainMenu.createButton("Select level", this::goToSelect);
        mainMenu.createButton("Options", this::goToOptions);
        mainMenu.createButton("Exit Game", (e) -> {stage.close();});

        // add player name menu buttons
        playerNamesMenu.createButton("Change player 1 name", this::changePlayer1Name);
        playerNamesMenu.createButton("Change player 2 name", this::changePlayer2Name);
        playerNamesMenu.createButton("Return to options", this::goToOptions);

        // setup pause menu
        pauseMenu.createButton("Resume Game", this::resumeGame);
        pauseMenu.createButton("Options", this::goToOptions);
        pauseMenu.createButton("End game", e -> gameObject.endGame());
        pauseMenu.createButton("Exit game", e -> stage.close());
        pauseMenu.menuScene.addEventFilter(KeyEvent.KEY_PRESSED, this::resumeGame);

        // setup options menu
        optionsMenu.createButton("Change player names", e -> stage.setScene(playerNamesMenu.menuScene));
        optionsMenu.createButton("Change volume", e -> createChangVolumePopUp());
        optionsMenu.createButton("Main menu", this::goToMainMenu);

        // select level menu
        selectMenu.createButton("Level 1", e -> goToSelectedLevel(gameObject.getLevel().level1));
        selectMenu.createButton("Level 2", e -> goToSelectedLevel(gameObject.getLevel().level2));
        selectMenu.createButton("Level 3", e -> goToSelectedLevel(gameObject.getLevel().level3));
        selectMenu.createButton("Main menu", this::goToMainMenu);

        // setup game over menu
        gameOverMenu.createButton("Back to main menu", e -> gameObject.endGame());
        gameOverMenu.createButton("Exit game", e -> stage.close());

        stage.setTitle("Gorillas");
        stage.getIcons().add(new Image(("/Images/Gorilla1.png")));
        music = new Music(new String[]{"/Sounds/Music/RetroFunk.mp3", "/Sounds/Music/BossTime.mp3", "/Sounds/Music/Stage3.mp3"});
        stage.setScene(mainMenu.menuScene);
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
    }



    public void goToGame() {
        gameObject.getLevel().setupLevel();
        gameObject.setPlayer1Turn(true);
        gameObject.setGameRunning(true);
        stage.setScene(gameObject.getLevel().getGameScene());
        gameObject.gameLoop();
    }

    public void resumeGame(ActionEvent event){
        if(gameObject.isJumping()){
            gameObject.getJumpTimeLine().play();
        }
        if(gameObject.isFired()){
            gameObject.getThrownCastableTimeline().play();
        }
        gameObject.getLevel().getBackgroundTimeline().play();
        stage.setScene(gameObject.getLevel().getGameScene());
    }

    public void resumeGame(KeyEvent event){
        if(gameObject.isJumping()){
            gameObject.getJumpTimeLine().play();
        }
        if(gameObject.isFired()){
            gameObject.getThrownCastableTimeline().play();
        }
        if(gameObject.isFalling()){
            gameObject.getPowerUpFallTimeline().play();
        }
        gameObject.getLevel().getBackgroundTimeline().play();
        optionsMenu.getButton(optionsMenu.getButtonCnt() - 1).setText("Return to options");
        optionsMenu.getButton(optionsMenu.getButtonCnt() - 1).setOnAction(this::goToOptions);
        stage.setScene(gameObject.getLevel().getGameScene());
    }

    public void goToMainMenu(ActionEvent event){
        stage.setScene(mainMenu.menuScene);
    }

    public void goToPause(){
        optionsMenu.getButton(optionsMenu.getButtonCnt() - 1).setOnAction((e) -> stage.setScene(pauseMenu.menuScene));
        optionsMenu.getButton(optionsMenu.getButtonCnt() - 1).setText("Return to pause screen");
        stage.setScene(pauseMenu.menuScene);
    }

    public void goToSelectedLevel(Map level) {
        gameObject.getLevel().setLevel(level);
        goToGame();
    }

    public void goToSelect(ActionEvent event){
        stage.setScene(selectMenu.menuScene);
    }

    public void goToOptions(ActionEvent event){
        stage.setScene(optionsMenu.menuScene);
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
        Label messageLabel = new Label("Enter player name and press enter");

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

    public void createChangVolumePopUp(){
        double width = screenX * 0.4, height = screenY * 0.6;
        double x = screenX * 0.3, y = screenY * 0.2;
        Stage popUp = new Stage();
        AnchorPane popUpPane = new AnchorPane();
        Scene popUpScene = new Scene(popUpPane, width, height, Color.DARKBLUE);
        Slider volumeSlider = new Slider(0,100,(int) (100 * gameObject.getVolume()));
        Button returnButton = new Button("Return");
        Label messageLabel = new Label("Enter player name and press enter");
        Label currentVolume = new Label("Volume : " + (int)(gameObject.getVolume() * 100));

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
        returnButton.setOnAction(e -> popUp.close());
        returnButton.setMinSize(width * 0.4, height * 0.2);
        returnButton.setLayoutX(width * 0.3);
        returnButton.setLayoutY(height * 0.75);
        returnButton.setFont(new Font("TR2N", 20));

        // slider setup
        volumeSlider.setMinSize(width * 0.6, height * 0.1);
        volumeSlider.setMaxSize(width * 0.6, height * 0.1);
        volumeSlider.setLayoutX(width * 0.2);
        volumeSlider.setLayoutY(height * 0.4);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setMinorTickCount(4);
        volumeSlider.setMajorTickUnit(10);
        volumeSlider.getStylesheets().add(getClass().getResource("/Slider.css").toExternalForm());
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                gameObject.setVolume(((double) newValue) / 100);
                music.changeVolume(gameObject.getVolume());
                currentVolume.setText("Volume : " + (int)(gameObject.getVolume() * 100));
            }
        });

        // volume dislay setup
        currentVolume.setMinSize(width * 0.6, height * 0.2);
        currentVolume.setMaxSize(width * 0.7, height * 0.3);
        currentVolume.setLayoutX(width * 0.15);
        currentVolume.setLayoutY(height * 0.55);
        currentVolume.setFont(new Font("TR2N", 30));
        currentVolume.setAlignment(Pos.CENTER);
        currentVolume.setTextAlignment(TextAlignment.CENTER);
        currentVolume.setTextFill(Color.WHITESMOKE);
        currentVolume.setWrapText(true);

        // scene setup
        popUpScene.getStylesheets().add(getClass().getResource("/Menu.css").toExternalForm());
        popUpPane.getChildren().add(returnButton);
        popUpPane.getChildren().add(volumeSlider);
        popUpPane.getChildren().add(messageLabel);
        popUpPane.getChildren().add(currentVolume);
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

    public Scene getMainMenuScene(){
        return mainMenu.menuScene;
    }

    public Scene getGameOverMenuScene(){
        return gameOverMenu.menuScene;
    }

    public Label getGameOverMenuTitel(){
        return gameOverMenu.menuTitle;
    }

    public Music getMusic() {
        return music;
    }

    private class Menu{
        ArrayList<Button> menuButtons;
        AnchorPane menuPane;
        double nextButtonY, middleX;
        double buttonWidth, buttonHeight;
        Label menuTitle = new Label();
        private Scene menuScene;
        private int buttonCnt = 0;

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
            menuTitle.setLayoutX((screenX - Helpers.textSize(menuTitle))/2);
            menuTitle.setLayoutY(15);
            menuTitle.setTextAlignment(TextAlignment.CENTER);
            menuPane.getChildren().add(menuTitle);





        }

        public void createButton(String text, EventHandler<ActionEvent> onClick){
            buttonCnt++;
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

        public int getButtonCnt() {
            return buttonCnt;
        }

        public Button getButton(int index){
            return menuButtons.get(index);
        }

        public Label getMenuTitle() {
            return menuTitle;
        }
    }


}
