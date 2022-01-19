package application;

public class FastBulletPowerUp extends PowerUp {


    public FastBulletPowerUp(double x, double y, int r) {
        super(x, y, r, "/Images/FastBulletPUP.png");
    }

    public void onCollision(GameObject gameObject) {
        boolean player1Turn = gameObject.isPlayer1Turn();
        Castable selectedCastable = player1Turn ? gameObject.getLevel().getPlayer1().getSelectedCastable() : gameObject.getLevel().getPlayer2().getSelectedCastable();
        selectedCastable.setVelocityX(selectedCastable.getVelocityX() * 2);
        selectedCastable.setVelocityY(selectedCastable.getVelocityY() * 2);
    }
}
