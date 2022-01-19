package application;

public class PowerfulBulletPowerUp extends PowerUp{
    public PowerfulBulletPowerUp(double x, double y, int r) {
        super(x, y, r, "/Images/PowerfulBulletPUP.png");
    }

    public void onCollision(GameObject gameObject) {
        // The current player's thrown castable deals double damage
        boolean player1Turn = gameObject.isPlayer1Turn();
        Castable selectedCastable = player1Turn ? gameObject.getLevel().getPlayer1().getSelectedCastable() : gameObject.getLevel().getPlayer2().getSelectedCastable();
        selectedCastable.setDamage(selectedCastable.getDamage() * 2);
        super.delete(gameObject);
    }
}
