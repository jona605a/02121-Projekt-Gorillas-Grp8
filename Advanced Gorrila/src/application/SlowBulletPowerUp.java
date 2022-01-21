package application;

public class SlowBulletPowerUp extends  PowerUp{

    public SlowBulletPowerUp(double x, double y, int r) {
        super(x, y, r, "/Images/SlowBulletPUP.png");
    }

    public void onUse(Player player) {
        // Get current player's thrown castable and double its weight
        Castable selectedCastable = player.getSelectedCastable();
        selectedCastable.setWeight(selectedCastable.getWeight()  * 2);
    }
}
