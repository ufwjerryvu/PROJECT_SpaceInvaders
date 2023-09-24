package invaders.entities;

import java.io.File;

import invaders.physics.Collider;
import invaders.physics.Coordinates;
import invaders.physics.Moveable;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

public class Alien implements Renderable, Collider, Moveable{
    private Coordinates position;
    private double width;
    private double height;
    private Image image;
    private double speed;
    private String strategy;
    private boolean delete;

    public Alien(Coordinates position){
        /*
        NOTE:
            - Just initializing things.
         */
        this.position = position;

        this.width = 30;
        this.height = 25;

        this.image = new Image(new File("src/main/resources/enemy.png").toURI().toString(), 
            width, height, false, true);
        
        this.speed = 0.5;
    }

    public Image getImage(){
        /*
        NOTE:
            - Returns the image of the alien.
        */
        return this.image;
    }

    public double getWidth(){
        /*
        NOTE:
            - Returns the width of the alien.
        */
        return this.width;
    }

    public double getHeight(){
        /*
        NOTE:
            - Returns the height of the alien.
        */
        return this.height;
    }

    public Layer getLayer(){
        /*
        NOTE:
            - The default for game objects is FOREGROUND.
        */
        return Layer.FOREGROUND;
    }

    public Coordinates getPosition(){
        /*
        NOTE:
            - Returns the coordinates of the enemy.
        */
        return this.position;
    }

    public String getStrategy(){
        /*
        NOTE:
            - Gets the shooting strategy of the alien, but as
            a string.
        */
        return this.strategy;
    }

    public boolean getDeleteStatus(){
        /*
        NOTE:
            - Returns whether the enemy is marked for deletion.
        */
        return this.delete;
    }

    public void setDeleteStatus(boolean delete){
        /*
        NOTE:
            - Marks whether the enemy is set for delete.
        */
        this.delete = delete;
    }

    public void setStrategy(String strategy){
        /*
        NOTE:
            - Returns the width of the alien.
        */ 
        this.strategy = strategy;
    }

    public void increaseSpeed(){
        /*
        NOTE:
            - Increases by a constant number of pixels per frame.
        */
        final double ACCELERATION = 0.2;
        this.speed += ACCELERATION;
    }

    public void up(){
        /*
        NOTE:
            - Enemies don't go up.
        */
        return;
    }

    public void down(){
        /*
        NOTE:
            - Enemies can go down.
        */
        this.position.setY(this.position.getY() + this.speed);
    }

    public void left(){
        /*
        NOTE:
            - Enemies can go left.
        */
        this.position.setX(this.position.getX() - this.speed);
    }

    public void right(){
        /*
        NOTE:
            - Enemies can go right.
         */
        this.position.setX(this.position.getX() + this.speed);
    }
}
