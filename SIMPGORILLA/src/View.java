import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Line;


public class View {
	
	private Group root;
	private Canvas canvas;
	private GraphicsContext gc;
	private Scene mainScene;
	private Stage mainStage;
	private double sceneWidth;
	private double sceneHeight;
	private Line aimLine;
	
	public View() {
		this(500, 500);
	}
	
	public View(double sceneWidth, double sceneHeight) {
		
		this.sceneWidth = sceneWidth;
		this.sceneHeight = sceneHeight;
		
		root = new Group();
		canvas = new Canvas(sceneHeight, sceneWidth);
		root.getChildren().add(canvas);
		gc = canvas.getGraphicsContext2D();
		
		aimLine = new Line();
		root.getChildren().add(aimLine);
		
		mainScene = new Scene(root, sceneWidth, sceneHeight);
		mainStage = new Stage();
		mainStage.setScene(mainScene);
	}
	
	public void clearCanvas() {
		gc.clearRect(0, 0, sceneWidth, sceneHeight);
	}
	
	public Stage getMainStage() {
		return mainStage;
	}
	
	public Group getRoot() {
		return root;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public double getWidth() {
		return this.sceneWidth;
	}
	
	public double getHeight() {
		return this.sceneHeight;
	}
	
	public void drawBox(double posX, double posY, double width, double height) {
		gc.fillRect(posX, posY, width, height);
	}
	
	public void drawAimLine(Gorilla target, double mouseX, double mouseY) {
		aimLine.setEndX(mouseX);
		aimLine.setEndY(mouseY);
		aimLine.setStartX(target.getX());
		aimLine.setStartY(target.getY());
	}
	
	public void animateThrow() {
		
	}
	
}
