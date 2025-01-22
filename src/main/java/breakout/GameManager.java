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

  /**
   * Initial state of game; starting splash screen with reset lives and score.
   * Adapted from code authored by ChatGPT for displaying text.
   */
  public void startScreen() {
    // Sets number of lives, current score, etc. to initial state
    setVariablesToInitialState();

    // Create root and add title-screen text
    mySceneRoot = new Group();
    addTextToRoot(GameText.getStartScreenText(START_LIVES, myHighScore));

    initializeScene();
  }

  private void setVariablesToInitialState() {
    myLivesRemaining = START_LIVES;
    myCurrentLevelNumber = 0;
    myScore = 0;

    myScoreText = GameText.getScoreText(myScore);
    myLivesText = GameText.getLivesText(myLivesRemaining);
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

    return addMyElementsToRoot();
  }

  private Group addMyElementsToRoot() {
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
        actionAfterLevelCompleted();
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

  private void actionAfterLevelCompleted() {
    // When there are no more levels
    if (getNextLevelNumber() < 0) {
      whenGameEnd(true);
    }
    // Otherwise, there is another level to continue onto
    else {
      addTextToRoot(GameText.getLevelCompleteText(myCurrentLevelNumber));
      myAnimation.stop();
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
      myLivesText.setText(GameText.livesToString(myLivesRemaining));
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
    myScoreText.setText(GameText.scoreToString(myScore));
  }

  /**
   * @param newScore Game score to compare to the current high score
   * @return True if new high score, false if not new high score
   */
  private boolean checkAndUpdateHighScore(int newScore) {
    if (newScore > myHighScore) {
      myHighScore = newScore;
      return true;
    }
    return false;
  }

  /**
   * Actions taken when a player loses the game (has no lives remaining)
   */
  private void whenPlayerLoses() {
    myLivesText.setText(GameText.livesToString(myLivesRemaining));
    myLivesText.setFill(Color.RED);
    // Display game-end splash screen
    whenGameEnd(false);
  }

  /**
   * When game is over
   */
  private void whenGameEnd(boolean playerWon) {
    boolean newHighScore = checkAndUpdateHighScore(myScore);
    addTextToRoot(GameText.getEndingText(playerWon, newHighScore, myHighScore, myScore));
    myAnimation.stop();
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
    // Cheat key Q: return to start screen. Updates high score, resets score and lives.
    if (code == KeyCode.Q) {
      setHighScoreResetGame();
    }
    // Cheat key R: reset paddle and ball to starting positions.
    if (code == KeyCode.R) {
      resetPaddleAndBalls();
    }
    if (code == KeyCode.SPACE) {
      handleSpaceBar();
    }
  }

  private void setHighScoreResetGame() {
    checkAndUpdateHighScore(myScore);
    myAnimation.stop();
    startScreen();
  }

  private void resetPaddleAndBalls() {
    myPaddle.reset();
    for (Ball ball : myBalls) {
      ball.reset();
    }
  }

  private void handleSpaceBar() {
    // If game hasn't started, then pressing space opens first level
    if (myCurrentLevelNumber == 0) {
      startLevel(myLevelNumbers.getFirst());
    }
    // If a level has been completed, then pressing space initializes the next level
    else if (checkLevelComplete()) {
      int nextLevelNumber = getNextLevelNumber();
      // Unless there are no more levels, then it redirects to start screen to restart game
      if (nextLevelNumber < 0) {
        startScreen();
      }
      else {
        startLevel(nextLevelNumber);
      }
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

  /**
   * @return -1 if no more levels remaining, otherwise returns level number of next level in List
   */
  private int getNextLevelNumber() {
    int index = myLevelNumbers.indexOf(myCurrentLevelNumber);
    int nextIndex = index + 1;
    if (nextIndex >= myLevelNumbers.size()) {
      return -1;
    }
    return myLevelNumbers.get(nextIndex);
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

  private void addTextToRoot(Text text) {
    mySceneRoot.getChildren().add(text);
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
