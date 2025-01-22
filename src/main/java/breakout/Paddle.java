package breakout;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle {

  public static final int PADDLE_WIDTH = 150;
  public static final int PADDLE_HEIGHT = 10;
  public static final int PADDLE_SPEED = 30;

  // start position of the paddle for each round
  private final int start_x;  // middle of scene (horizontally)
  private final int start_y;  // near bottom (vertically)

  // shape of paddle is a Rectangle
  private final Rectangle myShape;

  /**
   * Create the paddle for a given scene based on scene dimensions
   */
  public Paddle(int scene_width, int scene_height) {
    // paddle starts at bottom-center of the scene
    start_x = scene_width / 2 - PADDLE_WIDTH / 2;
    start_y = (scene_height / 15) * 14 - PADDLE_HEIGHT / 2;
    myShape = new Rectangle(start_x, start_y, PADDLE_WIDTH, PADDLE_HEIGHT);
    myShape.setFill(Color.WHITE);
  }

  /**
   * Returns internal shape object of game paddle.
   */
  public Rectangle getShape() {
    return myShape;
  }

  /**
   * Move paddle left if not contacting left screen edge (else, do not move). elapsedTime is used to
   * ensure consistent speed across different machines. (Robert C. Duvall)
   */
  public void moveLeft() {
    if (!isContactingLeftEdge()) {
      setX(getX() + -1 * PADDLE_SPEED);
    } else {
      setX(0);
    }
  }

  /**
   * Move paddle right if not contacting right screen edge (else, do not move). elapsedTime is used
   * to ensure consistent speed across different machines. (Robert C. Duvall)
   */
  public void moveRight(int sceneWidth) {
    if (!isContactingRightEdge(sceneWidth)) {
      setX(getX() + PADDLE_SPEED);
    } else
    // left corner of paddle is PADDLE_WIDTH distance from right scene edge
    {
      setX(sceneWidth - PADDLE_WIDTH);
    }
  }

  /**
   * Returns the distance between the left side of the paddle and the left scene edge.
   */
  private double distanceToScreenLeftEdge() {
    return getX();
  }

  /**
   * Returns the distance between the right side of the paddle and the right scene edge.
   */
  private double distanceToScreenRightEdge(int sceneWidth) {
    return sceneWidth - (getX() + PADDLE_WIDTH);
  }

  /**
   * Returns whether the paddle is contacting the left edge of the scene.
   */
  private boolean isContactingLeftEdge() {
    return (distanceToScreenLeftEdge() <= 0);
  }

  /**
   * Returns whether the paddle is contacting the right edge of the scene.
   */
  private boolean isContactingRightEdge(int sceneWidth) {
    return (distanceToScreenRightEdge(sceneWidth) <= 0);
  }

  /**
   * Bounce off the paddle if contacting the paddle from above or below
   */
  public void checkAndBounceBall(Ball ball) {
    // ball must be travelling downwards to be bounced.
    if (getX() <= ball.getCenterX()
        && getX() + getWidth() >= ball.getCenterX()
        && ball.getY() <= getY()
        && ball.getY() + ball.getHeight() >= getY()
        && ball.getMyVelocity().getY() > 0) {
      ball.reverseYSpeed();
    }
  }

  /**
   * Returns x-position of paddle
   */
  public double getX() {
    return myShape.getX();
  }

  /**
   * Returns y-position of paddle
   */
  public double getY() {
    return myShape.getY();
  }

  /**
   * Set x-position of paddle
   */
  public void setX(double xPos) {
    myShape.setX(xPos);
  }

  /**
   * Set y-position of paddle
   */
  public void setY(double yPos) {
    myShape.setY(yPos);
  }

  /**
   * Returns the width of paddle
   */
  public int getWidth() {
    return PADDLE_WIDTH;
  }

  /**
   * Reset paddle to its start position.
   */
  public void reset() {
    setX(start_x);
    setY(start_y);
  }
}
