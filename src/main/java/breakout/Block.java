package breakout;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block {
  private int myHealth;

  private final Rectangle myShape;

  public Block(double xPosition, double yPosition, double width, double height, int healthCount) {
    // Create rectangle shape for block
    myShape = new Rectangle(xPosition, yPosition, width, height);
    myShape.setFill(Color.DARKRED);
    myShape.setStroke(Color.BLACK);
    myShape.setStrokeWidth(2);

    myHealth = healthCount;
  }

  // Creates a block with 0 health and broken-block characteristics
  public static Block createEmptyBlock(double xPosition, double yPosition, double width, double height) {
    Block block = new Block(xPosition, yPosition, width, height, 0);
    block.makeInvisible();
    return block;
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
   * Code authored by ChatGPT.
   */
  public boolean intersects(Ball ball) {
    return (ball.getX() + ball.getWidth() >= getX())    // ball's right edge >= block's left
        && (ball.getX() <= getX() + getWidth())         // ball's left edge  <= block's right
        && (ball.getY() + ball.getHeight() >= getY())   // ball's bottom    >= block's top
        && (ball.getY() <= getY() + getHeight());       // ball's top       <= block's bottom
  }

  /**
   * Returns the amount of x-axis overlap between this block and the given ball.
   * Code authored by ChatGPT.
   */
  private double calculateXOverlap(Ball ball) {
    double ballRight = ball.getX() + ball.getWidth();
    double blockRight = getX() + getWidth();
    return Math.min(ballRight, blockRight) - Math.max(ball.getX(), getX());
  }

  /**
   * Returns the amount of y-axis overlap between this block and the given ball.
   * Code authored by ChatGPT.
   */
  private double calculateYOverlap(Ball ball) {
    double ballBottom = ball.getY() + ball.getHeight();
    double blockBottom = getY() + getHeight();
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
   * Code authored by ChatGPT.
   */
  public void bounceBall(Ball ball) {
    double overlapX = calculateXOverlap(ball);
    double overlapY = calculateYOverlap(ball);

    if (overlapX < overlapY) {    // Ball hit from left or right side

      ball.reverseXSpeed();

      // Move ball outside the block
      if (ball.getX() < getX()) {
        // when ball is to the left of block
        ball.setX(getX() - ball.getWidth());
      } else {
        // when ball is to the right of block
        ball.setX(getX() + getWidth());
      }

    } else if (overlapY < overlapX) {   // Ball hit from top or bottom

      ball.reverseYSpeed();

      // Move ball outside the block
      if (ball.getY() < getY()) {
        // when ball is above block
        ball.setY(getY() - ball.getHeight());
      } else {
        // when ball is below block
        ball.setY(getY() + getHeight());
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
