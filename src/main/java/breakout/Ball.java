package breakout;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * ADAPTED FROM: Robert C. Duvall's "Bouncer" class (<a href="https://coursework.cs.duke.edu/compsci308_2025spring/lab_bounce/-/blob/95c5d61cca4390e96f0a944670bf5a01094ed2f1/src/main/java/bounce/Bouncer.java">...</a>)
 *
 * Defines the ball which bounces off the paddle and is used to break game blocks
 */
public class Ball {
  public static final int BALL_SPEED = 175;
  public static final int BALL_SIZE = 20;

  // JFX representation on the screen
  private final ImageView myView;
  private Point2D myVelocity;
  // Start position of the ball (based on Scene size)
  private final double start_x;
  private final double start_y;

  public Ball (Image image, int screenWidth, int screenHeight) {
    myView = new ImageView(image);
    myView.setFitWidth(BALL_SIZE);
    myView.setFitHeight(BALL_SIZE);
    // start ball near the middle-bottom of the Scene
    myView.setX((BALL_SIZE + screenWidth - BALL_SIZE) / 2.0);
    start_x = getX();
    myView.setY((screenHeight / 9.0) * 7);
    start_y = getY();
    // default to BALL_SPEED speed in both the x and y directions
    myVelocity = new Point2D(BALL_SPEED, -BALL_SPEED);
  }

  /**
   * Move by taking one step based on its velocity.
   * Note, elapsedTime is used to ensure consistent speed across different machines.
   *
   * @author Robert C. Duvall
   */
  public void move (double elapsedTime) {
    myView.setX(getX() + myVelocity.getX() * elapsedTime);
    myView.setY(getY() + myVelocity.getY() * elapsedTime);
  }

  /**
   * Bounce off the walls represented by the edges of the screen.
   *
   * @author Robert C. Duvall
   */
  public void bounceEdge (double screenWidth, double screenHeight) {
    if (getX() < 0 || getX() > screenWidth - getWidth()) {
      reverseXSpeed();
    }
    // simulate bounce off the ceiling wall by simply reversing Y speed
    if (getY() <= 0) {
      reverseYSpeed();
    }
  }

  /**
   * Bounce off the paddle if contacting the paddle from above or below
   */
  public void bouncePaddle (Paddle paddle) {
    // paddle is treated as a line with 0 thickness
    if (paddle.getX() <= getCenterX()
        && paddle.getX() + paddle.getWidth() >= getCenterX()
        && getY() <= paddle.getY()
        && getY() + getHeight() >= paddle.getY()) {
      reverseYSpeed();
    }
  }

  /**
   * Bounce off a block if contacting any edge of the block
   */
  public void bounceBlock (Block block) {

  }

  /**
   * Returns true when ball is at or below the floor (bottom edge) of screen
   */
  public boolean isContactingFloor (double screenHeight) {
    return getY() + getHeight() > screenHeight;
  }

  /**
   * Stops the ball's motion
   */
  public void stopMotion () {
    myVelocity = new Point2D(0, 0);
  }

  /**
   * Reverse the x-axis (horizontal) speed of the ball
   */
  private void reverseXSpeed() {
    myVelocity = new Point2D(-myVelocity.getX(), myVelocity.getY());
  }

  /**
   * Reverse the y-axis (vertical) speed of the ball
   */
  private void reverseYSpeed() {
    myVelocity = new Point2D(myVelocity.getX(), -myVelocity.getY());
  }

  /**
   * Get the width of the ImageView representing the ball
   */
  public double getWidth() {
    return myView.getBoundsInLocal().getWidth();
  }

  /**
   * Get the width of the ImageView representing the ball
   */
  public double getHeight() {
    return myView.getBoundsInLocal().getHeight();
  }

  /**
   * Get the x-position of the top-left corner of the ImageView representing the ball
   */
  public double getX() {
    return myView.getX();
  }

  /**
   * Get the y-position of the top-left corner of the ImageView representing the ball
   */
  public double getY() {
    return getY();
  }

  /**
   * Get the x-coordinate of the center of the ball
   */
  public double getCenterX() {
    return getX() + (myView.getFitWidth() / 2);
  }

  /**
   * Get the y-coordinate of the center of the ball
   */
  public double getCenterY() {
    return myView.getY() + (myView.getFitHeight() / 2);
  }

  /**
   * Reset ball to start position and default velocity.
   */
  public void reset () {
    myView.setX(start_x);
    myView.setY(start_y);
  }

  /**
   * Returns internal view of the ball to interact with other JavaFX methods.
   *
   * @author Robert C. Duvall
   */
  public ImageView getView () {
    return myView;
  }
}
