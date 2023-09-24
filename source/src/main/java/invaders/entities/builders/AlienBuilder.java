package invaders.entities.builders;

import org.json.simple.JSONObject;

import invaders.entities.Alien;
import invaders.physics.Coordinates;

public interface AlienBuilder {
    public void clear();
    public Coordinates getCoordinates(JSONObject enemyConfig);
    public String getShootingStrategy(JSONObject enemyConfig);
    public void construct(JSONObject enemyConfig);
    public Alien getProduct();
}
