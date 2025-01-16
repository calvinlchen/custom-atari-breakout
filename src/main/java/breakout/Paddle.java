package breakout;

import javafx.scene.shape.Rectangle;

public class Paddle {
  public static final int PADDLE_WIDTH = 80;
  public static final int PADDLE_HEIGHT = 10;
  public static final int PADDLE_SPEED = 20;

  // start position of the paddle for each round
  private int start_x ;
  private int start_y;

  // shape of paddle is a Rectangle
  private Rectangle myShape;

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
   * Move paddle left or right based on given directional value (1 for right, -1 for left).
   * elapsedTime is used to ensure consistent speed across different machines. (Robert C. Duvall)
   */
  public void move (int direction, double elapsedTime) {
    myShape.setX(myShape.getX() + direction*PADDLE_SPEED*elapsedTime);
  }

  /**
   * Reset paddle to its start position.
   */
  public void reset () {
    myShape.setX(0);
  }
}
