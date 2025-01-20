package breakout;

import java.util.Scanner;

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
}
