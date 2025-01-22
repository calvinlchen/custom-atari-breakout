package breakout;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Robert C. Duvall, Calvin Chen
 */
public class Main extends Application {

  // useful names for constant values used
  public static final String TITLE = "Basketball Bouncer";
  public static final Color DUKE_BLUE = Util.hexToColor("#00539b");
  public static final int SCENE_WIDTH = 1000;
  public static final int SCENE_HEIGHT = 750;
  public static final int BODY_FONT_SIZE = 18;

  public static final String BALL_IMAGE = "/ball/basketball.png";
  public static final String MAPS_FILE_PREFIX = "/maps/";
  public static final String COLORS_FILE_PREFIX = "/colors/";

  /**
   * Start the application.
   */
  @Override
  public void start(Stage stage) {
    // Search through resource files and fetch all available levels
    List<Integer> levelNumbers = getLevelNumbers();

    GameManager gameManager = new GameManager(SCENE_WIDTH, SCENE_HEIGHT, stage, levelNumbers);

    gameManager.startScreen();
  }

  private List<Integer> getLevelNumbers() {
    // Initialize or clear your list
    List<Integer> levelNumbers = new ArrayList<>();

    for (int i = 1; i <= 99; i++) {
      // "lvl_01", "lvl_02", etc.
      String baseFilename = Util.getLevelFilename(i);

      // Full paths for map and color files
      String mapFilePath =
          MAPS_FILE_PREFIX + baseFilename + ".txt";        // e.g., "/maps/lvl_01.txt"
      String colorFilePath =
          COLORS_FILE_PREFIX + baseFilename + "_color.txt"; // e.g., "/colors/lvl_01_color.txt"

      // Check if BOTH exist as resources
      if (getClass().getResourceAsStream(mapFilePath) != null
          && getClass().getResourceAsStream(colorFilePath) != null) {
        levelNumbers.add(i);
      }
    }
    return levelNumbers;
  }
}
