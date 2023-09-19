package invaders.rendering;

import invaders.*;
import invaders.physics.*;
import invaders.engine.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

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
