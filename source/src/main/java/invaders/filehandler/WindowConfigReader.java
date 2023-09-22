package invaders.filehandler;

import org.json.simple.*;

public interface WindowConfigReader {
    public JSONObject getWindowConfig();
    public Long getWindowWidth();
    public Long getWindowHeight();
}
