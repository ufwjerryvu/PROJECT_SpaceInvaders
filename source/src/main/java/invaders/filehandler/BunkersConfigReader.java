package invaders.filehandler;

import invaders.physics.*;

import java.util.*;

import org.json.simple.*;

public interface BunkersConfigReader {
    public ArrayList<JSONObject> getAllBunkerConfigs();
    public Coordinates getBunkerCoordinates(JSONObject bunker);
    public Long getBunkerWidth(JSONObject bunker);
    public Long getBunkerHeight(JSONObject bunker);
}
