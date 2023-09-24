package invaders.entities;

import invaders.logic.*;
import invaders.physics.*;
import invaders.rendering.*;

import javafx.scene.image.*;

import java.io.*;

public class Player implements Moveable, Damagable, Renderable, Collider {
    private final Coordinates position;
    private final Animator anim = null;
    private String colour;
    private int speed;
    private int lives;

    private final double width = 25;
    private final double height = 30;
    private Image image;

    public Player(Coordinates position, String colour, int speed, int lives){
        /*
        NOTE:
            - Setting the image for the player. The image is a fixed relative 
            path. 

            - The position of the player is also stored.
        */
        this.image = new Image(new File("src/main/resources/player.png").toURI().toString(), 
            width, height, true, true);

        this.position = position;
        this.colour = colour;
        this.speed = speed;
        this.lives = lives;
    }

    @Override
    public Image getImage() {
        /*
        NOTE:
            - Gets the image representation.
        */
        return this.image;
    }

    @Override
    public double getWidth() {
        /*
        NOTE:
            - Gets the width of the player object.
        */
        return width;
    }

    @Override
    public double getHeight() {
        /*
        NOTE:
            - Gets the height of the player object.
        */
        return height;
    }

    @Override
    public Coordinates getPosition() {
        /*
        NOTE:
            - Returns the coordinates of the player at any given moment.
         */
        return position;
    }

    @Override
    public Layer getLayer() {
        /*
        NOTE:
            - Get the layer that the player is on.
        */
        return Layer.FOREGROUND;
    }

    public String getColour(){
        /*
        NOTE:
            - Returns the colour of the player object.
        */
        return this.colour;
    }

    public int getSpeed(){
        /*
        NOTE:
            - Returns the speed of the player object in pixels per
            frame.
        */
        return this.speed;
    }

    public void setDeathImage(){
        this.image = new Image(new File("src/main/resources/player_die.png").toURI().toString(), 
            width, height, true, true);
    }

    public void setRegularImage(){
        this.image = new Image(new File("src/main/resources/player.png").toURI().toString(), 
            width, height, true, true);
    }

    @Override
    public void takeLives(int number) {
        /*
        NOTE:
            - Takes a certain number of lives away from the player object but
            enforces the fact that if the number of lives remaining is negative
            then we set it to 0.
         */
        this.lives -= number;

        if(this.lives < 0){
            this.lives = 0;
        }
    }

    @Override
    public int getLives() {
        return this.lives;
    }

    @Override
    public boolean isAlive() {
        return this.lives > 0;
    }

    @Override
    public void up() {
        /*
        NOTE:
            - The player can only move left or right so this is overridden and
            the function just does nothing.
        */
        return;
    }

    @Override
    public void down() {
        /*
        NOTE:
            - The player can only move left or right so the function just
            basically does nothing.
        */
        return;
    }

    @Override
    public void left() {
        /*
        NOTE:
            - We could directly feed the speed of the player into the `setX()`. 
        */
        final int NUMBER_OF_PIXELS = this.getSpeed();
        this.position.setX(this.position.getX() - NUMBER_OF_PIXELS);
    }

    @Override
    public void right() {
        /*
        NOTE:
            - We could directly feed the speed of the player into the `setX()`. 
        */
        final int NUMBER_OF_PIXELS = this.getSpeed();
        this.position.setX(this.position.getX() + NUMBER_OF_PIXELS);
    }

    public boolean isColliding(Projectile projectile){
        /*
        NOTE:
            - Checking if the player is colliding with a projectile object.
             We take a life away from the player if they are colliding.
        */
        boolean collided = Collider.super.isColliding(projectile);

        if(collided){
            final int NUMBER_OF_LIVES_TO_DEDUCT = 1;
            this.takeLives(NUMBER_OF_LIVES_TO_DEDUCT);
        }

        return collided;
    }

    public boolean isColliding(Alien enemy){
        /*
        NOTE:
            - Slightly different thing if we're colliding with an enemy. We
            simply lose the game. All lives lost.
         */
        boolean collided = Collider.super.isColliding(enemy);

        if(collided){
            this.lives = 0;
        }

        /*
        NOTE:
            - If, however, an enemy is levelwith the player then the game is
            also lost.
        */

        if(enemy.getPosition().getY() >= this.position.getY()){
            this.lives = 0;
        }

        return collided;
    }
}
