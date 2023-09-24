package invaders.entities;

import java.io.File;

import invaders.entities.strategies.*;
import invaders.physics.*;
import invaders.rendering.*;
import javafx.scene.image.*;

public class Projectile implements Renderable, Collider, Moveable{
    private Coordinates position;
    private double width;
    private double height; 
    private Image image;
    private double speed;
    private ProjectileStrategy strategy;

    public Projectile(Coordinates position){
        /*
        NOTE:
            - Just initializing.
        */
        this.position = new Coordinates(position.getX(), position.getY());

        final int WIDTH_IN_PIXELS = 3;
        final int HEIGHT_IN_PIXELS = 10;

        this.width = WIDTH_IN_PIXELS;
        this.height = HEIGHT_IN_PIXELS;

        this.image = new Image(new File("src/main/resources/projectile.png").toURI().toString(), 
            this.width, this.height, false, true);

        /*
        NOTE:
            - Every projectile will be initialized with a slow speed and then
            we will worry about setting the strategy later on. The strategy will
            be set by the Builder.
         */
        this.strategy = new SlowStraight();
        this.speed = this.strategy.getStrategicSpeed();
    }

    public Image getImage(){
        /*
        NOTE:
            - Returns the image of the projectile.
         */
        return this.image;
    }

    public double getWidth(){
        /*
        NOTE:
            - Returns the width of the projectile.
        */
        return this.width;
    }

    public double getHeight(){
        /*
        NOTE:
            - Returns the height of the projectile.
        */
        return this.height;
    }

    public Layer getLayer(){
        /*
        NOTE:
            - Returns the layer of the projectile. Default is the foreground.
        */
        return Layer.FOREGROUND;
    }

    public Coordinates getPosition(){
        /*
        NOTE:
            - Returns the position of the projectile.
        */
        return this.position;
    }

    public void setStrategy(ProjectileStrategy strategy){
        /*
        NOTE:
            - We set the speed as we pick a new strategy.
        */
        this.strategy = strategy;
        this.speed = strategy.getStrategicSpeed();
    }

    public void up(){
        /*
        NOTE:
            - Moves the projectile up.
        */
        this.position.setY(this.position.getY() - this.speed);
    }

    public void down(){
        /*
        NOTE:
            - Moves the projectile down.
        */
        this.position.setY(this.position.getY() + this.speed);
    }

    public void left(){
        /*
        NOTE:
            - Projectiles can't move left, for now.
        */
        return;
    }

    public void right(){
        /*
        NOTE:
            - Projectiles can't move right, for now.
        */
        return;
    }

    public boolean isColliding(Bunker col){
        /*
        NOTE:
            - Activates the other collidable object's `isColliding()` method.
        */
        return col.isColliding(this);
    }

    public boolean isColliding(Alien col){
        /*
        NOTE:
            - Activates the Alien's `isColliding()` method.
        */
        return col.isColliding(this);
    }

    public boolean isColliding(Player col){
        /*
        NOTE:
            - Activates the Player's `isColliding()` method.
        */
        return col.isColliding(this);
    }

    public boolean isColliding(Projectile col){
        /*
        NOTE:
            - Just uses the default implementation. We don't even actually need
             to override this. But I'm doing this to show that I'm intentional
             about this.
        */
        return Collider.super.isColliding(col);
    }
}
