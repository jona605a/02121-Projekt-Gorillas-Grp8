package application;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Building extends MapObject {
    private Rectangle shape;
    private ImageView[] sprites;
    private static Random r = new Random();
    private static Image[] images = new Image[] {
            new Image("/Images/Brick Wall.png"),
            new Image("/Images/House with inhabitant high.png"),
            new Image("/Images/Brick Wall middle.png"),
            new Image("/Images/Building middle.png"),
            new Image("/Images/Flat Roof.png"),
            new Image("/Images/Roof with stuff.png")
    };
    private double roofSize = 0.55;

    Building(double x, double y, double width, double height, int[] stories) throws Exception {
        if(stories.length < 2){
            throw new Exception("Buildings must have at least two stories");
        }
        sprites = new ImageView[stories.length];
        this.x = x;
        this.y = y + height * roofSize;
        this.width = width;
        this.height = height;
        shape = new Rectangle(x,this.y, width,height * (stories.length - 1) - roofSize * height);
        for(int i = 0; i < stories.length; i++){
            sprites[i] = new ImageView(images[stories[i]]);
            sprites[i].setX(x);
            sprites[i].setY(y + height * (stories.length - 1 - i));
            sprites[i].setFitWidth(width);
            sprites[i].setFitHeight(height);
        }


    }

    Building(double x, double y, double width, double height, int storiesCnt) throws Exception {

        int[] stories = new int[storiesCnt];
        stories[stories.length - 1] = 4 + r.nextInt(2);
        stories[0] = r.nextInt(2);
        for(int i = 1; i < storiesCnt - 1; i++){
            stories[i] = 2 + r.nextInt(2);
        }

        sprites = new ImageView[stories.length];
        this.x = x;
        this.y = y + height * roofSize;
        this.width = width;
        this.height = height;
        shape = new Rectangle(x,this.y, width,height * (stories.length - 1) - roofSize * height);
        for(int i = 0; i < storiesCnt; i++){
            sprites[i] = new ImageView(images[stories[i]]);
            sprites[i].setX(x);
            sprites[i].setY(y + height * (storiesCnt - 1 - i));
            sprites[i].setFitWidth(width);
            sprites[i].setFitHeight(height);
        }



    }

    @Override
    public boolean collision(double x, double y) {
        return shape.contains(x, y);
    }

    @Override
    boolean collision(Bounds localBounds) {
        return shape.intersects(localBounds);
    }


    @Override
    public Node getShape(){
        return shape;
    }

    @Override
    Node[] getSprites() {
        return sprites;
    }

    @Override
    Node getSprite() {
        return null;
    }
}
