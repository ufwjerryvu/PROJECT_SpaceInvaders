package invaders.engine;

import java.util.*;

import invaders.*;
import invaders.entities.*;
import invaders.physics.*;
import invaders.rendering.*;
import invaders.filehandler.*;

public class GameEngine {
	/*
	NOTE:
		- This class manages the main loop and logic of the game.
	*/
	private String configPath;

	private final int width;
	private final int height;

	private List<Sprite> sprites;
	private List<Renderable> renderables;
	private Player player;

	private boolean left;
	private boolean right;

	public GameEngine(String configPath){
		/*
		NOTE:
			- We are using the `ConfigReader` to get each configuration specified
			by the config file.
		*/

		this.configPath = configPath;

		sprites = new ArrayList<Sprite>();
		renderables = new ArrayList<Renderable>();

		/*
		NOTE:
			- Here we are using the `WindowConfigReader` interface to load the configs 
			for the game. It's pretty simple cause we only have the width and the height
			of the window. The template given was really bad because we need to use the
			width and the height in this class as well as the width and the height in 
			the `GameWindow` class.
		 */
		WindowConfigReader cr = new ConfigReader(this.getConfigPath());

		this.width = (int)(long) cr.getWindowWidth();
        	this.height = (int)(long) cr.getWindowHeight();
		
		/*
		NOTE:
			- Here we are using the `PlayerConfigReader` interface to load the configs
			for the player from the JSON file.

			- And then, we initialize the player.
		*/
		PlayerConfigReader pcr = new ConfigReader(this.getConfigPath());

		Coordinates plCoordinates = pcr.getPlayerCoordinates();
		String plColour = pcr.getPlayerColour();
		int plSpeed = (int)(long) pcr.getPlayerSpeed();
		int plLives = (int)(long) pcr.getPlayerLives();

		player = new Player(plCoordinates, plColour, plSpeed, plLives);

		renderables.add(player);
	}

	public String getConfigPath(){
		/*
		NOTE:
			- Just returns the config path to the JSON file.
		*/
		return this.configPath;
	}

	public int getWindowWidth(){
		/*
		NOTE:
			- Just returns the width of the window loaded using the JSON file in the
			constructor.
		 */
		return this.width;
	}

	public int getWindowHeight(){
		/*
		NOTE:
			- Just returns the height of the window loaded using the JSON file in the
			constructor.
		 */
		return this.height;
	}

	public void update(){
		/*
		NOTE: 
			- Updates the game with every frame.
		*/

		movePlayer();
		for(Sprite sprite: sprites){
			sprite.update();
		}

		/*
		NOTE:
			- Ensuring that the renderable foreground sprites don't go 
			off-screen. 

			- `ro` might stand for "renderable objects". 
		*/
		for(Renderable ro: renderables){
			if(!ro.getLayer().equals(Renderable.Layer.FOREGROUND)){
				continue;
			}

			/*
			NOTE:
				- What are all these magic numbers? I thought good design
				was being preached here?

				- I got rid of the magic numbers.
			 */
			
			if(ro.getPosition().getX() + ro.getWidth() >= this.getWindowWidth()) {
				ro.getPosition().setX(this.getWindowWidth() - 1 -ro.getWidth());
			}

			if(ro.getPosition().getX() <= 0) {
				ro.getPosition().setX(1);
			}

			if(ro.getPosition().getY() + ro.getHeight() >= this.getWindowHeight()) {
				ro.getPosition().setY(this.getWindowHeight() - 1 -ro.getHeight());
			}

			if(ro.getPosition().getY() <= 0) {
				ro.getPosition().setY(1);
			}
		}
	}

	public List<Renderable> getRenderables(){
		return renderables;
	}

	/*
	NOTE:
		- Mutators for released and pressed buttons.
	*/
	public void leftReleased() {
		this.left = false;
	}

	public void rightReleased(){
		this.right = false;
	}

	public void leftPressed() {
		this.left = true;
	}
	public void rightPressed(){
		this.right = true;
	}

	public boolean shootPressed(){
		player.shoot();
		return true;
	}

	private void movePlayer(){
		if(left){
			player.left();
		}

		if(right){
			player.right();
		}
	}
}
