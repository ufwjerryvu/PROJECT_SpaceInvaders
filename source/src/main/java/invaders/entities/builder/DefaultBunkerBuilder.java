package invaders.entities.builder;

import org.json.simple.JSONObject;

import invaders.entities.Bunker;
import invaders.filehandler.ConfigReader;
import invaders.physics.Coordinates;

public class DefaultBunkerBuilder implements BunkerBuilder {
    private Bunker product;

    /*
    NOTE:
        - No constructors for this class because we don't need to initialize
        anything anyway.
     */

    public void clear(){
        /*
        NOTE:
            - Setting the product back to null.
        */
        this.product = null;
    }

    public Coordinates getCoordinates(JSONObject bunkerConfig){
        /*
        NOTE:
            - Using the `ConfigReader` to format the coordinates of the bunker.
            However, we don't specify a path because we are not reading the JSON
            file. Instead, we are just using the method in `ConfigReader` to 
            format a JSON object.
         */
        return new ConfigReader().getBunkerCoordinates(bunkerConfig);
    }

    public double getWidth(JSONObject bunkerConfig){
        /*
        NOTE:
            - Also using the `ConfigReader` to format the width of the bunker. 
            Again, we don't need to specify a path.
         */
        return new ConfigReader().getBunkerWidth(bunkerConfig);
    }

    public double getHeight(JSONObject bunkerConfig){
        /*
        NOTE:
            - Once again, the same explanation as above but for the height of the
            bunker object.
        */
        return new ConfigReader().getBunkerHeight(bunkerConfig);
    }

    public void construct(JSONObject bunkerConfig){
        /*
        NOTE:
            - Now we construct the object using the previous setup methods and then
            we 
         */
        Coordinates start = this.getCoordinates(bunkerConfig);
        double width = this.getWidth(bunkerConfig);
        double height = this.getHeight(bunkerConfig);

        this.product = new Bunker(start, (int) width, (int) height);
    }

    public Bunker getProduct(){
        /*
        NOTE:
            - Just returns the bunker that was built.
         */
        return this.product;
    }
}
