package invaders.logic;

public interface Damagable {

	public void takeLives(int lives);

	public int getLives();

	public boolean isAlive();

}
