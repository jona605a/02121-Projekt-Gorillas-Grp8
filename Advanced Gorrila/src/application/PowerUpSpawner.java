package application;

import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Random;

public class PowerUpSpawner {

    public static ArrayList<PowerUp> spawnPowerUps(double xMin, double yMin, double width, double height, int amount, ArrayList<StaticEntity> statics) {
        // spawns and returns a list of new powerups
        int r = 50;
        ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();

        while(amount-- > 0) {
            boolean valid;
            double x;
            double y;

            do {
                valid = true;
                x = randomNumber(xMin, xMin + width);
                y = randomNumber(yMin, yMin + height);
                for (int i = 0; i < statics.size(); i++){
                    if (statics.get(i).collision(new Circle(x, y, r).getLayoutBounds())) valid = false;
                }

            } while (!valid);
            int powerUpType = randomNumber( 6);

            switch (powerUpType) {
                case 0 -> powerUps.add(new FastBulletPowerUp(x, y, r));
                case 1 -> powerUps.add(new SlowBulletPowerUp(x, y, r));
                case 2 -> powerUps.add(new ExtraAmmoPowerUp(x, y, r));
                case 3 -> powerUps.add(new ExtraHPPowerUp(x, y, r)); //Missing sprite
                case 4 -> powerUps.add(new PowerfulBulletPowerUp(x, y, r));
                case 5 -> powerUps.add(new ExtraTurnPowerUp(x, y, r));
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
