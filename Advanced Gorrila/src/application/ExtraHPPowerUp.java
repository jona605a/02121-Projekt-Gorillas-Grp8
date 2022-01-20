package application;

public class ExtraHPPowerUp extends  PowerUp{
    public ExtraHPPowerUp(double x, double y, int r) {
        super(x, y, r, "/Images/ExtraHPPUP.png");
    }

    public void onCollision(GameObject gameObject) {
        // Gives the current player extra hp
        boolean player1Turn = gameObject.isPlayer1Turn();
        Player currentPlayer = player1Turn ? gameObject.getLevel().getPlayer1() : gameObject.getLevel().getPlayer2();
        currentPlayer.setHitpoints(currentPlayer.getHitPoints() + 20);
        super.delete(gameObject);
    }
}
