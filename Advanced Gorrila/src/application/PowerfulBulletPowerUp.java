package application;

public class PowerfulBulletPowerUp extends PowerUp{
    public PowerfulBulletPowerUp(double x, double y, int r) {
        super(x, y, r, "/Images/PowerfulBulletPUP.png");
    }

    public void onUse(Player player) {
        // The current player's thrown castable deals double damage
        player.getSelectedCastable().setDamage(player.getSelectedCastable().getDamage() * 2);
    }
}
