package breakout;

import java.io.InputStream;
import java.util.Scanner;

public class LevelMap {
  String mySourceFilePath;
  Block[][] myBlocks;

  /**
   *
   * @param filepath name/path of .txt file containing level block layout as integers
   */
  public LevelMap(String filepath, double screenWidth, double screenHeight) {
    mySourceFilePath = filepath;
    storeBlocksFromFile(filepath, screenWidth, screenHeight);
  }

  /**
   * Converts a .txt file into Blocks based on the integer values in the file
   * @param sourceFilePath filepath of .txt file containing number "picture" for block configuration
   */
  private void storeBlocksFromFile(String sourceFilePath,
      double screenWidth, double screenHeight) {

    // Proportion of total vertical screen space used by blocks
    double verticalSpaceUsage = 0.75;
    InputStream sourceInputStream = getInputStreamFromPath(sourceFilePath);

    // Creates 2D array template based on how many values are in the file
    myBlocks = getBlockMapTemplate(sourceInputStream);

    // If myBlocks is not the zero array
    if (myBlocks.length != 0 && myBlocks[0].length != 0) {
      double blockWidth = Util.divideSpaceByElement(screenWidth, myBlocks[0].length);
      double blockHeight = Util.divideSpaceByElement(screenHeight*(verticalSpaceUsage), myBlocks.length);

      // Reset InputStream
      sourceInputStream = getInputStreamFromPath(sourceFilePath);
      Scanner scanner = new Scanner(sourceInputStream);
      for (int row = 0; row < myBlocks.length; row++) {
        for (int column = 0; column < myBlocks[0].length; column++) {
          int healthValue = scanner.nextInt();
          if (healthValue < 1) {
            // If file integer is 0, create an off-screen "broken" block
            myBlocks[row][column] = Block.createEmptyBlock(column*blockWidth,
                row*blockHeight, blockWidth, blockHeight);
          } else {
            myBlocks[row][column] = new Block(column*blockWidth, row*blockHeight,
                blockWidth, blockHeight, healthValue);
          }
        }
      }
      return;
    }
    System.out.println("Invalid file input.");
  }

  /**
   * Returns an empty but correctly-sized Block[][] 2D array based on the number of rows and columns
   * of integers in the file.
   * Returns size-zero array if file input is invalid, such as if rows do not contain the same
   * number of integers.
   * Adapted from Scanner code written by ChatGPT.
   */
  private Block[][] getBlockMapTemplate(InputStream sourceInputStream) {
    Scanner fileScanner = new Scanner(sourceInputStream);

    // default to -1 since no line has been scanned yet
    int initialColumnCount = -1;
    int lineCount = 0;

    while (fileScanner.hasNextLine()) {
      int columnCount = Util.countNonNegativeIntegersInString(fileScanner.nextLine());

      // If an integer in the string is negative, or if the String contains no integers
      if (columnCount <= 0) {
        System.out.println("Error: No integers in file row.");
        return new Block[0][0];
      }
      if (initialColumnCount < 0) {
        initialColumnCount = columnCount;
      }
      else if (initialColumnCount != columnCount) {
        // If a row doesn't have the same number of blocks as the row above it, return size-0 array
        System.out.println("Error: Different rows have a different amount of integers.");
        return new Block[0][0];
      }
      lineCount++;
    }

    // If the file contains no lines, or if either dimension (rows/columns) exceeds size 50
    if (lineCount <= 0 || initialColumnCount > 50 || lineCount > 50) {
      System.out.println("Error: Accepted dimensions exceeded. (Must have at least 1 row, and "
          + "cannot exceed 50 rows or columns of blocks.)");
      return new Block[0][0];
    }
    return new Block[lineCount][initialColumnCount];
  }

  /**
   * Returns new InputStream from String of source file path
   */
  private InputStream getInputStreamFromPath(String filepath) {
    return getClass().getResourceAsStream(filepath);
  }

  /**
   * Returns all blocks currently stored in the level map
   */
  public Block[][] getBlocks() {
    return myBlocks;
  }
}
