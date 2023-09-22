package invaders.rendering;

import invaders.physics.*;
import javafx.scene.image.*;

public interface Renderable {

    public Image getImage();

    public double getWidth();
    public double getHeight();

    public Coordinates getPosition();

    public Renderable.Layer getLayer();

    /*
    NOTE:
        - Setting the different layers.
    */
    public static enum Layer {
        BACKGROUND, FOREGROUND, EFFECT
    }
}
