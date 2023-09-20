package invaders.filehandler;

import java.io.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public interface WindowConfigReader {
    public JSONObject getWindowConfig();
    public int getWindowWidth();
    public int getWindowHeight();
}
