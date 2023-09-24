package invaders.entities.strategies;

public class SlowStraight implements ProjectileStrategy {
    public double getStrategicSpeed(){
        final double SPEED_IN_PIXELS = 1.5;

        return SPEED_IN_PIXELS;
    }
}
