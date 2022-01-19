package application;

import java.util.ArrayList;
import java.util.Random;

public class PowerUpSpawner {

    public static PowerUp[] spawnPowerUps(double xMin, double yMin, double width, double height, int amount, ArrayList<StaticEntity> statics) {
        // spawns and returns a list of new powerups
        int r = 50;
        PowerUp[] powerUps = new PowerUp[amount];

        while(amount-- > 0) {
            boolean valid;
            double x;
            double y;

            do {
                valid = true;
                x = randomNumber(xMin, xMin + width);
                y = randomNumber(yMin, yMin + height);
                for (int i = 0; i < statics.size(); i++){
                    if (statics.get(i).collision(x, y)) valid = false;
                }

            } while (!valid);
            int powerUpType = randomNumber( 6);

            switch (powerUpType) {
                case 0 -> powerUps[amount] = new FastBulletPowerUp(x, y, r);
                case 1 -> powerUps[amount] = new SlowBulletPowerUp(x, y, r);
                case 2 -> powerUps[amount] = new ExtraAmmoPowerUp(x, y, r);
                //case 3 -> powerUps[amount] = new ExtraHPPowerUp(x, y, r); Missing sprite
                case 4 -> powerUps[amount] = new PowerfulBulletPowerUp(x, y, r);
                case 5 -> powerUps[amount] = new ExtraTurnPowerUp(x, y, r);
                default -> System.out.println();
            }
        }

        return powerUps;
    }

    private static int randomNumber(int max) {
        //returns an integer between 0 (inclusive) and max (exclusive)
        Random random = new Random();
        return random.nextInt(max);
    }

    private static double randomNumber(double min, double max) {
        // returns a double between min (inclusive) and max (exclusive)
        Random random = new Random();
        return random.nextDouble(max - min) + min;
    }
}
