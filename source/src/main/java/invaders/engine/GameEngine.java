package invaders.engine;

import java.util.*;

import invaders.*;
import invaders.entities.*;
import invaders.entities.builders.*;
import invaders.entities.factories.*;
import invaders.physics.*;
import invaders.rendering.*;
import invaders.filehandler.*;

enum Direction{
	LEFT,
	RIGHT,
	UP,
	DOWN,
	NONE
}

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
	private List<Alien> aliens;
	private List<Projectile> alienProjectiles;
	private Direction alienHerdDirection;
	private int frameCountDescend = 0;
	private boolean herdDescent = false;
	
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
			- Initializing all of the bunker objects using the Builder pattern. This 
			`GameEngine` class is the client and it calls on the director to deliver
			the final product. 

			- And then the bunkers are added to renderables to render.
		*/
		BunkerDirector bunkerDirector = new BunkerDirector(new DefaultBunkerBuilder());
		this.bunkers = bunkerDirector.makeRegularBunkers(this.getConfigPath());

		/*
		SECTION 5:
			- Initializing all enemy objects using the Builder pattern as well.
		*/
		AlienDirector alienDirector = new AlienDirector(new DefaultAlienBuilder());
		this.aliens = alienDirector.makeRegularAliens(this.getConfigPath());
		this.alienHerdDirection = Direction.NONE;

		/*
		SECTION 6:
			- Adding things to renderables.
		 */
		this.renderables.add(this.player);
		this.renderables.addAll(this.bunkers);
		this.renderables.addAll(this.aliens);
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

	public void updatePlayerProjectile(){
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

			/*
			NOTE:
				- Now we're checking if the player's projectile is colliding with 
				an enemy.
			 */
			if(playerProjectile != null){
				for(Alien alien : this.aliens){
					if(this.playerProjectile.isColliding(alien)){
						/*
						NOTE:
							- We do the same thing as we did for bunkers.
						*/
						this.removeRenderable(this.playerProjectile);
						this.playerProjectile = null;
						break;
					}
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

	public void updateEnemyProjectile(){
		
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

	public void updateAliens(){
		/*
		NOTE:
			- We move the aliens first.
		 */
		this.moveAliens();

		/*
		NOTE:
			- We check if there are any aliens that need to be removed.
		*/
		List<Alien> removables = new ArrayList<Alien>();

		boolean increaseSpeed = false;

		for(Alien alien : this.aliens){
			if(alien.getDeleteStatus()){

				increaseSpeed = true;
				this.removeRenderable(alien);
				removables.add(alien);
			}
		}

		for(Alien removable : removables){
			/*
			NOTE:
				- Again, we don't want to run into a `ConcurrentModificationException`
				so we need to remove it separately.
			*/
			this.aliens.remove(removable);
		}

		/*
		NOTE:
			- Now we increase all the alien's speeds if the player projectile has hit
			one of the enemies.
		*/
		if(increaseSpeed){
			for(Alien alien : this.aliens){
				alien.increaseSpeed();
			}
		}
	}

	public void update(){
		/*
		NOTE: 
			- Updates the game for every frame.
		*/

		this.updatePlayer();
		this.updatePlayerProjectile();
		this.updateBunkers();
		this.updateAliens();

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
			/*
			NOTE:
				- We use the factory method to produce the player's projectile.
				The player is needed as an argument as it is a shooting entity.
			 */
			ProjectileFactory factory = new SlowProjectileFactory();
			this.playerProjectile = factory.produceProjectile(this.player);

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

	private void moveAliens(){
		/*
		NOTE:
			- This is to be called in `updateAliens()`. 
		*/

		/*
		NOTE:
			- Initially, it is set to NONE because we don't know where the aliens
			are set. 
		*/
		if(this.alienHerdDirection == Direction.NONE){
			this.alienHerdDirection = Direction.LEFT;
			//this.alienHerdDirection = Direction.RIGHT;
		}

		final int FRAMES_IN_ONE_SECOND = 60;
		if(this.herdDescent){
			/*
			NOTE:
				- Resetting the flag and not the counter. The counter we reset
				at the end.
			 */
			this.frameCountDescend++;

			if(this.frameCountDescend > FRAMES_IN_ONE_SECOND){
				this.herdDescent = false;
			}
		}

		/*
		NOTE:
			- Now we loop through the list of aliens.
		*/
		for(Alien alien : this.aliens){
			final int windowMarginLeft = 1;
			final int windowMarginRight = this.getWindowWidth() - 1;

			if(this.herdDescent){
				alien.down();
				continue;
			}

			/*
			NOTE:
				- This is checking if there are any enemy objects hitting the
				left or the right bound of the window. If there is, we set the
				herd's direction accordingly.
			 */

			if(alien.getPosition().getX() <= windowMarginLeft){
					/*
					NOTE:
						- We flag to let the whole herd descend towards the player for 
						one second if the herd has hit the edge of the window.
					*/

					this.herdDescent = true;
					this.alienHerdDirection = Direction.RIGHT;
					break;

			}else if(alien.getPosition().getX() + alien.getWidth() >= windowMarginRight){
					/*
					NOTE:
						- Same thing here. We let the herd descend but our horizontal
						direction changes.
					*/

					this.herdDescent = true;
					this.alienHerdDirection = Direction.LEFT;
					break;
			}
			
			/*
			NOTE:
				- Switching directions.
			*/
			if(this.alienHerdDirection == Direction.LEFT){
				alien.left();
			}

			if(this.alienHerdDirection == Direction.RIGHT){
				alien.right();
			}
		}

		/*
		NOTE:
			- Resetting the counter here.
		 */

		if(this.frameCountDescend > FRAMES_IN_ONE_SECOND){
			this.frameCountDescend = 0;

			/*
			NOTE:
				- This is a sort of a band-aid solution to get the aliens bounce off 
				the margins a little bit. If this isn't here then the aliens will
				keep on descending forever.
			 */
			for(Alien alien : this.aliens){
				if(this.alienHerdDirection == Direction.LEFT){
					alien.left();
				}

				if(this.alienHerdDirection == Direction.RIGHT){
					alien.right();
				}
			}
		}
	}
}
