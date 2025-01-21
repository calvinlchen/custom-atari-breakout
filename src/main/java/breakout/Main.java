package breakout;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.image.Image;


/**
 * @author Robert C. Duvall, Calvin Chen
 */
public class Main extends Application {

    // useful names for constant values used
    public static final String TITLE = "Basketball Bouncer";
    public static final Color DUKE_BLUE = new Color(0, 0.325, 0.608, 1);
    public static final int SCENE_WIDTH = 1000;
    public static final int SCENE_HEIGHT = 750;
//    public static final int BLOCK_WIDTH = 50;
//    public static final int BLOCK_HEIGHT = 20;
    public static final int FRAMES_PER_SECOND = 60;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    // public static final String RESOURCE_PATH = "/resources/";
    public static final String BALL_IMAGE = "/ball/basketball.png";
    public static final String LEVEL_MAP_01 = "/maps/lvl_01.txt";

    private List<Ball> myBalls;
    private List<Block> myBlocks;

    /**
     * Initialize what will be displayed.
     */
    @Override
    public void start(Stage stage) {
        LevelMap level01 = new LevelMap(LEVEL_MAP_01, SCENE_WIDTH, SCENE_HEIGHT);

        Paddle paddle = new Paddle(SCENE_WIDTH, SCENE_HEIGHT);

        Ball ball = new Ball(new Image(getClass().getResourceAsStream(BALL_IMAGE)), SCENE_WIDTH, SCENE_HEIGHT);
        myBalls = new ArrayList<>();
        myBalls.add(ball);

        myBlocks = new ArrayList<>();

        for (Block[] blockRow : level01.getBlocks()) {
          myBlocks.addAll(Arrays.asList(blockRow));
        }

        Group root = new Group();
        root.getChildren().add(paddle.getShape());
        for (Ball b : myBalls) {
            root.getChildren().add(b.getView());
        }
        for (Block b : myBlocks) {
            root.getChildren().add(b.getShape());
        }

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, DUKE_BLUE);
        stage.setScene(scene);
        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode(), paddle));



        stage.setTitle(TITLE);
        stage.show();
        // attach "game loop" to timeline to play it (basically just calling step() method repeatedly forever)
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(paddle, SECOND_DELAY)));
        animation.play();


    }

    private void step (Paddle p, double elapsedTime) {
        for (Ball ball : myBalls) {
            ball.bounceOffEdge(SCENE_WIDTH);
            ball.bounceOffPaddle(p);

            for (Block block : myBlocks) {
                if (block.checkIfShouldBounce(ball)) {
                    block.bounceBall(ball);
                    block.hitBlockActions();
                    // stops the entire loop after the first bounce
                    break;
                }
            }

            ball.move(elapsedTime);

            if (ball.isContactingFloor(SCENE_HEIGHT)) {
                ball.stopMotion();
            }
        }
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
