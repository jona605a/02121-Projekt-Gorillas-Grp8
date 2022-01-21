package application;

public class ExtraAmmoPowerUp extends PowerUp{
    public ExtraAmmoPowerUp(double x, double y, int r) {
        super(x, y, r, ClassLoader.getSystemResource("Images/ExtraAmmoPUP.png").toString());
    }

    public void onUse(Player player) {
        // Gives the current player an extra coconut
        player.addCoconuts(1);
    }
}
