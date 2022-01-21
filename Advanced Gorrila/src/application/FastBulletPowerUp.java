package application;

public class FastBulletPowerUp extends PowerUp {


    public FastBulletPowerUp(double x, double y, int r) {
        super(x, y, r, "/Images/FastBulletPUP.png");
    }

    public void onUse(Player player) {
        // Get current player's thrown castable and halve its weight
        Castable selectedCastable = player.getSelectedCastable();
        selectedCastable.setWeight(selectedCastable.getWeight()  / 2);
    }
}
