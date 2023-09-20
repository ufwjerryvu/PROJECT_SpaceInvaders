package invaders.filehandler;

import java.io.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public interface WindowConfigReader {
    public JSONObject getWindowConfig();
    public Long getWindowWidth();
    public Long getWindowHeight();
}
