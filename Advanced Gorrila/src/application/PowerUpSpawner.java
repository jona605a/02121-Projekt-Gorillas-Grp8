package application;

import java.util.Random;

public class PowerUpSpawner {

    public static PowerUp[] spawnPowerUps(double xMin, double yMin, double width, double height, int amount) {
        // spawns and returns a list of new powerups
        int r = 4;
        PowerUp[] powerUps = new PowerUp[amount];

        while(amount-- > 0) {

            double x = randomNumber(xMin, xMin+width);
            double y = randomNumber(yMin, yMin+width);
            int powerUpType = randomNumber( 6);

            switch (powerUpType) {
                case 0:
                    powerUps[amount] = new FastBulletPowerUp(x, y, r);
                    break;
                case 1:
                    powerUps[amount] = new SlowBulletPowerUp(x, y, r);
                    break;
                case 2:
                    powerUps[amount] = new ExtraAmmoPowerUp(x, y, r);
                    break;
                case 3:
                    powerUps[amount] = new ExtraHPPowerUp(x, y, r);
                    break;
                case 4:
                    powerUps[amount] = new PowerfulBulletPowerUp(x, y, r);
                    break;
                case 5:
                    powerUps[amount] = new ExtraTurnPowerUp(x, y, r);
                    break;
            }
        }

        return powerUps;
    }

    private static int randomNumber(int max) {
        //return a
        Random random = new Random();
        return random.nextInt(max);
    }

    private static double randomNumber(double min, double max) {
        // returns a number between min (inclusive) and max (exclusive)
        Random random = new Random();
        return random.nextDouble(max - min) + min;
    }
}
