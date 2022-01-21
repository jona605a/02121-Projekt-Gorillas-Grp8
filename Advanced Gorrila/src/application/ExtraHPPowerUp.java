package application;

public class ExtraHPPowerUp extends  PowerUp{
    public ExtraHPPowerUp(double x, double y, int r) {
        super(x, y, r, ClassLoader.getSystemResource("Images/ExtraHPPUP.png").toString());
    }

    public void onUse(Player player) {
        // Gives the current player extra hp
        player.setHitpoints(player.getHitPoints() + 20);
    }
}
