package invaders.engine;

import java.util.*;

import invaders.*;
import invaders.entities.*;
import invaders.entities.builders.*;
import invaders.entities.strategies.FastStraight;
import invaders.entities.strategies.SlowStraight;
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

	private List<Renderable> renderables;
	private List<Renderable> deletables;

	private Player player;
	private Projectile playerProjectile;
	private List<Bunker> bunkers;

	private boolean left;
	private boolean right;

	public GameEngine(String configPath){
		/*
		SECTION 1:
			- We are using the `ConfigReader` to get each configuration specified
			by the config file.
		*/

		this.configPath = configPath;

		this.renderables = new ArrayList<Renderable>();
		this.deletables = new ArrayList<Renderable>();

		/*
		SECTION 2:
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
		SECTION 3:
			- Here we are using the `PlayerConfigReader` interface to load the configs
			for the player from the JSON file.

			- And then, we initialize the player.
		*/
		PlayerConfigReader pcr = new ConfigReader(this.getConfigPath());

		Coordinates plCoordinates = pcr.getPlayerCoordinates();
		String plColour = pcr.getPlayerColour();
		int plSpeed = (int)(long) pcr.getPlayerSpeed();
		int plLives = (int)(long) pcr.getPlayerLives();

		this.player = new Player(plCoordinates, plColour, plSpeed, plLives);

		/*
		SECTION 4:
			-  Intializing all of the bunker objects using the Builder pattern. This 
			`GameEngine` class is the client and it calls on the director to deliver
			the final product. 

			- And then the bunkers are added to renderables to render.
		*/
		BunkerDirector bunkerDirector = new BunkerDirector(new DefaultBunkerBuilder());
		this.bunkers = bunkerDirector.makeRegularBunkers(this.getConfigPath());

		/*
		SECTION 5:
			- Adding things to renderables.
		 */
		this.renderables.add(this.player);
		this.renderables.addAll(this.bunkers);
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

	public void removeRenderable(Renderable renderable){
		/*
		NOTE:
			- Making this a function because we are going to need to remove
			a lot of things and this just makes it easier and reusable.
		 */
		this.renderables.remove(renderable);
		this.deletables.add(renderable);
	}

	public void updatePlayer(){
		/*
		NOTE:
			- We move the player if any movements is detected.
		 */
		this.movePlayer();
	}

	public void updateProjectiles(){
		if(this.playerProjectile != null){
			/*
			NOTE:
				- Updating the motion of the player's projectile.
			*/
			this.playerProjectile.up();

			/*
			NOTE:
				- We're checking if the player's projectile is colliding with any bunkers.
			*/
			for(Bunker bunker: this.bunkers){
				if(this.playerProjectile.isColliding(bunker)){
					/*
					NOTE:
						- If it is then we remove the player projectile from the list of 
						renderables and make the projectile null.
					*/
					this.removeRenderable(this.playerProjectile);
					this.playerProjectile = null;
					break;
				}
			}
		}
		
		/*
		NOTE:
			- Now we need to check if it's null again because it might have
			been deleted while colliding with the bunkers in the previous 
			block.
		*/
		if(this.playerProjectile != null){
			final int WINDOW_TOP_BOUNDARY = 0;

			/*
			NOTE:
				- Here, we remove it if it has reached the top of the window.
			*/
			if(this.playerProjectile.getPosition().getY() <= WINDOW_TOP_BOUNDARY){
				this.removeRenderable(this.playerProjectile);
				this.playerProjectile = null;
			}
		}
	}

	public void updateBunkers(){
		/*
		NOTE:
			- This is to see if the bunkers need to be deleted.
		*/
		List<Bunker> removables = new ArrayList<Bunker>();

		for(Bunker bunker : this.bunkers){
			if(bunker.getDeleteStatus()){
				/*
				NOTE:
					- First, we remove them from rendering. If we delete the 
					bunker object from the bunkers list then we will absolutely
					run into a `ConcurrentModificationException`. Thus it has to 
					be divided into two separate sections.
				 */
				this.removeRenderable(bunker);
				removables.add(bunker);
			}
		}

		for(Bunker removable : removables){
			/*
			NOTE:
				- Then, we actually delete the object.
			 */
			this.bunkers.remove(removable);
		}
	}

	public void update(){
		/*
		NOTE: 
			- Updates the game for every frame.
		*/

		this.updatePlayer();
		this.updateProjectiles();
		this.updateBunkers();

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

	public List<Renderable> getDeletables(){
		/*
		NOTE:
			- We first use the copy constructor to make sure when we clear the
			deletables from the list of deletable objects, the return list isn't
			empty.
		 */
		List<Renderable> temp = new ArrayList<Renderable>(this.deletables);

		this.deletables.clear();

		return temp;
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
		/*
		NOTE:
			- We need to check if the player projectile has existed yet. 

			- If not, we allocate a new projectile object and we pass the player's
			coordinates in but we modify the starting position a little bit to 
			center the projectile object. 
		*/
		
		if(this.playerProjectile == null){
			final double DUMMY = 0;

			/*
			NOTE:
				- Finding the center of the player object but also taking into
				account the width and height of the projectile.
			 */
			int projectileWidth = (int) new Projectile(new Coordinates(DUMMY, DUMMY)).getWidth();
			int projectileHeight = (int) new Projectile(new Coordinates(DUMMY, DUMMY)).getHeight();

			double playerCenterX = this.player.getPosition().getX() + this.player.getWidth() / 2 -
				projectileWidth / 2;
			
			double startY = this.player.getPosition().getY() - projectileHeight;

			this.playerProjectile = new Projectile(new Coordinates(playerCenterX, startY));
			this.playerProjectile.setStrategy(new FastStraight());

			/*
			NOTE:
				- Adding the player's projectile to renderables and collidables.
			 */
			this.renderables.add(this.playerProjectile);
		}

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
