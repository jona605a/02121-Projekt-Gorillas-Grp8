package application;

public class ExtraTurnPowerUp extends PowerUp{

    public ExtraTurnPowerUp(double x, double y, int r) {
        super(x, y, r, "/Images/ExtraTurnPUP.png");
    }

    public void onCollision(GameObject gameObject) {
        boolean player1Turn = gameObject.isPlayer1Turn();
        gameObject.setPlayer1Turn(!player1Turn);
    }
}
