package application;

public class SlowBulletPowerUp extends  PowerUp{

    public SlowBulletPowerUp(double x, double y, int r) {
        super(x, y, r, "/Images/SlowBulletPUP.png");
    }

    public void onCollision(GameObject gameObject) {
        boolean player1Turn = gameObject.isPlayer1Turn();
        Castable selectedCastable = player1Turn ? gameObject.getLevel().getPlayer1().getSelectedCastable() : gameObject.getLevel().getPlayer2().getSelectedCastable();
        selectedCastable.setVelocityX(selectedCastable.getVelocityX() * 0.5);
        selectedCastable.setVelocityY(selectedCastable.getVelocityY() * 0.5);
        super.delete(gameObject);
    }
}
