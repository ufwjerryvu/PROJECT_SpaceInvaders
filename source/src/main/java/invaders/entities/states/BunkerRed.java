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

        bunker.setImage(new Image(new File("src/main/resources/bunker_red.png").toURI().toString()));
    }

    public void setNextState(){
        /*
        NOTE:
            - (TO-DO) The next state is `null` as there is no other states to go
            on from here. This is because `null` should be enough to represent that
            the object is to be destroyed. 

            - We also need to reassign the state in the bunker object to `null`.
         */
        BunkerState nextState = null;
        this.getBunker().setNextState(nextState);
    }
}
