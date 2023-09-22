package invaders.entities.states;

import java.io.File;

import invaders.entities.Bunker;
import javafx.scene.image.Image;

public class BunkerGreen extends BunkerState {
    public BunkerGreen(Bunker bunker){
        /*
        NOTE:
            - We're setting the image of the bunker to green.
        */
        super(bunker);

        bunker.setImage(new Image(new File("src/main/resources/bunker_green.png").toURI().toString(),
            bunker.getWidth(), bunker.getHeight(), false, true));
    }

    public void setNextState(){
        /*
        NOTE:
            - The next state is `BunkerYellow`. The constructor of the yellow
            bunker should automatically set the new image of the bunker. 

            - We also need to reassign the state in the bunker object to the new
            state.
         */
        BunkerState nextState = new BunkerYellow(this.getBunker());
        this.getBunker().setNextState(nextState);
    }
}
