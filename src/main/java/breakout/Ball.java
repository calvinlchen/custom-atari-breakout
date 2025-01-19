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
  private ImageView myView;
  private Point2D myVelocity;
  // Start position of the ball (based on Scene size)
  private double start_x;
  private double start_y;

  public Ball (Image image, int screenWidth, int screenHeight) {
    myView = new ImageView(image);
    myView.setFitWidth(BALL_SIZE);
    myView.setFitHeight(BALL_SIZE);
    // start ball near the middle-bottom of the Scene
    myView.setX((BALL_SIZE + screenWidth - BALL_SIZE) / 2.0);
    start_x = myView.getX();
    myView.setY((screenHeight / 9.0) * 7);
    start_y = myView.getY();
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
    myView.setX(myView.getX() + myVelocity.getX() * elapsedTime);
    myView.setY(myView.getY() + myVelocity.getY() * elapsedTime);
  }

  /**
   * Bounce off the walls represented by the edges of the screen.
   *
   * @author Robert C. Duvall
   */
  public void bounceEdge (double screenWidth, double screenHeight) {
    if (myView.getX() < 0 || myView.getX() > screenWidth - getViewWidth()) {
      reverseXSpeed();
    }
    // simulate bounce off the ceiling wall by simply reversing Y speed
    if (myView.getY() <= 0) {
      reverseYSpeed();
    }
  }

  /**
   * Bounce off the paddle when contacting any edge of the paddle
   */
  public void bouncePaddle (Paddle paddle) {
    // paddle is treated as a line with 0 thickness
    if (paddle.getX() <= myView.getX()+myView.getFitWidth()/2
        && paddle.getX()+paddle.getWidth() >= myView.getX()+myView.getFitWidth()/2
        && myView.getY() <= paddle.getY()
        && myView.getY()+getViewHeight() >= paddle.getY()) {
      reverseYSpeed();
    }
  }

  /**
   * Returns true when ball is at or below the floor (bottom edge) of screen
   */
  public boolean isContactingFloor (double screenHeight) {
    return myView.getY() + myView.getFitHeight() > screenHeight;
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
  private double getViewWidth() {
    return myView.getBoundsInLocal().getWidth();
  }

  /**
   * Get the width of the ImageView representing the ball
   */
  private double getViewHeight() {
    return myView.getBoundsInLocal().getHeight();
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
