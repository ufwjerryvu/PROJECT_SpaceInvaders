package invaders.filehandler;

import invaders.physics.*;

import java.util.*;

import org.json.simple.*;

public interface EnemiesConfigReader {
    public ArrayList<JSONObject> getAllEnemyConfigs();
    public Coordinates getEnemyCoordinates(JSONObject enemy);
    public String getEnemyShootingStrategy(JSONObject enemy);
}
