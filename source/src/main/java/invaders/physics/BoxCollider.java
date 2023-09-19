package invaders.physics;

public class BoxCollider implements Collider {

    private double width;
    private double height;
    private Coordinates position;

    public BoxCollider(double width, double height, Coordinates position){
        this.height = height;
        this.width = width;
        this.position = position;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public Coordinates getPosition(){
        return this.position;
    }
}
