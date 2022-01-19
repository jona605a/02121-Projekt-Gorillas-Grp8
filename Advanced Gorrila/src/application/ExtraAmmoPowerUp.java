package application;

public class ExtraAmmoPowerUp extends PowerUp{
    public ExtraAmmoPowerUp(double x, double y, int r) {
        super(x, y, r, "/Images/ExtraAmmoPUP.png");
    }

    public void onCollision(GameObject gameObject) {
        boolean player1Turn = gameObject.isPlayer1Turn();
        Player currentPlayer = player1Turn ? gameObject.getLevel().getPlayer1() : gameObject.getLevel().getPlayer2();
        currentPlayer.addCastable(new Coconut(currentPlayer.getPosX(), currentPlayer.getPosY()));
        super.delete(gameObject);
    }
}
