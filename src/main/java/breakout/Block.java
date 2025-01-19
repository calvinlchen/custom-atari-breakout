package breakout;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block {
  private int myHealth;

  private final Rectangle myShape;

  // Defines the margin of a block that is used to determine if a ball has collided.
  // For instance, a value of 0.2 means that a ball approaching from above will bounce if its bottom
  // edge intersects the top 20% of the block.
  public static final double COLLISION_MARGIN = 0.20;

  public Block(double xPosition, double yPosition, double width, double height, int healthCount) {
    // Create rectangle shape for block
    myShape = new Rectangle(xPosition, yPosition, width, height);
    myShape.setFill(Color.DARKRED);
    myShape.setStroke(Color.BLACK);
    myShape.setStrokeWidth(2);

    myHealth = healthCount;
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
   * Returns whether a ball should "bounce" off the TOP of this block based on current positions
   */
  public boolean shouldBounceTopEdge(Ball ball) {
    return ball.getY() + ball.getHeight() >= getY()
        && ball.getY() + ball.getHeight() <= getY() + getHeight()*COLLISION_MARGIN
        && ballIsWithinHorizontalBounds(ball)
        && getHealth() > 0;
  }

  /**
   * Returns whether a ball should "bounce" off the BOTTOM of this block based on current positions
   */
  public boolean shouldBounceBottomEdge(Ball ball) {
    return ball.getY() <= getY() + getHeight()
        && ball.getY() >= getY() + getHeight()*(1-COLLISION_MARGIN)
        && ballIsWithinHorizontalBounds(ball)
        && getHealth() > 0;
  }

  /**
   * Returns whether a ball should "bounce" off the LEFT edge of this block based on current positions
   */
  public boolean shouldBounceLeftEdge(Ball ball) {
    return ball.getX() + ball.getWidth() >= getX()
        && ball.getX() + ball.getWidth() <= getX() + getWidth()*COLLISION_MARGIN
        && ballIsWithinVerticalBounds(ball)
        && getHealth() > 0;
  }

  /**
   * Returns whether a ball should "bounce" off the RIGHT edge of this block based on current positions
   */
  public boolean shouldBounceRightEdge(Ball ball) {
    return ball.getX() <= getX()
        && ball.getX() >= getX() + getWidth()*(1-COLLISION_MARGIN)
        && ballIsWithinVerticalBounds(ball)
        && getHealth() > 0;
  }

  /**
   * Returns whether a ball is positioned within the horizontal range of the block's coordinates.
   */
  private boolean ballIsWithinHorizontalBounds(Ball ball) {
    return ball.getCenterX() >= getX() && ball.getCenterX() <= getX() + getWidth();
  }

  /**
   * Returns whether a ball is positioned within the vertical range of a block's coordinates.
   */
  private boolean ballIsWithinVerticalBounds(Ball ball) {
    return ball.getCenterY() >= getY() && ball.getCenterY() <= getY() + getHeight();
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
