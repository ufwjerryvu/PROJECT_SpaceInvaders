package invaders.physics;

public interface Collider {

    public double getWidth();

    public double getHeight();

    public Coordinates getPosition();

    public default boolean isColliding(Collider col) {
        /*
        NOTE:
            - Getting the coordinates of the first object.
         */
        double oneLeftX = this.getPosition().getX();
        double oneRightX = this.getPosition().getX() + this.getWidth();
        double oneTopY = this.getPosition().getY();
        double oneBottomY = this.getPosition().getY() + this.getHeight();

        /*
        NOTE:
            - Then getting the coordinates of the second object.
         */
        double twoLeftX = col.getPosition().getX();
        double twoRightX = col.getPosition().getX() + col.getWidth();
        double twoTopY = col.getPosition().getY();
        double twoBottomY = col.getPosition().getY() + col.getHeight();

        if (oneRightX < twoLeftX || twoRightX < oneLeftX) {
            /*
            NOTE:
                - Checking if there is any overlap in the x-axis.
             */
            return false;
        }

        if (oneBottomY < twoTopY || twoBottomY < oneTopY) {
            /*
            NOTE:
                - Checking if there is any overlap in the y-axis
             */
            return false; 
        }

        /*
        NOTE:
            - There is an overlap in both the x- and y-axes. 
         */
        return true;
    }
}