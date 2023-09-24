package invaders.entities.factories;

import invaders.entities.Projectile;
import invaders.entities.strategies.FastStraight;
import invaders.physics.Coordinates;
import invaders.rendering.Renderable;

public class FastProjectileFactory implements ProjectileFactory {
    public Projectile produceProjectile(Renderable shootingEntity){
        /*
        NOTE:
            - We just make a projectile with the given coordinates from the shooting
            entity and set the strategy of the projectile to the fast-straight strategy.
        */

        double centerX = this.getCenterX(shootingEntity);
        double startY = shootingEntity.getPosition().getY();

        Projectile projectile = new Projectile(new Coordinates(centerX, startY));

        projectile.setStrategy(new FastStraight());

        return projectile;
    }
}
