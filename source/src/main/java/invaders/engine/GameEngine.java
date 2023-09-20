package invaders.engine;

import java.util.*;

import invaders.Sprite;
import invaders.entities.*;
import invaders.physics.Coordinates;
import invaders.rendering.*;

public class GameEngine {
	/*
	NOTE:
		- This class manages the main loop and logic of the game.
	*/
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

		sprites = new ArrayList<Sprite>();
		renderables = new ArrayList<Renderable>();

		player = new Player(new Coordinates(200, 380));
		renderables.add(player);
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
			 */
			if(ro.getPosition().getX() + ro.getWidth() >= 640) {
				ro.getPosition().setX(639-ro.getWidth());
			}

			if(ro.getPosition().getX() <= 0) {
				ro.getPosition().setX(1);
			}

			if(ro.getPosition().getY() + ro.getHeight() >= 400) {
				ro.getPosition().setY(399-ro.getHeight());
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
