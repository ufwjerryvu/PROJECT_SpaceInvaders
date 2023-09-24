package invaders.entities.builders;

import java.util.*;

import org.json.simple.JSONObject;

import invaders.entities.Alien;
import invaders.filehandler.ConfigReader;
import invaders.filehandler.EnemiesConfigReader;

public class AlienDirector {
    private AlienBuilder builder;

    public AlienDirector(AlienBuilder builder){
        /*
        NOTE:
            - Assigning any builder under the `AlienBuilder` interface.
         */
        this.builder = builder;
    }

    public void setBuilder(AlienBuilder builder){
        /*
        NOTE:
            - This is just in case we need to builder other types of aliens
            instead.
         */
        this.builder = builder;
    }

    public List<Alien> makeRegularAliens(String configPath){
        List<Alien> aliens = new ArrayList<Alien>();
        
        EnemiesConfigReader cr = new ConfigReader(configPath);
        ArrayList<JSONObject> allEnemyConfigs = cr.getAllEnemyConfigs();

        for(JSONObject enemyConfig: allEnemyConfigs){
            /*
            NOTE:
                - We need to first clear the builder first because the builder might
                still have the previous build. 
             */
            this.builder.clear();
            this.builder.construct(enemyConfig);
            aliens.add(this.builder.getProduct());
        }

        return aliens;
    }
}
