package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Block {
  private int myHealth;

  private final Rectangle myShape;
  private final LevelMap myLevelMap;

  // Block color brightness changes depending on block health. More health = darker, less health =
  // brighter.
  public static final double MIN_BLOCK_BRIGHTNESS_FACTOR = 0.5; // when highest health value on map
  public static final double MAX_BLOCK_BRIGHTNESS_FACTOR = 1.5; // when at one/minimum health

  public Block(double xPosition, double yPosition, double width, double height, int healthCount,
      LevelMap levelMap) {
    // Create rectangle shape for block
    myShape = new Rectangle(xPosition, yPosition, width, height);
    myShape.setFill(Color.DARKRED);
    myShape.setStroke(Color.BLACK);
    myShape.setStrokeWidth(1);

    myHealth = healthCount;
    myLevelMap = levelMap;
  }

  /**
   * Creates a block with 0 health and broken-block characteristics
   */
  public static Block createEmptyBlock(double xPosition, double yPosition, double width,
      double height, LevelMap levelMap) {
    Block block = new Block(xPosition, yPosition, width, height, 0, levelMap);
    block.makeInvisible();
    return block;
  }

  /**
   * Creates a block for the LevelMap array with the proper characteristics based on the provided
   * health value (typically from the source file)
   */
  public static Block createBlockFor2dArrayWithParameters( int arrayMapRow, int arrayMapColumn,
      double blockWidth, double blockHeight, int healthValue, LevelMap levelMap) {
    if (healthValue < 1) {
      // If file integer is 0, create an off-screen "broken" block
      return Block.createEmptyBlock(arrayMapColumn*blockWidth,
          arrayMapRow*blockHeight, blockWidth, blockHeight, levelMap);
    }
    return new Block(arrayMapColumn*blockWidth, arrayMapRow*blockHeight,
        blockWidth, blockHeight, healthValue, levelMap);
  }

  /**
   * Returns internal shape object of block.
   */
  public Rectangle getShape() {
    return myShape;
  }

  /**
   * Returns remaining health/lives for block
   */
  public int getHealth() {
    return myHealth;
  }

  /**
   * Returns whether a block has zero health (true) or not (false)
   */
  public boolean isBroken() {
    return myHealth <= 0;
  }

  /**
   * Removes one health from a block.
   */
  public void decreaseHealth() {
    myHealth--;
  }

  /**
   * Returns whether a block is currently visible within a level scene based on color and outline
   */
  public boolean isVisible() {
    return myShape.getFill() == Color.TRANSPARENT && myShape.getStroke() == null;
  }

  /**
   * Makes a block disappear visually by removing color and outline
   */
  private void makeInvisible() {
    myShape.setFill(Color.TRANSPARENT);
    myShape.setStroke(null);
  }

  /**
   * Returns whether a ball is intersecting this block.
   * Adapted from code authored by ChatGPT.
   */
  public boolean intersects(Ball ball) {
    return (ball.getRightEdgeX() >= getX())         // ball's right edge >= block's left
        && (ball.getX() <= getRightEdgeX())         // ball's left edge  <= block's right
        && (ball.getBottomEdgeY() >= getY())        // ball's bottom    >= block's top
        && (ball.getY() <= getBottomEdgeY());       // ball's top       <= block's bottom
  }

  /**
   * Returns the amount of x-axis overlap between this block and the given ball.
   * Adapted from code authored by ChatGPT.
   */
  private double calculateXOverlap(Ball ball) {
    double ballRight = ball.getRightEdgeX();
    double blockRight = getRightEdgeX();
    return Math.min(ballRight, blockRight) - Math.max(ball.getX(), getX());
  }

  /**
   * Returns the amount of y-axis overlap between this block and the given ball.
   * Adapted from code authored by ChatGPT.
   */
  private double calculateYOverlap(Ball ball) {
    double ballBottom = ball.getBottomEdgeY();
    double blockBottom = getBottomEdgeY();
    return Math.min(ballBottom, blockBottom) - Math.max(ball.getY(), getY());
  }

  /**
   * @param ball ball which is being compared to this block
   * @return True if ball should bounce off Block object, False if not
   */
  public boolean checkIfShouldBounce(Ball ball) {
    return (getHealth() > 0 && intersects(ball));
  }

  /**
   * Calculate overlap between block and ball, and "bounce" ball velocity accordingly.
   * Adapted from code authored by ChatGPT.
   */
  public void bounceBall(Ball ball) {
    double overlapX = calculateXOverlap(ball);
    double overlapY = calculateYOverlap(ball);

    if (overlapX < overlapY) {    // Ball hit from left or right side

      ball.reverseXSpeed();

      // Move ball outside the block
      if (ball.getX() < getX()) {
        // when ball is to the left of block
        ball.setX(getX() - ball.getWidth());  // place ball directly to the left of the block
      } else {
        // when ball is to the right of block
        ball.setX(getRightEdgeX());
      }

    } else if (overlapY < overlapX) {   // Ball hit from top or bottom

      ball.reverseYSpeed();

      // Move ball outside the block
      if (ball.getY() < getY()) {
        // when ball is above block
        ball.setY(getY() - ball.getHeight());
      } else {
        // when ball is below block
        ball.setY(getBottomEdgeY());
      }

    } else {
      // Perfect corner case -> reverse both x and y velocities
      ball.reverseXSpeed();
      ball.reverseYSpeed();
    }
  }

  /**
   * Completes actions that occur on a block when it is hit by a ball.
   */
  public void hitBlockActions(Ball ball) {
    decreaseHealth();
    if (isBroken()) {
      makeInvisible();
    }
    updateColorForHealth();
    bounceBall(ball);
  }

  /**
   * Updates a block's color to the correct gradient of the main color based on its health.
   * Adapted from code authored by ChatGPT.
   */
  public void updateColorForHealth() {
    int currentHealth = getHealth();
    if (currentHealth <= 0) {
      return;
    }

    Color mainColor = myLevelMap.getMainColor();
    int levelMaxBlockHealth = myLevelMap.getInitialMaxHealth();

    double fraction =
        (double)(levelMaxBlockHealth - currentHealth) / (levelMaxBlockHealth - 1);

    double brightnessRange = MAX_BLOCK_BRIGHTNESS_FACTOR - MIN_BLOCK_BRIGHTNESS_FACTOR;
    double brightnessFactor = MIN_BLOCK_BRIGHTNESS_FACTOR + fraction * brightnessRange;
    double saturationFactor = MAX_BLOCK_BRIGHTNESS_FACTOR - fraction * brightnessRange;

    // Suppose the "base" color is your mainColor, e.g., Color.DARKRED
    // We do not shift hue (0), we do not change saturation (1.0),
    // we scale brightness by brightnessFactor, keep opacity the same (1.0).
    Color newColor = mainColor.deriveColor(0, saturationFactor, brightnessFactor, 1.0);
    setColor(newColor);

  }

  /**
   * @return fill color of the block as Paint object
   */
  public Paint getColor() {
    return getShape().getFill();
  }

  /**
   * Sets the fill color of the block
   * @param color Paint object representing color
   */
  public void setColor(Paint color) {
    getShape().setFill(color);
  }

  /**
   * Returns the x (horizontal) position of the top-left corner of the block Rectangle shape
   */
  public double getX() {
    return myShape.getX();
  }

  /**
   * Returns the y (vertical) position of the top-left corner of the block Rectangle shape
   */
  public double getY() {
    return myShape.getY();
  }

  /**
   * @return the x-position value of the right-hand side of the shape.
   */
  private double getRightEdgeX() {
    return getX() + getWidth();
  }

  /**
   * @return the y-position value of the bottom edge of the shape.
   */
  private double getBottomEdgeY() {
    return getY() + getHeight();
  }

  /**
   * Returns the width of the block Rectangle shape
   */
  public double getWidth() {
    return myShape.getWidth();
  }

  /**
   * Returns the height of the block Rectangle shape
   */
  public double getHeight() {
    return myShape.getHeight();
  }
}
