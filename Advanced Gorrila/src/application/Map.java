package application;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Random;

public class Map {

    private ArrayList<MapObject> mapObjects;
    private ArrayList<Building> buildings;
    private ArrayList<Platform> platforms;
    private ArrayList<PowerUp> powerUps;
    private AnchorPane pane;
    private Scene scene;
    private double screenX, screenY;
    private double buildingWidth, buildingStoryHeight;
    private Random r = new Random();
    private int numOfBuildings = 0;
    private boolean random;


    Map(double screenX, double screenY, boolean r) throws Exception {
        this.screenX = screenX;
        this.screenY = screenY;
        pane = new AnchorPane();
        scene = new Scene(pane);
        mapObjects = new ArrayList<>();
        buildings = new ArrayList<>();
        platforms = new ArrayList<>();
        powerUps = new ArrayList<>();
        random = r;
        buildingWidth = screenX / 10;
        buildingStoryHeight = 0.19 * screenY;

        if(r){
            for(int i = 0; i < 10; i++){
                int stories = 2 + this.r.nextInt(3);
                Building b = new Building(buildingWidth * i, screenY - buildingStoryHeight * stories, buildingWidth,buildingStoryHeight, stories);
                buildings.add(b);
                mapObjects.add(b);
                pane.getChildren().addAll(buildings.get(i).getSprites());
            }
            numOfBuildings = 10;
        }
    }




    public void createBuilding(double x, int[] stories) throws Exception {
        Building building = new Building(x, screenY - buildingStoryHeight * stories.length, buildingWidth,buildingStoryHeight, stories);
        buildings.add(building);
        mapObjects.add(building);
        pane.getChildren().addAll(building.getSprites());
        numOfBuildings++;
    }

    public Building getBuilding(int index){
        return buildings.get(index);
    }

    public void createPlatform(double x, double y){
        Platform p = new Platform(x, y, buildingWidth / 2);
        mapObjects.add(p);
        platforms.add(p);
        pane.getChildren().add(p.getSprite());
    }

    public void newRandom() throws Exception {
        buildings.clear();
        mapObjects.clear();
        pane.getChildren().clear();
        if(random){
            for(int i = 0; i < 10; i++){
                int stories = 2 + this.r.nextInt(3);
                Building b = new Building(buildingWidth * i, screenY - buildingStoryHeight * stories, buildingWidth,buildingStoryHeight, stories);
                buildings.add(b);
                mapObjects.add(b);
                pane.getChildren().addAll(buildings.get(i).getSprites());
            }
            numOfBuildings = 10;
        }
    }

    public Scene getScene() {
        return scene;
    }

    public void reset(){
        pane.getChildren().clear();
        for(Building obj : buildings){
            pane.getChildren().addAll(obj.getSprites());
        }
        for(Platform p : platforms){
            pane.getChildren().add(p.getSprite());
        }
    }

    public AnchorPane getPane() {
        return pane;
    }

    public ArrayList<MapObject> getMapObjects() {
        return mapObjects;
    }

    public MapObject getMapObject(int index){
        return mapObjects.get(index);
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public ArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }

    public int getNumOfBuildings() {
        return numOfBuildings;
    }

    public double getBuildingWidth() {
        return buildingWidth;
    }

    public double getBuildingStoryHeight() {
        return buildingStoryHeight;
    }
}
