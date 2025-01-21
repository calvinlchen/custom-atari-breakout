package breakout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameManager {
  private int myLivesRemaining;
  private int myCurrentLevel;
  private int myScore;
  private int myHighScore;

  private Group mySceneRoot;
  private Stage myStage;
  private Paddle myPaddle;
  private List<Ball> myBalls;
  private List<Block> myBlocks;
  private Text myScoreText;
  private Text myLivesText;

  private int mySceneWidth;
  private int mySceneHeight;

  public static final String BALL_IMAGE = "/ball/basketball.png";
  public static final String MAPS_FILE_PREFIX = "/maps/";
  public static final String COLORS_FILE_PREFIX = "/colors/";

  public static final int FRAMES_PER_SECOND = 60;
  public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

  public static final int SCORE_INCREMENT = 100;

  public GameManager(int sceneWidth, int sceneHeight, Stage stage) {
    myLivesRemaining = 3;
    myCurrentLevel = 0;
    myScore = 0;
    myHighScore = 0;

    myScoreText = createScoreText();

    mySceneWidth = sceneWidth;
    mySceneHeight = sceneHeight;
    myStage = stage;

  }

  public void startLevel(int levelNumber) {
    myCurrentLevel = levelNumber;
    String levelName = Util.getLevelFilename(levelNumber);
    mySceneRoot = createSceneRoot(levelName);
    initializeScene();
  }

  /**
   * Add all necessary objects to a new root
   * @param levelName level number in "lvl_xx" format (i.e. lvl_01)
   * @return root of all scene objects
   */
  private Group createSceneRoot(String levelName) {
    // Create Block objects from file, store in LevelMap
    LevelMap levelMap = new LevelMap(MAPS_FILE_PREFIX+levelName+".txt",
        mySceneWidth, mySceneHeight);

    // Create paddle
    myPaddle = createPaddle();
    // Put ball in List
    resetToOneNewBall();
    // Put blocks in List
    resetBlocksToLevelMap(levelMap);

    // Create root with paddle, ball, blocks
    Group root = new Group();
    root.getChildren().add(myPaddle.getShape());
    for (Ball ball : myBalls) {
      root.getChildren().add(ball.getView());
    }
    for (Block block : myBlocks) {
      root.getChildren().add(block.getShape());
    }

    root.getChildren().add(myScoreText);

    return root;
  }

  /**
   * Initialize what will be displayed.
   * Adapted from code by Robert C. Duvall.
   */
  private void initializeScene() {
    Scene scene = new Scene(mySceneRoot, mySceneWidth, mySceneHeight, Color.BLACK);
    myStage.setScene(scene);
    myStage.setTitle(Main.TITLE);
    myStage.show();

    // respond to input
    scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));

    // attach "game loop" to timeline to play it (basically just calling step() method repeatedly forever)
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(new KeyFrame(
        Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY)));
    animation.play();
  }

  /**
   * Actions taken per step
   */
  private void step (double elapsedTime) {
    for (Ball ball : myBalls) {
      // Check and bounce off wall/ceiling if necessary
      ball.checkAndBounceEdge(mySceneWidth);

      // Check and bounce the ball off paddle if necessary
      myPaddle.checkAndBounceBall(ball);

      // Check and bounce the ball off a block if contacting
      checkBlocksAndBounceBall(ball);

      ball.move(elapsedTime);

      if (ball.isContactingFloor(mySceneHeight)) {
        whenBallHitsFloor(ball);
      }
    }
  }

  /**
   * Check all blocks and bounce the ball if it is contacting a block.
   * @param ball Ball object to bounce if contacting a block
   */
  private void checkBlocksAndBounceBall(Ball ball) {
    for (Block block : myBlocks) {
      if (block.checkIfShouldBounce(ball)) {
        block.bounceBall(ball);
        incrementScore();
        block.hitBlockActions();
        // skips rest of the loop after the first block bounce
        break;
      }
    }
  }

  /**
   * Actions taken if ball hits the bottom of the map.
   */
  private void whenBallHitsFloor(Ball ball) {
    myLivesRemaining--;
    if (myLivesRemaining > 0) {
      ball.reset();
    }
    else {
      ball.stopMotion();
      whenPlayerLoses();
    }
  }

  private void incrementScore() {
    myScore += SCORE_INCREMENT;
    myScoreText.setText(getScoreTextString());
  }

  /**
   * Actions taken when a player loses the game (has no lives remaining)
   */
  private void whenPlayerLoses() {

  }

  private Text createScoreText() {
    Text scoreText = new Text(getScoreTextString());
    scoreText.setFill(Color.WHITE);
    scoreText.setX(Main.SCENE_WIDTH * 0.1);
    scoreText.setY(Main.SCENE_HEIGHT * 0.95);
    scoreText.setFont(new Font(Main.BODY_FONT_SIZE));
    return scoreText;
  }

  private String getScoreTextString() {
    return "Score: " + myScore;
  }

  /**
   * Handle user keyboard input.
   * Adapted from code authored by Robert C. Duvall
   */
  private void handleKeyInput(KeyCode code) {
    if (code == KeyCode.RIGHT) {
      myPaddle.moveRight(mySceneWidth);
    } else if (code == KeyCode.LEFT) {
      myPaddle.moveLeft();
    }
    if (code == KeyCode.SPACE) {
      if (myLivesRemaining > 0) {
        setAllBallsInMotion();
      }
    }
  }

  private void setAllBallsInMotion() {
    for(Ball ball : myBalls) {
      if (!ball.isMoving()) {
        ball.startMotion();
      }
    }
  }

  /**
   * Creates a ball based on the provided image and scene dimensions.
   * @return new Ball object
   */
  private Ball createBall() {
    return new Ball(new Image(getClass().getResourceAsStream(BALL_IMAGE)), mySceneWidth, mySceneHeight);
  }

  /**
   * Creates a paddle based on the provided scene dimensions.
   * @return new Paddle object
   */
  private Paddle createPaddle() {
    return new Paddle(mySceneWidth, mySceneHeight);
  }

  /**
   * Replace myBalls with a single new ball, used when starting a new level
   */
  private void resetToOneNewBall() {
    // reset List
    myBalls = new ArrayList<>();

    // add new Ball object
    Ball ball = createBall();
    myBalls.add(ball);
  }

  /**
   * replaces existing myBlocks with new list of blocks based on a LevelMap
   * @param levelMap LevelMap object containing the layout of blocks for a scene
   */
  private void resetBlocksToLevelMap(LevelMap levelMap) {
    myBlocks = new ArrayList<>();
    for (Block[] blockRow : levelMap.getBlocks()) {
      myBlocks.addAll(Arrays.asList(blockRow));
    }
  }
}
