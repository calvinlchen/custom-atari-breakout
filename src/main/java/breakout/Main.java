package breakout;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;

/**
 * @author Robert C. Duvall, Calvin Chen
 */
public class Main extends Application {

    // useful names for constant values used
    public static final String TITLE = "Basketball Bouncer";
    public static final Color DUKE_BLUE = new Color(0, 0.325, 0.608, 1);
    public static final int SCENE_WIDTH = 1100;
    public static final int SCENE_HEIGHT = 850;
    public static final int BLOCK_WIDTH = 50;
    public static final int BLOCK_HEIGHT = 20;


    /**
     * Initialize what will be displayed.
     */
    @Override
    public void start(Stage stage) {
        Circle shape = new Circle(200, 200, 40);
        shape.setFill(Color.LIGHTSTEELBLUE);

        Rectangle block = new Rectangle(500, 500, BLOCK_WIDTH, BLOCK_HEIGHT);
        block.setFill(Color.LIGHTSTEELBLUE);

        Paddle paddle = new Paddle(SCENE_WIDTH, SCENE_HEIGHT);

        Group root = new Group();
        root.getChildren().add(shape);
        root.getChildren().add(block);
        root.getChildren().add(paddle.getShape());

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, DUKE_BLUE);
        stage.setScene(scene);
        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode(), paddle));


        stage.setTitle(TITLE);
        stage.show();
    }

    // Adapted from Robert C. Duvall's "bouncer" code
    private void handleKeyInput(KeyCode code, Paddle paddle) {
        if (code == KeyCode.RIGHT) {
            paddle.moveRight(SCENE_WIDTH);
        } else if (code == KeyCode.LEFT) {
            paddle.moveLeft();
        }
    }
}
