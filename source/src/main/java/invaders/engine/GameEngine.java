package invaders.engine;

import java.util.*;

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

	private boolean won = false;
	private boolean lost = false;
	private Status statusScreen;

	private List<Renderable> renderables;
	private List<Renderable> deletables;

	private Player player;
	private Projectile playerProjectile;
	private int playerDeathCounter = 0;
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
		
		final double statusX = this.width / 2 - new Status(new Coordinates(0, 0)).getWidth() / 2;
		final double statusY = this.height / 2 - new Status(new Coordinates(0, 0)).getHeight() / 2;
		this.statusScreen = new Status(new Coordinates(statusX, statusY));
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

		this.alienProjectiles = new ArrayList<Projectile>();

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

		/*
		NOTE:
			- Animating the player just a bit to let the player know
			that he or she has been hit by the projectile.
		 */
		if(this.playerDeathCounter > 0){
			this.playerDeathCounter++;
			this.player.setDeathImage();
		}

		/*
		NOTE:
			- We are going to let the player dissapear and reappear 
			in approximately a fifth of a second.
		 */
		final int INTERVAL_IN_FRAMES = 12;
		if(this.playerDeathCounter > INTERVAL_IN_FRAMES){
			this.playerDeathCounter = 0;
			this.player.setRegularImage();
		}

		if(player.getLives() <= 0){
			this.won = false;
			this.lost = true;
		}

		if(this.aliens.size() <= 0){
			this.won = true;
			this.lost = false;
		}
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

	public void updateEnemyProjectiles(){
		/*
		NOTE:
			- Picking three random aliens to shoot from.
		*/
		final int MAX_NUMBER_OF_ALIEN_PROJECTILES = 3;

		/*
		NOTE:
			- The variable below denotes how many aliens should be shooting simultaneously.
		 */
		int count_aliens_shoot_simultaneously = 1;

		List<Alien> randoms = this.pickRandomAliens(count_aliens_shoot_simultaneously);
		final int ARBITRARY_CHANCE = 150;

		if(this.alienProjectiles.size() < MAX_NUMBER_OF_ALIEN_PROJECTILES
			&& this.alienShootDecision(ARBITRARY_CHANCE)){
			/*
			NOTE:
				- Using the factory pattern to produce projectiles.
			*/
			ProjectileFactory factory = null;

			for(Alien random : randoms){
				if(random.getStrategy().equals("fast_straight")){
					factory = new FastProjectileFactory();
				}
				if(random.getStrategy().equals("slow_straight")){
					factory = new SlowProjectileFactory();
				}

				/*
				NOTE:
					- We produce alien projectiles using the factory pattern and
					we need to add it to the list of renderables as well.
				*/
				Projectile temp = factory.produceProjectile(random);
				this.alienProjectiles.add(temp);
				this.renderables.add(temp);
			}
		}

		/*
		NOTE:
			- Moving all projectiles downwards.
		*/
		List<Projectile> removables = new ArrayList<Projectile>();

		for(Projectile projectile : this.alienProjectiles){
			projectile.down();
			
			/*
			NOTE:
				- If a projectile goes out of the window boundaries then we remove
				that projectile from the list of renderables and we remove it from
				the list of projectiles.
			 */
			boolean markedForRemoval = false;

			if(projectile.getPosition().getY() + projectile.getHeight() >= this.height - 1){
				markedForRemoval = true;
			}

			if(projectile.isColliding(this.player)){
				markedForRemoval = true;
				this.playerDeathCounter++;
			}

			/*
			NOTE:
				- Check if the projectile is colliding with any bunkers. Breaking
				early because we don't want to be looping through the remaining.
			 */
			for(Bunker bunker : this.bunkers){
				if(projectile.isColliding(bunker)){
					markedForRemoval = true;
					break;
				}
			}

			/*
			NOTE:
				- Removing both projectiles if they collide.
			 */
			if(this.playerProjectile != null){
				if(projectile.isColliding(this.playerProjectile)){
					markedForRemoval = true;
					this.removeRenderable(this.playerProjectile);
					removables.add(this.playerProjectile);
					this.playerProjectile = null;
				}
			}

			if(markedForRemoval){
				this.removeRenderable(projectile);
				removables.add(projectile);
			}
		}

		for(Projectile removable : removables){
			/*
			NOTE:
				- Then, we actually delete the object.
			 */
			this.alienProjectiles.remove(removable);
		}
	}

	public void updateBunkers(){
		/*
		NOTE:
			- This is to see if the bunkers have collided with an enemy
		*/
		for(Bunker bunker : this.bunkers){
			for(Alien enemy : this.aliens){
				enemy.isColliding(bunker);
			}
		}
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
			- We check the aliens' collisions with the player.
		*/
		for(Alien alien : this.aliens){
			alien.isColliding(this.player);
		}

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

	public void displayGameStatus(){
		/*
		NOTE:
			- This is sort of a band-aid solution because I haven't figured out
			how to output text to the screen but time is of the essence so let's
			make do with whatever we have.
		 */
		List<Renderable> currentRenderables = new ArrayList<Renderable>(this.renderables);

		for(Renderable renderable : currentRenderables){
			this.removeRenderable(renderable);
		}

		if(this.won){
			this.statusScreen.setWin();
			this.renderables.add(statusScreen);
		}else if(this.lost){
			this.statusScreen.setLose();
			this.renderables.add(statusScreen);
		}
	}

	public void update(){
		/*
		NOTE: 
			- Updates the game for every frame.
		*/

		if(this.won || this.lost){
			this.displayGameStatus();
			return;
		}

		this.updatePlayer();
		this.updatePlayerProjectile();
		this.updateBunkers();
		this.updateAliens();
		this.updateEnemyProjectiles();

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

    private List<Alien> pickRandomAliens(int amount) {
        if (amount >= this.aliens.size()) {
            /*
			NOTE:
				- Return a copy of the input list if count is greater than or equal to the list size.
			*/
            return new ArrayList<>(this.aliens);
        }

        Random random = new Random();
        List<Alien> randomAliens = new ArrayList<>();
		
		/*
		NOTE:
			- Creating a copy to avoid modifying the original list.
		 */
        List<Alien> temp = new ArrayList<Alien>(this.aliens); 

        for (int i = 0; i < amount; i++) {
            int randomIndex = random.nextInt(temp.size());
            randomAliens.add(temp.remove(randomIndex)); // Remove and add the random element
        }

        return randomAliens;
    }

	private boolean alienShootDecision(int chance){
		/*
		NOTE:
			- We produce a 1 in `chance` that the enemy is going to shoot.
			The higher the chance the less likely the enemy is going to 
			shoot.
		 */
		Random random = new Random();

		int randomNumber = random.nextInt(chance);

		final int BENCHMARK = 0;
		if(randomNumber == BENCHMARK){
			return true;
		}

		return false;
	}

}