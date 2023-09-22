package invaders.filehandler;

import invaders.physics.*;

import org.json.simple.*;

public interface PlayerConfigReader {
    public JSONObject getPlayerConfig();
    public String getPlayerColour();
    public Long getPlayerSpeed();
    public Long getPlayerLives();
    public Coordinates getPlayerCoordinates();
}
