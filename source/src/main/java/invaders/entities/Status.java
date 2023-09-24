package invaders.entities;

import java.io.File;

import invaders.physics.Coordinates;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

public class Status implements Renderable{
    private Coordinates position;
    private double width = 200;
    private double height = 80;
    private Image image;

    public Status(Coordinates position){
        this.position = position;
        this.image = null;
    }

    /*
    NOTE:
        - Just the regular setters and getters.
     */
    public Image getImage(){ return this.image;}
    public double getWidth(){ return this.width;}
    public double getHeight(){ return this.height;}
    public Coordinates getPosition(){ return this.position;}
    public Layer getLayer(){ return Layer.FOREGROUND;}

    public void setLose(){
        this.image = new Image(new File("src/main/resources/lose.png").toURI().toString(), 
            width, height, true, true);
    }

    public void setWin(){
        this.image = new Image(new File("src/main/resources/win.png").toURI().toString(), 
            width, height, true, true);
    }
}
