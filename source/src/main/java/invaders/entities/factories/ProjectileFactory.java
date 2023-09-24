package invaders.entities.factories;

import invaders.entities.Projectile;
import invaders.physics.Coordinates;
import invaders.rendering.Renderable;

public interface ProjectileFactory {
    public default double getCenterX(Renderable shootingEntity){
        final double DUMMY = 0;
        /*
        NOTE:
            - Returns the horizontal center position of the entity shooting the projectile.
        */
        int projectileWidth = (int) new Projectile(new Coordinates(DUMMY, DUMMY)).getWidth();
        int projectileHeight = (int) new Projectile(new Coordinates(DUMMY, DUMMY)).getHeight();

        double entityCenterX = shootingEntity.getPosition().getX() + shootingEntity.getWidth() / 2 -
            projectileWidth / 2;
        
        // double startY = this.player.getPosition().getY() - projectileHeight;

        return entityCenterX;
    }

    public Projectile produceProjectile(Renderable shootingEntity);
}
