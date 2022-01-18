package application;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class Player {
    private int hitPoints;
    private String name;
    private double posX, posY;
    private double velocityX, velocityY;
    private ArrayList<Castable> castables = new ArrayList<Castable>();
    private Circle hitBox = new Circle();
    private Castable selectedCastable;
    private Label nameLabel;
    private ImageView spriteView;
    public Image gorilla1 = new Image("/Images/Gorilla1.png", 58, 58, true, false);
    public Image gorilla2 = new Image("/Images/Gorilla2.png", 58, 58, true, false);
    public Image gorilla3 = new Image("/Images/Gorilla3.png", 58, 58, true, false);

    Player(int hp, String name, double x, double y, double spriteoffsetX){
        this.hitPoints = hp;
        this.name = name;
        posX = x;
        posY = y;
        velocityX = 0;
        velocityY = 0;
        castables.add(new Banana(x, y));
        hitBox.setRadius(10);
        hitBox.setCenterX(x);
        hitBox.setCenterY(y);
        selectedCastable = new Banana(posX, posY);


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
        setSprite(gorilla1, spriteoffsetX);

    }

    public boolean collision(double x, double y){
        return hitBox.contains(x, y);
    }

    boolean collision(Bounds localBounds) {
        return hitBox.intersects(localBounds);
    }

    public void setSprite(Image image, double spriteoffsetX) {
        spriteView.setImage(image);
        spriteView.setX(posX + spriteoffsetX - image.getWidth()/2);
        spriteView.setY(posY - gorilla1.getHeight()/2 - image.getHeight()/2);
        //System.out.println(image.getHeight());
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

}
