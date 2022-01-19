package application;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class Player {
    private int hitPoints;
    private String name;
    private double posX, posY;
    private double velocityX, velocityY;
    private ArrayList<Castable> castables = new ArrayList<Castable>();
    private Rectangle hitBox = new Rectangle();
    private Castable selectedCastable;
    private Label nameLabel;
    private ImageView spriteView;
    public Image gorilla1 = new Image("/Images/Gorilla1.png", 58, 58, true, false);
    public Image gorilla2 = new Image("/Images/Gorilla2.png", 58, 58, true, false);
    public Image gorilla3 = new Image("/Images/Gorilla3.png", 58, 58, true, false);

    Player(int hp, String name, double x, double y){
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

        int noOfCoconuts = 10;
        // Adding coconuts as ammo
        for(int i = 0; i < noOfCoconuts; i++) {
        	castables.add(new Coconut(x, y));
        }

        // Display player name
        nameLabel = new Label();
        nameLabel.setFont(new Font("Verdana", 16));
        nameLabel.setText(this.name);
        nameLabel.setLayoutX(posX - GUIHelpers.textSize(nameLabel)/2);
        nameLabel.setLayoutY(posY + gorilla1.getHeight()/2 + 15);
        nameLabel.setTextAlignment(TextAlignment.CENTER);

        // Set player sprite
        spriteView = new ImageView();
        spriteView.setPreserveRatio(true);
        setSprite(gorilla1);

    }

    public boolean collision(double x, double y){
        return hitBox.contains(x, y);
    }

    boolean collision(Bounds localBounds) {
        return hitBox.intersects(localBounds);
    }

    public void setSprite(Image image) {
        spriteView.setImage(image);
        spriteView.setX(posX - image.getWidth() * 0.5);
        spriteView.setY(posY - image.getHeight() / 2);
        //System.out.println(image.getHeight());
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

    public int getHitPoints() {
        return hitPoints;
    }

    public Castable getSelectedCastable() {
        return selectedCastable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        nameLabel.setText(name);
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
    }

    public void setPosY(double posY) {
        this.posY = posY;
        hitBox.setY(posY - gorilla1.getHeight() / 2);
        spriteView.setY(posY - gorilla1.getHeight() / 2);
    }

    public void addCastable(Castable castable) {
    	this.castables.add(castable);
    }
    
    public void addHitPoints(int hp) {
    	this.hitPoints += hp;
    }

    public void switchCastable() {
    	if(castables.size() > 1) {
    		if(selectedCastable.getWeight() == castables.get(0).getWeight()) {
    			selectedCastable = new Coconut(posX, posY);
    		}
    		else {
    			selectedCastable = new Banana(posX, posY);
    		}
    	}
    }
    
    public void useCoconut() {
    	castables.remove(1);
    }
}
