package breakout;

import java.io.InputStream;
import java.util.Scanner;
import javafx.scene.paint.Color;

public class LevelMap {
  private String mySourceFilePath;
  private Block[][] myBlocks;
  private int myInitialMaxHealth;

  // The main block color; becomes darker/lighter when blocks have more/less health
  private Color myMainColor;

  // Defines the proportion of vertical screen space that the blocks take up
  public final double BLOCK_VERTICAL_SPACE_USAGE = 0.75;

  /**
   * Constructs
   * @param filepath name/path of .txt file containing level block layout as integers
   */
  public LevelMap(String filepath, double screenWidth, double screenHeight) {
    mySourceFilePath = filepath;

    // Creates 2D array template based on how many values are in the file
    myBlocks = getBlockMapTemplate(filepath);

    // Store actual Blocks into the array based on source-file values
    storeBlocksFromFile(filepath, screenWidth, screenHeight);

    // Store the current max health value among all blocks
    setInitialMaxHealth();

    if (myInitialMaxHealth > 0) {
      // Store main color for level blocks
      String colorFilename = "/colors/lvl_01_color.txt";
      System.out.println(colorFilename);
      myMainColor = getColorFromFile(colorFilename);

      // Set block colors according to myMainColor and block health values
      setAllBlockColors();
    }
  }

  /**
   * Sets the color of all blocks in the level according to myMainColor and the current block health
   * and the initial maximum block health value
   */
  public void setAllBlockColors() {
    for (Block[] row : myBlocks) {
      for (Block block : row) {
        block.updateColorForHealth();
      }
    }
  }

  /**
   * Returns an empty but correctly-sized Block[][] 2D array based on the number of rows and columns
   * of integers in the file.
   * Returns size-zero array if file input is invalid, such as if rows do not contain the same
   * number of integers.
   * Adapted from Scanner code written by ChatGPT.
   */
  private Block[][] getBlockMapTemplate(String sourceFilePath) {
    InputStream sourceInputStream = getInputStreamFromPath(sourceFilePath);
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
   * Converts a .txt file into Blocks based on the integer values in the file
   * @param sourceFilePath filepath of .txt file containing number "picture" for block configuration
   */
  private void storeBlocksFromFile(String sourceFilePath,
      double screenWidth, double screenHeight) {

    // If myBlocks is not the zero array
    if (myBlocks.length != 0 && myBlocks[0].length != 0) {
      double blockWidth = Util.divideSpaceByElement(screenWidth, myBlocks[0].length);
      double blockHeight = Util.divideSpaceByElement(
          screenHeight*(BLOCK_VERTICAL_SPACE_USAGE), myBlocks.length);

      InputStream sourceInputStream = getInputStreamFromPath(sourceFilePath);
      Scanner scanner = new Scanner(sourceInputStream);

      for (int row = 0; row < myBlocks.length; row++) {
        for (int column = 0; column < myBlocks[0].length; column++) {
          int healthValue = scanner.nextInt();
          myBlocks[row][column] = Block.createBlockFor2dArrayWithParameters(row, column,
              blockWidth, blockHeight, healthValue, this);
        }
      }
      return;
    }
    System.out.println("Invalid file input.");
  }

  /**
   * Retrieves main level color from color file; defaults to gray if no color file is found
   */
  private Color getColorFromFile(String filename) {
    InputStream in = getInputStreamFromPath(filename);
    Scanner s = new Scanner(in);
    if (s.hasNext()) {
      return Util.hexToColor(s.next());
    }
    return Color.GRAY;
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

  /**
   * Stores the maximum health value among all blocks in myBlocks
   */
  private void setInitialMaxHealth() {
    myInitialMaxHealth = 0;
    for (Block[] row : myBlocks) {
      for (Block block : row) {
        if (block.getHealth() > myInitialMaxHealth) {
          myInitialMaxHealth = block.getHealth();
        }
      }
    }
  }

  /**
   * Returns the starting max health value among all blocks based on the source file map
   */
  public int getInitialMaxHealth() {
    return myInitialMaxHealth;
  }

  /**
   * Returns the main block color
   */
  public Color getMainColor() {
    return myMainColor;
  }

  /**
   * Returns the filepath of the source file for this level block mapping
   */
  public String getSourceFilePath() {
    return mySourceFilePath;
  }
}
