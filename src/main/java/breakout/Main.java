package breakout;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Robert C. Duvall, Calvin Chen
 */
public class Main extends Application {

    // useful names for constant values used
    public static final String TITLE = "Basketball Bouncer";
    public static final Color DUKE_BLUE = new Color(0, 0.325, 0.608, 1);
    public static final int SCENE_WIDTH = 1000;
    public static final int SCENE_HEIGHT = 750;

    /**
     * Start the application.
     */
    @Override
    public void start(Stage stage) {
        GameManager gameManager = new GameManager(SCENE_WIDTH, SCENE_HEIGHT, stage);
        gameManager.startLevel(1);
    }
}
