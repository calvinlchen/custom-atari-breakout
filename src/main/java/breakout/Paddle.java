package breakout;

import javafx.scene.shape.Rectangle;

public class Paddle {
  public static final int PADDLE_WIDTH = 80;
  public static final int PADDLE_HEIGHT = 10;
  public static final int PADDLE_SPEED = 12;

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
    start_x = scene_width/2 - PADDLE_WIDTH/2;
    start_y = (scene_height/10)*9 - PADDLE_HEIGHT/2;
    myShape = new Rectangle(start_x, start_y, PADDLE_WIDTH, PADDLE_HEIGHT);
  }

  /**
   * Returns internal shape object of game paddle.
   */
  public Rectangle getShape() {
    return myShape;
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
   * Move paddle left if not contacting left screen edge (else, do not move).
   * elapsedTime is used to ensure consistent speed across different machines. (Robert C. Duvall)
   */
  public void moveLeft () {
    if (! isContactingLeftEdge())
      myShape.setX(myShape.getX() + -1*PADDLE_SPEED);
    else
      myShape.setX(0);
  }

  /**
   * Move paddle right if not contacting right screen edge (else, do not move).
   * elapsedTime is used to ensure consistent speed across different machines. (Robert C. Duvall)
   */
  public void moveRight (int sceneWidth) {
    if (! isContactingRightEdge(sceneWidth))
      myShape.setX(myShape.getX() + PADDLE_SPEED);
    else
      // left corner of paddle is PADDLE_WIDTH distance from right scene edge
      myShape.setX(sceneWidth - PADDLE_WIDTH);
  }

  /**
   * Returns the distance between the left side of the paddle and the left scene edge.
   */
  private double distanceToLeftEdge() {
    return myShape.getX();
  }

  /**
   * Returns the distance between the right side of the paddle and the right scene edge.
   */
  private double distanceToRightEdge(int sceneWidth) {
    return sceneWidth - (myShape.getX() + PADDLE_WIDTH);
  }

  /**
   * Returns whether the paddle is contacting the left edge of the scene.
   */
  private boolean isContactingLeftEdge() {
    return (distanceToLeftEdge() <= 0);
  }

  /**
   * Returns whether the paddle is contacting the right edge of the scene.
   */
  private boolean isContactingRightEdge(int sceneWidth) {
    return (distanceToRightEdge(sceneWidth) <= 0);
  }

  /**
   * Reset paddle to its start position.
   */
  public void reset () {
    myShape.setX(start_x);
    myShape.setY(start_y);
  }
}
