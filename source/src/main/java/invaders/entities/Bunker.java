package invaders.entities;

import javafx.scene.image.*;

import invaders.*;
import invaders.engine.*;
import invaders.entities.states.*;
import invaders.filehandler.*;
import invaders.logic.*;
import invaders.physics.*;
import invaders.rendering.*;

public class Bunker implements Renderable{
    private Coordinates position;
    private double width;
    private double height;
    private Image representation;
    private BunkerState state;

    public Bunker(Coordinates position, int width, int height){
        /*
        NOTE:
            - Just initializing all the attributes.
        */

        this.position = position;
        this.width = width;
        this.height = height;

        /*
        NOTE:
            - Added a reference to the green state. The bunker always starts
            out as green when we are constructing it according to the specs.
            
            - We pass this instance in as it serves as a back-reference for the 
            states.
        */
        
        state = new BunkerGreen(this);
    }

    public Image getImage(){
        /*
        NOTE:
            - Returns the image of this object.
        */

        return this.representation;
    }

    public double getWidth(){
        /*
        NOTE:
            - Returns the width of the bunker.
        */
        return this.width;
    }

    public double getHeight(){
        /*
        NOTE:
            - Returns the height of the bunker.
        */
        return this.height;
    }

    public Layer getLayer(){
        /*
        NOTE:
            - The default for sprites is the foreground.
        */
        return Layer.FOREGROUND;
    }

    public Coordinates getPosition(){
        /*
        NOTE:
            - Getting the position of the bunker object.
        */
        return this.position;
    }

    public void setImage(Image image){
        /*
        NOTE:
            - Setting the image of the bunker object.
        */
        this.representation = image;
    }

    public void setNextState(BunkerState state){
        /*
        NOTE:
            - This function is mostly used by the concret states to decide their
            successor. 
        */

        this.state = state;
    }
}
