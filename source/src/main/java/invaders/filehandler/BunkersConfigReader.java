package invaders.filehandler;

import invaders.physics.*;

import java.io.*;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public interface BunkersConfigReader {
    public ArrayList<JSONObject> getAllBunkerConfigs();
    public Coordinates getBunkerCoordinates(JSONObject bunker);
    public Long getBunkerWidth(JSONObject bunker);
    public Long getBunkerHeight(JSONObject bunker);
}
