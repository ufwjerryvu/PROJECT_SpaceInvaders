package invaders.rendering;

import java.util.*;

public class Animator {
	private final List<Animation> animations;
	private Animation state;

	public Animator(List<Animation> animations){
		/*
		NOTE:
			- If we don't find anything in the animations list then
			we throw an exception. 
		 */
		if(animations.isEmpty()){
			throw new IllegalArgumentException("Error: no animations in the animations list.");
		}
		
		this.animations = animations;
		this.state = animations.get(0);
	}

	public void setState(String name){
		/*
		NOTE:
			- Just setting the next animation frame in the animation list.
		*/
		this.state = animations.stream().filter(a -> a.getName().equals(name)).findFirst().orElse(animations.get(0));
	}

	public Animation getState(){
		return this.state;
	}

}
