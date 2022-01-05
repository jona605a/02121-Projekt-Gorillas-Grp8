import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Main extends Application {
	
	private int playerWidth = 10;
	private boolean player1Turn = true;
	private Gorilla player1;
	private Gorilla player2;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		View view = new View();
		stage = view.getMainStage();
		stage.setTitle("SIMPGORILLA");
		player1 = new Gorilla(0, view.getHeight()-playerWidth, playerWidth, playerWidth, view);
		player2 = new Gorilla(view.getWidth()-playerWidth, view.getHeight()-playerWidth, playerWidth, playerWidth, view);
		
		stage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				MouseClickedEvent(mouseEvent, view);
			}
		});
		
		view.getCanvas().setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				Gorilla target = player1Turn ? player1 : player2;
				view.drawAimLine(target, mouseEvent.getX(), mouseEvent.getY());
			}
		});
		
		stage.show();
		
	}
	
	public void MouseClickedEvent(MouseEvent event, View view) {
		double angle;
		
		if (player1Turn) {
			angle = Math.atan((view.getHeight()-event.getY()) / event.getX());
			
		} else {
			angle = Math.atan((view.getHeight()-event.getY()) / (view.getWidth() - event.getX()));
		}
		
		// *** REPLACE IN FUTURE ***
		double velocity = 100;
		
		Gorilla shooter = player1Turn ? player1 : player2;
		Gorilla target = player1Turn ? player2 : player1;
		boolean flyEast = player1Turn ? true : false;
		
		// Throw projectile
		new Projectile(shooter.getX(), shooter.getY(), velocity, angle, target, view.getWidth(), view.getHeight(), view.getRoot(), flyEast);
		
		// change turn
		player1Turn = player1Turn ? false : true;

	}
	
	public static void endGame() {
		// restart the game
		System.out.println("Game over!");
	}
}
