package invaders.physics;

public class Coordinates {

	private double x;
	private double y;

	public Coordinates(double x, double y){
		/*
		NOTE:
			- This class should be self-explanatory.
		 */
		this.x = x;
		this.y = y;
	}

	public double getX(){ return this.x;}
	public double getY(){ return this.y;}

	public void setX(double x){ this.x = x;}
	public void setY(double y){ this.y = y;}
}
