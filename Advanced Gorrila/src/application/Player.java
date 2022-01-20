package application;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class Player {
    private double hitPoints;
    private String name;
    private double posX, posY;
    private double velocityX, velocityY;
    private ArrayList<Castable> castables = new ArrayList<Castable>();
    private Rectangle hitBox = new Rectangle();
    private Castable selectedCastable;
    private Label nameLabel;
    private ImageView spriteView;
    private ProgressBar healthBar;
    private ArrayList<PowerUp> powerUps;
    private PowerUp selectedPowerUp;
    private int numOfCoconuts;
    private int selectedIndex = 0;
    private Image redX = new Image("/Images/X.png");
    private ImageView powerUpImage = new ImageView(redX);
    private ImageView selectedAmmo = new ImageView();

    public Image gorilla1 = new Image("/Images/Gorilla1.png", 58, 58, true, false);
    public Image gorilla2 = new Image("/Images/Gorilla2.png", 58, 58, true, false);
    public Image gorilla3 = new Image("/Images/Gorilla3.png", 58, 58, true, false);


    Player(double hp, String name, double x, double y, double screenY){

        this.hitPoints = hp;
        this.name = name;
        posX = x;
        posY = y - gorilla1.getHeight() / 2;
        velocityX = 0;
        velocityY = 0;
        castables.add(new Banana(x, y));
        hitBox.setHeight(gorilla1.getHeight());
        hitBox.setWidth(0.8 * gorilla1.getWidth());
        hitBox.setX(x - gorilla1.getWidth() * 0.4);
        hitBox.setY(y - gorilla1.getHeight());
        selectedCastable = new Banana(posX, posY);
        powerUps = new ArrayList<>();

        selectedAmmo.setImage(selectedCastable.getSpriteView().getImage());

        numOfCoconuts = 10;
        // Adding coconuts as ammo
        addCoconuts(numOfCoconuts);

        // Display player name
        nameLabel = new Label();
        nameLabel.setFont(new Font("Verdana", 16));
        nameLabel.setText(this.name);
        nameLabel.setLayoutX(posX - GUIHelpers.textSize(nameLabel)/2);
        nameLabel.setLayoutY(posY - (gorilla1.getHeight()/2 + 25));
        nameLabel.setTextAlignment(TextAlignment.CENTER);

        // Set player sprite
        spriteView = new ImageView();
        spriteView.setPreserveRatio(true);
        setSprite(gorilla1);

        // player healthbar
        healthBar = new ProgressBar(1);
        healthBar.setMinSize(60,15);
        healthBar.setMaxSize(60, 15);
        healthBar.setLayoutX(x - 30);
        healthBar.setLayoutY(y - gorilla1.getHeight() - 40);
        healthBar.setStyle("-fx-accent: green");

        // ammo and powerup views
        selectedAmmo.setPreserveRatio(true);
        selectedAmmo.setFitWidth(50);
        selectedAmmo.setFitWidth(50);
        selectedAmmo.setX(x - selectedAmmo.getFitWidth() - 5);
        selectedAmmo.setY(screenY - 100);



        powerUpImage.setX(x + 5);
        powerUpImage.setY(screenY - 100);
        powerUpImage.setPreserveRatio(true);
        powerUpImage.setFitWidth(50);
        powerUpImage.setFitWidth(50);


    }

    public void addNodes(AnchorPane pane){
        pane.getChildren().add(nameLabel);
        pane.getChildren().add(healthBar);
        pane.getChildren().add(spriteView);
        pane.getChildren().add(powerUpImage);
        pane.getChildren().add(selectedAmmo);
    }

    public boolean collision(double x, double y){
        return hitBox.contains(x, y);
    }

    boolean collision(Bounds localBounds) {
        return hitBox.intersects(localBounds);
    }

    boolean collision(Castable castable){
        if(hitBox.intersects(castable.getHitBox().getLayoutBounds())){
            applyDamage(castable.getDamage());
            return true;
        }else{
            return false;
        }
    }

    public void addCoconuts(int amount){
        for(int i = 0; i < amount; i++){
            castables.add(new Coconut(posX, posY));
        }
    }

    public void addPowerUp(PowerUp p){
        powerUps.add(p);
        System.out.println(powerUps.size());
        if(powerUps.size() == 1){
            selectedPowerUp = p;
            powerUpImage.setImage(p.getSprite().getImage());
        }
    }

    public void setSprite(Image image) {
        spriteView.setImage(image);
        spriteView.setX(posX - image.getWidth() * 0.5);
        spriteView.setY(posY - image.getHeight() / 2);
        //System.out.println(image.getHeight());
    }

    public void applyDamage(double damage){
        hitPoints -= damage;
        hitPoints = hitPoints < 0 ? 0 : hitPoints;
        healthBar.setProgress(hitPoints / 100);
    }

    public void addCastable(Castable castable) {
        this.castables.add(castable);
    }

    public void switchCastable() {
        if(selectedIndex == 0){
            if(castables.size() > 1){
                selectedIndex = castables.size() - 1;
                selectedCastable = castables.get(selectedIndex);
            }
        }else{
            selectedCastable = castables.get(0);
            selectedIndex = 0;
        }
        selectedAmmo.setImage(selectedCastable.getSpriteView().getImage());
    }

    public void switchPowerUp(){
        if(powerUps.size() > 0){
            if(selectedIndex < powerUps.size() - 1){
                selectedIndex++;
            }else{
                selectedIndex = 0;
            }
            selectedPowerUp = powerUps.get(selectedIndex);
            powerUpImage.setImage(selectedPowerUp.getSprite().getImage());
        }
    }

    public void usePowerUp(){
        if(powerUps.size() > 0){
            selectedPowerUp.onUse(this);
            powerUps.remove(selectedPowerUp);
            if(powerUps.size() > 0){
                powerUpImage.setImage(powerUps.get(0).getSprite().getImage());
                selectedPowerUp = powerUps.get(0);
            }else{
                powerUpImage.setImage(redX);
            }
        }
    }

    public void useCoconut() {
        castables.remove(1);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public Label getNameLabel() {
        return nameLabel;
    }

    public ImageView getSpriteView() {
        return spriteView;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getHitPoints() {
        return hitPoints;
    }

    public Castable getSelectedCastable() {
        return selectedCastable;
    }

    public String getName() {
        return name;
    }

    public Image getGorilla1() {
        return gorilla1;
    }

    public ArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }

    public ArrayList<Castable> getCastables() {
        return castables;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setName(String name) {
        this.name = name;
        nameLabel.setText(name);
        nameLabel.setTextAlignment(TextAlignment.CENTER);
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public void setPosX(double posX) {
        this.posX = posX;
        hitBox.setX(posX - gorilla1.getWidth() * 0.4);
        spriteView.setX(posX - gorilla1.getWidth() * 0.5);
        nameLabel.setLayoutX(posX - GUIHelpers.textSize(nameLabel)/2);
        healthBar.setLayoutX(posX - 30);
    }

    public void setPosY(double posY) {
        this.posY = posY;
        hitBox.setY(posY - gorilla1.getHeight() / 2);
        spriteView.setY(posY - gorilla1.getHeight() / 2);
        nameLabel.setLayoutY(posY - (gorilla1.getHeight()/2 + 25));
        healthBar.setLayoutY(posY - gorilla1.getHeight()/2 - 40);
    }

    public void setNumOfCoconuts(int numOfCoconuts) {
        this.numOfCoconuts = numOfCoconuts;
    }

    
    public void setHitpoints(double hp) {
    	hitPoints = hp;
        if(hitPoints > 100){
            hitPoints = 100;
        }
        healthBar.setProgress(hitPoints / 100);
    }


}
