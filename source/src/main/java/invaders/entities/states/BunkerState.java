package invaders.entities.states;

import invaders.entities.*;

public abstract class BunkerState {
    private Bunker bunker;

    public BunkerState(Bunker bunker){
        /*
        NOTE:
            - Keeping a back-reference to the bunker object to manipulate the
            attributes later on (i.e., state of the bunker object).
        */
        this.bunker = bunker;
    }

    public Bunker getBunker(){
        /*
        NOTE:
            - We have a getter method so that the inheriting classes can access
            the bunker object to manipulate its attributes.
        */

        return this.bunker;
    }

    public abstract void setNextState();
}
