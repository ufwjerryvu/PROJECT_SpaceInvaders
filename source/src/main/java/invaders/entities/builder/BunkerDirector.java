package invaders.entities.builder;

import java.util.*;

import org.json.simple.JSONObject;

import invaders.entities.Bunker;
import invaders.filehandler.BunkersConfigReader;
import invaders.filehandler.ConfigReader;

public class BunkerDirector {
    private BunkerBuilder builder;

    public BunkerDirector(BunkerBuilder builder){
        /*
        NOTE:
            - We are assigning any builder under the `BunkerBuilder` to the 
            `BunkerDirector` instance.
         */
        this.builder = builder;
    }

    public void setBuilder(BunkerBuilder builder){
        /*
        NOTE:
            - Might want to switch to a different builder later on just in case
            we have different types of bunkers later down the road.
         */
        this.builder = builder;
    }

    public List<Bunker> makeRegularBunkers(String configPath){
        /*
        NOTE:
            - This just returns a list of all bunkers that has been built. This
            will be called by the client, which should be `GameEngine`.
         */
        List<Bunker> bunkers = new ArrayList<Bunker>();
        
        BunkersConfigReader cr = new ConfigReader(configPath);
        ArrayList<JSONObject> allBunkerConfigs = cr.getAllBunkerConfigs();

        for(JSONObject bunkerConfig: allBunkerConfigs){
            /*
            NOTE:
                - We need to first clear the builder first because the builder might
                still have the previous build. 
             */
            this.builder.clear();
            this.builder.construct(bunkerConfig);
            bunkers.add(this.builder.getProduct());
        }

        return bunkers;
    }
}
