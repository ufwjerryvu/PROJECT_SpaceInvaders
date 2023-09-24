package invaders.entities.strategies;

public class FastStraight implements ProjectileStrategy {
    public double getStrategicSpeed(){
        final double SPEED_IN_PIXELS = 2.5;

        return SPEED_IN_PIXELS;
    }
}
