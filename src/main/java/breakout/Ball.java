package breakout;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * ADAPTED FROM: Robert C. Duvall's "Bouncer" class (<a href="https://coursework.cs.duke.edu/compsci308_2025spring/lab_bounce/-/blob/95c5d61cca4390e96f0a944670bf5a01094ed2f1/src/main/java/bounce/Bouncer.java">...</a>)
 * Defines the ball which bounces off the paddle and is used to break game blocks.
 */
public class Ball {
  public static final int BALL_SPEED = 210;
  public static final int BALL_SIZE = 28;

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
    // begin at initial position, zero velocity.
    reset();
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
   * Bounce off the left, right, and top edges of the screen when ball position indicates it.
   *
   * @author Robert C. Duvall
   */
  public void bounceOffEdge(double screenWidth) {
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
  public void bounceOffPaddle(Paddle paddle) {
    // paddle is treated as a line with 0 thickness
    if (paddle.getX() <= getCenterX()
        && paddle.getX() + paddle.getWidth() >= getCenterX()
        && getY() <= paddle.getY()
        && getY() + getHeight() >= paddle.getY()) {
      reverseYSpeed();
    }
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
  public void reverseXSpeed() {
    myVelocity = new Point2D(-myVelocity.getX(), myVelocity.getY());
  }

  /**
   * Reverse the y-axis (vertical) speed of the ball
   */
  public void reverseYSpeed() {
    myVelocity = new Point2D(myVelocity.getX(), -myVelocity.getY());
  }

  /**
   * Get the width of the ImageView representing the ball
   */
  public double getWidth() {
    return myView.getFitWidth();
  }

  /**
   * Get the width of the ImageView representing the ball
   */
  public double getHeight() {
    return myView.getFitHeight();
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
    return myView.getY();
  }

  /**
   * Set the x-position of the top-left corner of the ImageView representing the ball
   */
  public void setX(double xPos) {
    myView.setX(xPos);
  }

  /**
   * Set the y-position of the top-left corner of the ImageView representing the ball
   */
  public void setY(double yPos) {
    myView.setY(yPos);
  }

  /**
   * Get the x-coordinate of the center of the ball
   */
  public double getCenterX() {
    return getX() + (getWidth() / 2);
  }

  /**
   * Get the y-coordinate of the center of the ball
   */
  public double getCenterY() {
    return myView.getY() + (getHeight() / 2);
  }

  /**
   * @return the x-position value of the right-hand side of the ImageView.
   */
  public double getRightEdgeX() {
    return getX() + getWidth();
  }

  /**
   * @return the y-position value of the bottom edge of the ImageView.
   */
  public double getBottomEdgeY() {
    return getY() + getHeight();
  }

  /**
   * Reset ball to start position and zero velocity.
   */
  public void reset () {
    myView.setX(start_x);
    myView.setY(start_y);
    myVelocity = new Point2D(0, 0);
  }

  /**
   * Start moving the ball in the upward-right direction based on BALL_SPEED.
   */
  public void startMotion() {
    myVelocity = new Point2D(BALL_SPEED, -BALL_SPEED);
  }

  /**
   * Checks if ball is in motion based on myVelocity.
   */
  public boolean isMoving() {
    return (myVelocity.getX() != 0 || myVelocity.getY() != 0);
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
