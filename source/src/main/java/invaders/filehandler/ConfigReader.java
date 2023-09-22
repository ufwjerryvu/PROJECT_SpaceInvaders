package invaders.filehandler;

import invaders.physics.*;

import java.io.*;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public class ConfigReader implements PlayerConfigReader,
     WindowConfigReader, BunkersConfigReader, EnemiesConfigReader{
    /*
    NOTE:
        - The purpose of this class is to read in the game configurations in the
        JSON file set by the path. For example, this class reads in individual 
        information like the player's spawn points, or the size of the Window, etc.
     */
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

    public Long getWindowWidth(){
        /*
        NOTE:
            - Returns the width of the window.
        */
        JSONObject window = this.getWindowConfig();
        Long windowWidth = (Long)((JSONObject) window.get("size")).get("x");

        return windowWidth;
    }

    public Long getWindowHeight(){
        /*
        NOTE:
            - Returns the height of the window.
        */
        JSONObject window = this.getWindowConfig();
        Long windowHeight = (Long)((JSONObject) window.get("size")).get("y");

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

    public Long getPlayerSpeed(){
        /*
        NOTE:
            - Returns the speed of the player
         */

        JSONObject player = this.getPlayerConfig();
        Long playerSpeed = (Long)((JSONObject) player).get("speed");

        return playerSpeed;
    }

    public Long getPlayerLives(){
        /*
        NOTE:
            - Returns the player's number of lives.
         */

        JSONObject player = this.getPlayerConfig();
        Long playerLives = (Long)((JSONObject) player).get("lives");

        return playerLives;
    }

    public Coordinates getPlayerCoordinates(){
        /*
        NOTE:
            - Returns the player's starting coordinates.
        */
        JSONObject player = this.getPlayerConfig();
        Long playerStartX = (Long)((JSONObject) player.get("position")).get("x");
        Long playerStartY = (Long)((JSONObject) player.get("position")).get("y");

        return new Coordinates(playerStartX, playerStartY);
    }

    public ArrayList<JSONObject> getAllBunkerConfigs(){
        /*
        NOTE:
            - This returns a list of all the bunker objects in
            the JSON file. 
        */
        ArrayList<JSONObject> rlist = new ArrayList<JSONObject>();

        JSONArray enemies = (JSONArray) this.object.get("Bunkers");

        for(Object enemy: enemies){
            rlist.add((JSONObject) enemy);
        }

        return rlist;
    }

    public Coordinates getBunkerCoordinates(JSONObject bunker){
        /*
        NOTE:
            - Reading the coordinates of a specific bunker object.
        */
        Long x = (Long)((JSONObject) bunker.get("position")).get("x");
        Long y = (Long)((JSONObject) bunker.get("position")).get("y");

        return new Coordinates(x, y);
    }

    public Long getBunkerWidth(JSONObject bunker){
        /*
        NOTE:
            - Read the width of a specific bunker object.
        */
        return (Long)((JSONObject) bunker.get("size")).get("x");
    }

    public Long getBunkerHeight(JSONObject bunker){
        /*
        NOTE:
            - Read the height of a specific bunker object.
        */
        return (Long)((JSONObject) bunker.get("size")).get("y");
    }
    
    public ArrayList<JSONObject> getAllEnemyConfigs(){
        /*
        NOTE:
            - This returns a list of all the enemy objects in
            the JSON file. 
        */
        ArrayList<JSONObject> rlist = new ArrayList<JSONObject>();

        JSONArray enemies = (JSONArray) this.object.get("Enemies");

        for(Object enemy: enemies){
            rlist.add((JSONObject) enemy);
        }

        return rlist;
    }

    public Coordinates getEnemyCoordinates(JSONObject enemy){
        /*
        NOTE:
            - This returns the starting coordinates of an enemy object
            in the JSON file.
        */

        Long x = (Long)((JSONObject) enemy.get("position")).get("x");
        Long y = (Long)((JSONObject) enemy.get("position")).get("y");

        return new Coordinates(x, y);
    }

    public String getEnemyShootingStrategy(JSONObject enemy){
        /*
        NOTE:
            - Gets the shooting strategy of the enemy. Two possible
            values as of now is: "slow_straight" and "fast_straight".
        */
        return (String) enemy.get("projectile");
    }
}
