package invaders.entities.builder;

import org.json.simple.JSONObject;

import invaders.entities.Bunker;
import invaders.physics.Coordinates;

public interface BunkerBuilder {
    /*
    NOTE:
        - Provides the interface for concrete builders of bunkers.

        - Bunkers are really simple as they are mostly composed of the coordinates,
        the width, and the height. They are simply rectangular objects, for now.
    */
    public void clear();

    public Coordinates getCoordinates(JSONObject bunkerConfig);
    
    public double getWidth(JSONObject bunkerConfig);
    public double getHeight(JSONObject bunkerConfig);

    public void construct(JSONObject bunkerConfig);
    
    public Bunker getProduct();
}   
