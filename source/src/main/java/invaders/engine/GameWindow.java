package invaders.engine;

import java.util.*;

import invaders.entities.*;
import invaders.rendering.*;
import invaders.filehandler.*;

import javafx.util.*;
import javafx.animation.*;
import javafx.scene.*;
import javafx.scene.layout.*;

public class GameWindow {
	private final int width;
    private final int height;

	private Scene scene;
    private Pane pane;
    private GameEngine model;

    private List<Viewport> viewports;
    private Renderable background;

    private double xViewportOffset = 0.0;
    private double yViewportOffset = 0.0;
    private static final double VIEWPORT_MARGIN = 280.0;

	public GameWindow(GameEngine model){
        /*
        NOTE:
            - We get the window configs using the accessors in the game engine model
            because the config has already been loaded there.
        */
        
        this.model = model;
        this.width = model.getWindowWidth();
        this.height = model.getWindowHeight();

        /*
        NOTE: 
            - Just initializing other things and storing information.
        */
        pane = new Pane();
        scene = new Scene(pane, width, height);
        this.background = new SpaceBackground(model, pane);

        GameInput sprites = new GameInput(this.model);

        scene.setOnKeyPressed(sprites::handlePressed);
        scene.setOnKeyReleased(sprites::handleReleased);

        viewports = new ArrayList<Viewport>();

    }

	public void run() {
        /*
        NOTE:
            - This is called the the master class App.java.
        */
         Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17), t -> this.draw()));

         timeline.setCycleCount(Timeline.INDEFINITE);
         timeline.play();
    }

    private void draw(){
        model.update();

        /*
        NOTE:
            - Draws all the renderables in the game engine model.
         */
        List<Renderable> renderables = model.getRenderables();
        for (Renderable entity : renderables) {
            boolean notFound = true;
            /*
            NOTE:
                - We are going through all the viewports to see if the renderable
                entity (sprite) stored in the viewport matches the entity in the
                list of renderable entities.
             */
            for (Viewport view : viewports) {
                if (view.matchesEntity(entity)) {
                    /*
                    NOTE:
                        - If found, we update the viewport. That is, we update the
                        new coordinates of the viewports. 
                     */
                    notFound = false;
                    view.update(xViewportOffset, yViewportOffset);
                    break;
                }
            }
            if (notFound) {
                /*
                NOTE:  
                    - If not found then we initialize a new viewport with the entity
                    (sprite) and add that to our list of viewports. 

                    - I don't know what `pane.getChildren().add(viewport.getNode())`
                    does. I'm assuming it has something to do with trees.
                 */
                Viewport viewport = new Viewport(entity);
                viewports.add(viewport);
                pane.getChildren().add(viewport.getNode());
            }
        }

        for (Viewport viewport : viewports) {
            /*
            NOTE:
                - If the viewport is marked for delete then we delete the entity
                from `pane`. This is probably useful for when we need to remove an
                enemy when it's shot. 
             */
            if (viewport.isMarkedForDelete()) {
                pane.getChildren().remove(viewport.getNode());
            }
        }
        viewports.removeIf(Viewport::isMarkedForDelete);
    }

	public Scene getScene() {
        return scene;
    }
}
