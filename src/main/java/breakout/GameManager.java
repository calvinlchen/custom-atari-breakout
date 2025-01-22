package breakout;

import java.util.ArrayList;
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
  private int myCurrentLevelNumber;
  private int myScore;
  private int myHighScore;
  private List<Integer> myLevelNumbers;

  private Group mySceneRoot;
  private Stage myStage;
  private Paddle myPaddle;
  private List<Ball> myBalls;
  private List<Block> myBlocks;
  private Text myScoreText;
  private Text myLivesText;

  private int mySceneWidth;
  private int mySceneHeight;

  private Timeline myAnimation;
  public static final int FRAMES_PER_SECOND = 60;
  public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

  public static final int SCORE_INCREMENT = 100;
  public static final int START_LIVES = 3;

  public GameManager(int sceneWidth, int sceneHeight, Stage stage, List<Integer> levelNumbers) {
    setVariablesToInitialState();
    myHighScore = 0;
    myLevelNumbers = levelNumbers;

    mySceneWidth = sceneWidth;
    mySceneHeight = sceneHeight;
    myStage = stage;
  }

  public void startScreen() {
    // Sets number of lives, current score, etc. to initial state
    setVariablesToInitialState();

    // Create a root node (Group, Pane, or any other layout)
    mySceneRoot = new Group();

    // Text for game instructions
    Text titleText = new Text(
        """
        Welcome to Basketball Bouncer!

        Break as many blocks with the ball as you can to score points.
        If the ball hits the bottom edge, you lose a life!
        (You start with """ + START_LIVES + """
         lives.)

        Move the paddle with your left and right arrow keys
        so that the ball doesn't hit the floor.

        High score: """ + myHighScore + """

        Press SPACE to start!
        """
    );

    titleText.setFill(Color.WHITE);
    titleText.setX(50);  // adjust for positioning
    titleText.setY(100);
    titleText.setFont(Font.font(24)); // choose a bigger font if you like

    mySceneRoot.getChildren().add(titleText);

    initializeScene();
  }

  private void setVariablesToInitialState() {
    myLivesRemaining = START_LIVES;
    myCurrentLevelNumber = 0;
    myScore = 0;

    myScoreText = createScoreText();
    myLivesText = createLivesText();
  }

  private void startLevel(int levelNumber) {
    // Stop any existing animation
    myAnimation.stop();

    myCurrentLevelNumber = levelNumber;
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
    LevelMap levelMap = new LevelMap(myCurrentLevelNumber,
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

    // Add Text elements
    root.getChildren().addAll(myScoreText, myLivesText);

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
    myAnimation = new Timeline();
    myAnimation.setCycleCount(Timeline.INDEFINITE);
    myAnimation.getKeyFrames().add(new KeyFrame(
        Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY)));
    myAnimation.play();
  }

  /**
   * Actions taken per step
   */
  private void step (double elapsedTime) {
    // Only run step methods if not on the start screen
    if (myCurrentLevelNumber > 0) {
      if (checkLevelComplete()) {
        myAnimation.stop();
      }

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
  }

  /**
   * Check all blocks and bounce the ball if it is contacting a block.
   * @param ball Ball object to bounce if contacting a block
   */
  private void checkBlocksAndBounceBall(Ball ball) {
    for (Block block : myBlocks) {
      if (block.checkIfShouldBounce(ball)) {
        incrementScore();
        block.hitBlockActions(ball);
        if (block.isBroken()) {
          myBlocks.remove(block);
        }
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
    if (! checkPlayerLost()) {
      myLivesText.setText(getLivesTextString());
      ball.reset();
    }
    else {
      ball.stopMotion();
      whenPlayerLoses();
    }
  }

  private boolean checkLevelComplete() {
    // System.out.println(myBlocks);
    return myBlocks.isEmpty();
  }

  private boolean checkPlayerLost() {
    return myLivesRemaining <= 0;
  }

  private void incrementScore() {
    myScore += SCORE_INCREMENT;
    myScoreText.setText(getScoreTextString());
  }

  private void checkAndUpdateHighScore(int newScore) {
    if (newScore > myHighScore) {
      myHighScore = newScore;
    }
  }

  /**
   * Actions taken when a player loses the game (has no lives remaining)
   */
  private void whenPlayerLoses() {
    myLivesText.setText(getLivesTextString());
    myLivesText.setFill(Color.RED);
    checkAndUpdateHighScore(myScore);
    myAnimation.stop();
  }

  private Text createScoreText() {
    Text scoreText = new Text(getScoreTextString());
    scoreText.setFill(Color.WHITE);
    scoreText.setX(Main.SCENE_WIDTH * 0.1);   // Place text near left edge of screen
    scoreText.setY(Main.SCENE_HEIGHT * 0.95); // Place text near bottom edge
    scoreText.setFont(new Font(Main.BODY_FONT_SIZE));
    return scoreText;
  }

  private Text createLivesText() {
    Text livesText = new Text(getLivesTextString());
    livesText.setFill(Color.WHITE);
    livesText.setX(Main.SCENE_WIDTH * 0.75);  // Place text near right edge of screen
    livesText.setY(Main.SCENE_HEIGHT * 0.95); // Place text near bottom edge
    livesText.setFont(new Font(Main.BODY_FONT_SIZE));
    return livesText;
  }

  private String getScoreTextString() {
    return "Score: " + myScore;
  }

  private String getLivesTextString() {
    return "Lives remaining: " + myLivesRemaining;
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
      // If game hasn't started, then pressing space opens first level
      if (myCurrentLevelNumber == 0) {
        startLevel(myLevelNumbers.getFirst());
      }
      // If a level has been completed, then pressing space initializes the next level
      else if (checkLevelComplete()) {
        startLevel(getNextLevelNumber());
      }
      // If the player has already lost, then pressing space goes back to the start screen
      else if (checkPlayerLost()) {
        startScreen();
      }
      // During a level, press space to set any non-moving balls into motion
      else {
        setStillBallsInMotion();
      }
    }
  }

  private int getNextLevelNumber() {
    int index = myLevelNumbers.indexOf(myCurrentLevelNumber);
    return myLevelNumbers.get(index + 1);
  }

  private void setStillBallsInMotion() {
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
    return new Ball(new Image(getClass().getResourceAsStream(Main.BALL_IMAGE)), mySceneWidth, mySceneHeight);
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
      for (Block block : blockRow) {
        if (! block.isBroken()) {
          myBlocks.add(block);
        }
      }
    }
  }
}
