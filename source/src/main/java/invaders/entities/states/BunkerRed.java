package invaders.entities.states;

import java.io.File;

import invaders.entities.Bunker;
import javafx.scene.image.Image;

public class BunkerRed extends BunkerState {
    public BunkerRed(Bunker bunker){
        /*
        NOTE:
            - We're setting the image of the bunker to red.
        */
        super(bunker);

        bunker.setImage(new Image(new File("src/main/resources/bunker_red.png").toURI().toString(),
            bunker.getWidth(), bunker.getHeight(), false, true));
    }

    public void setNextState(){
        /*
        NOTE:
            - We don't have a next state. We just simply mark the bunker up 
            for deletion.
         */
        this.getBunker().setDeleteStatus(true);
    }
}
