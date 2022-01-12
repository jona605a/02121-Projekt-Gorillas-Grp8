package application;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import javax.swing.text.html.ImageView;
import java.util.ArrayList;

public class Player {
    private int hitPoints;
    private String name;
    private double posX, posY;
    private ArrayList<Castable> castables = new ArrayList<Castable>();
    private Circle hitBox = new Circle();
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
        posY = y - gorilla1.getHeight()/2;
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
        spriteView.setX(posX - image.getWidth()/2);
        spriteView.setY(posY - image.getHeight()/2);
        //System.out.println(image.getHeight());
    }

    public Circle getHitBox() {
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

    public int getHitPoints() {
        return hitPoints;
    }

    public Castable getSelectedCastable() {
        return selectedCastable;
    }
}
