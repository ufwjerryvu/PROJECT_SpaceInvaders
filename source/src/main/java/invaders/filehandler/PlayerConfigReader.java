package invaders.filehandler;

import invaders.physics.*;

import java.io.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public interface PlayerConfigReader {
    public JSONObject getPlayerConfig();
    public String getPlayerColour();
    public Long getPlayerSpeed();
    public Long getPlayerLives();
    public Coordinates getPlayerCoordinates();
}
