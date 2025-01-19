package breakout;

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
    public static final int SCENE_WIDTH = 1100;
    public static final int SCENE_HEIGHT = 850;
//    public static final int BLOCK_WIDTH = 50;
//    public static final int BLOCK_HEIGHT = 20;
    public static final int FRAMES_PER_SECOND = 60;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    // public static final String RESOURCE_PATH = "/resources/";
    public static final String BALL_IMAGE = "/ball/basketball.png";

    private List<Ball> myBalls;

    /**
     * Initialize what will be displayed.
     */
    @Override
    public void start(Stage stage) {
        Paddle paddle = new Paddle(SCENE_WIDTH, SCENE_HEIGHT);
        Ball ball = new Ball(new Image(getClass().getResourceAsStream(BALL_IMAGE)), SCENE_WIDTH, SCENE_HEIGHT);
        myBalls = new ArrayList<>();
        myBalls.add(ball);

        Group root = new Group();
        root.getChildren().add(paddle.getShape());
        root.getChildren().add(ball.getView());

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
        for (Ball b : myBalls) {
            b.bounceEdge(SCENE_WIDTH, SCENE_HEIGHT);
            b.bouncePaddle(p);
            b.move(elapsedTime);
            if (b.isContactingFloor(SCENE_HEIGHT)) {
                b.stopMotion();
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
