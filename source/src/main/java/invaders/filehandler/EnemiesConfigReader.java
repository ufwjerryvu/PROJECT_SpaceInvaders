package invaders.filehandler;

import invaders.physics.*;

import java.io.*;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public interface EnemiesConfigReader {
    public ArrayList<JSONObject> getAllEnemyConfigs();
    public Coordinates getEnemyCoordinates(JSONObject enemy);
    public String getEnemyShootingStrategy(JSONObject enemy);
}
