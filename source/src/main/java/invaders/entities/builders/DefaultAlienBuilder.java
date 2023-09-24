package invaders.entities.builders;

import org.json.simple.JSONObject;

import invaders.entities.Alien;
import invaders.filehandler.ConfigReader;
import invaders.physics.Coordinates;

public class DefaultAlienBuilder implements AlienBuilder{
    private Alien product;

    public void clear(){
        /*
        NOTE:
            - Setting the alien product to null in preparation for a new one.
        */
        this.product = null;
    }

    public Coordinates getCoordinates(JSONObject enemyConfig){
        /*
        NOTE:
            - Using a dummy configuration reader to format the JSON object.
         */
        return new ConfigReader().getEnemyCoordinates(enemyConfig);
    }

    public String getShootingStrategy(JSONObject enemyConfig){
        /*
        NOTE:
            - Using a dummy configuration reader to format the JSON object.
         */
        return new ConfigReader().getEnemyShootingStrategy(enemyConfig);
    }

    public void construct(JSONObject enemyConfig){
        /*
        NOTE:
            - Assembling the alien and setting its shooting strategy.
        */
        Alien construction = new Alien(this.getCoordinates(enemyConfig));
        construction.setStrategy(this.getShootingStrategy(enemyConfig));

        this.product = construction;
    }

    public Alien getProduct(){
        /*
        NOTE:
            - Retrieving the alien.
        */
        return this.product;
    }
}
