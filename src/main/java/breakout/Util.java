package breakout;

import java.util.Scanner;
import javafx.scene.paint.Color;

/**
 * This class contains static helper methods used in other classes.
 */
public class Util {

  /**
   * Returns the number of integers (separated by spaces) in a String
   */
  public static int countIntegersInString(String s) {
    Scanner scanner = new Scanner(s);
    int count = 0;
    while (scanner.hasNextInt()) {
      scanner.nextInt();
      count++;
    }
    return count;
  }

  /**
   * Returns the number of non-negative integers (separated by spaces) in a String
   */
  public static int countNonNegativeIntegersInString(String s) {
    Scanner scanner = new Scanner(s);
    int count = 0;
    while (scanner.hasNextInt()) {
      if (scanner.nextInt() < 0) {
        return -1;
      }
      count++;
    }
    return count;
  }

  /**
   * Given a certain number of elements that must be equally sized, return the proper size of each
   * element to fit within a given space.
   */
  public static double divideSpaceByElement(double spaceSize, int numElements) {
    return spaceSize / numElements;
  }

  /**
   * Converts a given hex code String to a Color object.
   * @param hexCode String in hex code format, such as "#FFFFFF", or "#00539b" (without quotes)
   */
  public static Color hexToColor(String hexCode) {
    return Color.valueOf(hexCode);
  }

  /**
   * @return level name with level filename format (lvl_xx)
   */
  public static String getLevelFilename(int levelNumber) {
    String filename = "lvl_";

    // add 0 before level number if level number is single digits
    if (levelNumber < 10) {
      filename += "0";
    }

    return filename + levelNumber;
  }
}
