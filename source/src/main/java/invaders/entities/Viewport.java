package invaders.entities;

import invaders.physics.*;
import invaders.rendering.*;
import javafx.scene.*;
import javafx.scene.image.*;

public class Viewport {
    /*
    NOTE:
        - The viewport contains the entity, its position, whether
        it's being marked for delete, and node?
    */
    private Renderable entity;
    private Coordinates position;
    private boolean delete = false;
    private ImageView node;

    public Viewport(Renderable entity) {
        /*
        NOTE:
            - Just setting all the attributes. 
        */
        this.entity = entity;
        this.position = entity.getPosition();

        /*
        NOTE:
            - Setting the viewport's image to the entity's image. 
        */
        node = new ImageView(entity.getImage());
        node.setViewOrder(getViewOrder(entity.getLayer()));

        update(0.0, 0.0);
    }

    private static double getViewOrder(Renderable.Layer layer) {
        /*
        NOTE:
            - Switching the layer, background, and foreground.
        */
        switch (layer) {
            case BACKGROUND: return 100.0;
            case FOREGROUND: return 50.0;
            case EFFECT: return 25.0;
            default: throw new IllegalStateException("Error: javac doesn't understand how enums work.");
        }
    }

    public void update(double xViewportOffset, double yViewportOffset) {
        /*
        NOTE:
            - Checking if the image in this viewport is the same as the image 
            stored in the sprite entity.
         */
        if (!node.getImage().equals(entity.getImage())) {
            node.setImage(entity.getImage());
        }

        /*
        NOTE:
            - We're setting new coordinates if the viewport offset is not 0.
        */
        node.setX(position.getX() - xViewportOffset);
        node.setY(position.getY() - yViewportOffset);

        /*
        NOTE:
            - Setting the width and the heigh of the viewport based on the sprite
            entity because the entity might shrinks or stretch or expand and the
            viewport needs to be able to capture this information and display it.
         */
        node.setFitHeight(entity.getHeight());
        node.setFitWidth(entity.getWidth());
        node.setPreserveRatio(true);

        /*
        NOTE:
            - I don't know why this is being set to false? Is it to ensure that 
            entities that are accidentally marked for deletion don't actually
            end up being marked for deletion?
        */
        delete = false;
    }

    public boolean matchesEntity(Renderable entity) { return this.entity.equals(entity); }

    public void markForDelete() { delete = true;}

    public Node getNode() { return node;}

    public boolean isMarkedForDelete() { return delete;}
}
