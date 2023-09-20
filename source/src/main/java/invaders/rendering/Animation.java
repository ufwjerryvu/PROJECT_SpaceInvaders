package invaders.rendering;

import javafx.scene.image.*;

public interface Animation {
    public String getName();
    public Image getCurrentFrame();
    public void next();
}
