package invaders.filehandler;

import invaders.physics.*;

import java.io.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public class ConfigReader implements PlayerConfigReader,
     WindowConfigReader{
    /*
    NOTE:
        - The purpose of this class is to read in the game configurations in the
        JSON file set by the path. For example, this class reads in individual 
        information like the player's spawn points, or the size of the Window, etc.
     */
    private String path;
    private JSONObject object;

    public ConfigReader(String path){
        this.parseAll(path);
    }

    private void parseAll(String path){
        JSONParser parser = new JSONParser();

        try{
            Object raw = parser.parse(new FileReader(path));

            /*
            NOTE:
                - Casting an object of type `Object` into a `JSONObject`.
            */
            this.object = (JSONObject) raw;

        }
        
        catch(FileNotFoundException e){ e.printStackTrace();}
        catch(IOException e){ e.printStackTrace(); }
        catch(ParseException e){ e.printStackTrace();}
    }

    public JSONObject getWindowConfig(){
        /*
        NOTE:
            - This is to return the JSONObject with all the related configurations
            related to the game.
        */

        return (JSONObject) this.object.get("Game");
    }

    public int getWindowWidth(){
        /*
        NOTE:
            - Returns the width of the window.
        */
        JSONObject window = this.getWindowConfig();
        int windowWidth = (int)((JSONObject) window.get("size")).get("x");

        return windowWidth;
    }

    public int getWindowHeight(){
        /*
        NOTE:
            - Returns the height of the window.
        */
        JSONObject window = this.getWindowConfig();
        int windowHeight = (int)((JSONObject) window.get("size")).get("y");

        return windowHeight;
    }

    public JSONObject getPlayerConfig(){
        /*
        NOTE:
            - Returns the block with all the player configs in it.
        */

        return (JSONObject) this.object.get("Player");
    }

    public String getPlayerColour(){
        /*
        NOTE:
            - Returns the colour of the player.
        */
        JSONObject player = this.getPlayerConfig();
        String playerColour = (String) ((JSONObject) player).get("colour");

        return playerColour;
    }

    public int getPlayerSpeed(){
        /*
        NOTE:
            - Returns the speed of the player
         */

        JSONObject player = this.getPlayerConfig();
        int playerSpeed = (int)((JSONObject) player).get("speed");

        return playerSpeed;
    }

    public int getPlayerLives(){
        /*
        NOTE:
            - Returns the player's number of lives.
         */

        JSONObject player = this.getPlayerConfig();
        int playerLives = (int)((JSONObject) player).get("lives");

        return playerLives;
    }

    public Coordinates getPlayerCoordinates(){
        /*
        NOTE:
            - Returns the player's starting coordinates.
        */
        JSONObject player = this.getPlayerConfig();
        double playerStartX = (double)((JSONObject) player.get("position")).get("x");
        double playerStartY = (double)((JSONObject) player.get("position")).get("y");

        return new Coordinates(playerStartX, playerStartY);
    }
}
