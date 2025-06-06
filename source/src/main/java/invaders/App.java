package invaders;

import javafx.application.*;
import javafx.stage.*;

import invaders.engine.*;

import java.util.*;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Map<String, String> params = getParameters().getNamed();

        /*
        NOTE:
            - GameEngine must be fed into the GameWindow object to 
            read configurations and other things.
         */
        GameEngine model = new GameEngine("src/main/resources/config.json");
        GameWindow window = new GameWindow(model);
        window.run();

        stage.setTitle("Space Invaders");
        stage.setScene(window.getScene());
        stage.show();

        window.run();
    }
}
