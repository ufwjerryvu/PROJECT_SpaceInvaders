package invaders.entities.states;

import java.io.File;

import invaders.entities.Bunker;
import javafx.scene.image.Image;

public class BunkerYellow extends BunkerState {
    public BunkerYellow(Bunker bunker){
        /*
        NOTE:
            - We're setting the image of the bunker to yellow.
        */
        super(bunker);

        bunker.setImage(new Image(new File("src/main/resources/bunker_yellow.png").toURI().toString(),
            bunker.getWidth(), bunker.getHeight(), false, true));
    }

    public void setNextState(){
        /*
        NOTE:
            - The next state is `BunkerRed`. The constructor of the red
            bunker should automatically set the new image of the bunker. 

            - We also need to reassign the state in the bunker object to the new
            state.
         */
        BunkerState nextState = new BunkerRed(this.getBunker());
        this.getBunker().setNextState(nextState);
    }
}
