package invaders.entities;

import java.io.File;

import invaders.physics.Collider;
import invaders.physics.Coordinates;
import invaders.physics.Moveable;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

public class Projectile implements Renderable, Collider, Moveable{
    private Coordinates position;
    private double width;
    private double height; 
    private Image image;
    private double speed;

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
        final int SLOW_STRAIGHT = 1;
        this.speed = SLOW_STRAIGHT;
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

    public boolean isColliding(Collider col){
        /*
        NOTE:
            - Calls the other object's `isColliding()` method.
        */
        return col.isColliding(this);
    }
}
