package application;

public class ExtraTurnPowerUp extends PowerUp{

    public ExtraTurnPowerUp(double x, double y, int r) {
        super(x, y, r, ClassLoader.getSystemResource("Images/ExtraTurnPUP.png").toString());
    }

    public void onUse(GameObject gameObject) {
        // Gives the current player another turn
        boolean player1Turn = gameObject.isPlayer1Turn();
        gameObject.setPlayer1Turn(!player1Turn);
    }

    @Override
    public void onUse(Player player) {
        // does nothing
    }
}
