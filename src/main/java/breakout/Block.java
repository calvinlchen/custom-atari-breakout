package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Block {
  private int myHealth;

  private final Rectangle myShape;

  public Block(double xPosition, double yPosition, double width, double height, int healthCount) {
    // Create rectangle shape for block
    myShape = new Rectangle(xPosition, yPosition, width, height);
    myShape.setFill(Color.DARKRED);
    myShape.setStroke(Color.BLACK);
    myShape.setStrokeWidth(1);

    myHealth = healthCount;
  }

  /**
   * Creates a block with 0 health and broken-block characteristics
   */
  public static Block createEmptyBlock(double xPosition, double yPosition, double width,
      double height) {
    Block block = new Block(xPosition, yPosition, width, height, 0);
    block.makeInvisible();
    return block;
  }

  /**
   * Creates a block for the LevelMap array with the proper characteristics based on the provided
   * health value (typically from the source file)
   */
  public static Block createBlockFor2dArrayWithParameters( int arrayMapRow, int arrayMapColumn,
      double blockWidth, double blockHeight, int healthValue) {
    if (healthValue < 1) {
      // If file integer is 0, create an off-screen "broken" block
      return Block.createEmptyBlock(arrayMapColumn*blockWidth,
          arrayMapRow*blockHeight, blockWidth, blockHeight);
    }
    return new Block(arrayMapColumn*blockWidth, arrayMapRow*blockHeight,
        blockWidth, blockHeight, healthValue);
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
  public void hitBlockActions() {
    decreaseHealth();
    if (isBroken()) {
      makeInvisible();
    }
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
