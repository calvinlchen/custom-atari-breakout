package breakout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LevelMap {
  File mySourceFile;
  Block[][] myBlocks;

  /**
   *
   * @param filepath name/path of .txt file containing level block layout as integers
   */
  public LevelMap(String filepath) {
    File mySourceFile = new File(filepath);
    try {
      storeBlocksFromFile(mySourceFile);
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    }
  }

  /**
   * Converts a .txt file into Blocks based on the integer values in the file
   * @param sourceFile file containing integers which define block configuration
   * @throws FileNotFoundException if provided file does not exist or contains non-integer values
   */
  private void storeBlocksFromFile(File sourceFile) throws FileNotFoundException {
    Scanner scanner = new Scanner(sourceFile);
    while (scanner.hasNext()) {
      String s = scanner.next();
      try {
        int number = Integer.parseInt(s);
        System.out.println(number);
      } catch (NumberFormatException e) {
        System.out.println("Invalid number format: " + e.getMessage());
      }
    }
  }
}
