package invaders.entities;

import invaders.engine.*;
import invaders.physics.*;
import invaders.rendering.*;

import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.*;

public class SpaceBackground implements Renderable {
	private Rectangle space;
	private Pane pane;
    private GameEngine model;

	public SpaceBackground(GameEngine engine, Pane pane){
		double width = pane.getWidth();
		double height = pane.getHeight();
		space = new Rectangle(0, 0, width, height);
		space.setFill(Paint.valueOf("BLACK"));
		space.setViewOrder(1000.0);

		pane.getChildren().add(space);
	}

	public Image getImage() { return null;}

	@Override
	public double getWidth() { return 0;}

	@Override
	public double getHeight() { return 0;}

	@Override
	public Coordinates getPosition() { return null;}

	@Override
	public Layer getLayer() { return Layer.BACKGROUND;}
}
