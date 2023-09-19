package invaders.engine;

import javafx.scene.input.*;
import javafx.scene.media.*;

import java.net.*;
import java.util.*;

class GameInput {
    private final GameEngine model;

    private boolean left = false;
    private boolean right = false;

    private Set<KeyCode> pressedKeys = new HashSet<>();

    private Map<String, MediaPlayer> sounds = new HashMap<>();

    public GameInput(GameEngine model) {
        this.model = model;

        /*
        NOTE:
            - Is there a better place for this code?
        */
        URL mediaUrl = getClass().getResource("/shoot.wav");
        String jumpURL = mediaUrl.toExternalForm();

        Media sound = new Media(jumpURL);
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        sounds.put("shoot", mediaPlayer);
    }

    public void handlePressed(KeyEvent keyEvent) {
        /*
        NOTE:
            - What happens when we pressed a certain key?
         */
        if (pressedKeys.contains(keyEvent.getCode())) {
            return;
        }
        pressedKeys.add(keyEvent.getCode());

        /*
        NOTE:
            - In this case, if we should, the shoot sound gets played.
         */
        if (keyEvent.getCode().equals(KeyCode.SPACE)) {
            if (model.shootPressed()) {
                MediaPlayer shoot = sounds.get("shoot");
                shoot.stop();
                shoot.play();
            }
        }

        /*
        NOTE:
            - In this case, if it is the left or right keys then the left
            and right booleans are set accordingly.
        */
        if (keyEvent.getCode().equals(KeyCode.LEFT)) {
            left = true;
        }
        if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            right = true;
        }

        /*
        NOTE:
            - Calling the `GameEngine` model. If left is pressed the `leftPressed()`
            is called where it moves the player to the left. Similarly, if the right
            arrow key is pressed then the player gets moved to the right.
         */
        if (left) {
            model.leftPressed();
        }

        if(right){
            model.rightPressed();
        }
    }

    public void handleReleased(KeyEvent keyEvent) {
        /*
        NOTE:
            - This is what is called when the player releases a key.

            - We remove the key that's being stored in the hash map first and
            then we check if the key that was released is equivalent to the left
            or the right arrow key. 
        */
        pressedKeys.remove(keyEvent.getCode());

        if (keyEvent.getCode().equals(KeyCode.LEFT)) {
            left = false;
            model.leftReleased();
        }
        if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            model.rightReleased();
            right = false;
        }
    }
}
