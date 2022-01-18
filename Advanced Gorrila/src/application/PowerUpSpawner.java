package application;

import java.util.Random;

public class PowerUpSpawner {

    public static PowerUp[] spawnPowerUps(double xMin, double yMin, double width, double height, int amount) {
        // spawns and returns a list of new powerups
        int r = 4;
        PowerUp[] powerUps = new PowerUp[amount];

        while(amount-- > 0) {

            double x = randomNumber(xMin, xMin+width);
            double y = randomNumber(yMin, yMin+height);
            int powerUpType = randomNumber( 6);

            switch (powerUpType) {
                case 0 -> powerUps[amount] = new FastBulletPowerUp(x, y, r);
                case 1 -> powerUps[amount] = new SlowBulletPowerUp(x, y, r);
                case 2 -> powerUps[amount] = new ExtraAmmoPowerUp(x, y, r);
                case 3 -> powerUps[amount] = new ExtraHPPowerUp(x, y, r);
                case 4 -> powerUps[amount] = new PowerfulBulletPowerUp(x, y, r);
                case 5 -> powerUps[amount] = new ExtraTurnPowerUp(x, y, r);
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
