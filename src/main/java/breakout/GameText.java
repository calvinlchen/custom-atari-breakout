package breakout;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameText {

  public static final int FONT_SIZE_A = 24;
  public static final int FONT_SIZE_B = 32;

  public static Text getStartScreenText(int numLives, int highScore) {
    // Text for game instructions
    String startInstructions = """
        Welcome to Basketball Bouncer!

        Break as many blocks with the ball as you can to score points.
        If the ball hits the bottom edge, you lose a life!
        (You start with %d lives.)

        Move the paddle with your left and right arrow keys
        so that the ball doesn't hit the floor.

        High score: %d

        Press SPACE to start!
        """.formatted(numLives, highScore);

    Text titleText = new Text(startInstructions);

    titleText.setFill(Color.WHITE);
    titleText.setX(50);
    titleText.setY(100);
    titleText.setFont(Font.font(FONT_SIZE_A));

    return titleText;
  }

  public static Text getLevelCompleteText(int levelNumber) {
    // Text for game instructions
    String continueGame = """
    Level %d complete!
    
    Press SPACE to continue
    """.formatted(levelNumber);

    Text text = new Text(continueGame);

    text.setFill(Color.WHITE);
    text.setX(50);
    text.setY(100);
    text.setFont(Font.font(FONT_SIZE_B));

    return text;
  }

  public static Text getEndingText(boolean playerWon, boolean newHighScore, int highScore,
      int score) {

    String continueGame;

    if (playerWon) {
      continueGame = """
      Congratulations, you won!!
      
      """;
    }
    else {
      continueGame = """
      Game Over
      
      """;
    }

    continueGame += """
    
    Your score: %d
    High score: %d
    """.formatted(score, highScore);

    if(newHighScore) {
      continueGame += """
      
      New high score!
      
      """;
    }

    continueGame += """
    
    
    Press SPACE to restart.
    """;

    Text text = new Text(continueGame);

    text.setFill(Color.WHITE);
    text.setX(50);
    text.setY(100);
    text.setFont(Font.font(FONT_SIZE_B));

    return text;
  }

  public static Text getScoreText(int score) {
    Text scoreText = new Text(scoreToString(score));
    scoreText.setFill(Color.WHITE);
    scoreText.setX(Main.SCENE_WIDTH * 0.1);   // Place text near left edge of screen
    scoreText.setY(Main.SCENE_HEIGHT * 0.95); // Place text near bottom edge
    scoreText.setFont(new Font(Main.BODY_FONT_SIZE));
    return scoreText;
  }

  public static Text getLivesText(int numLives) {
    Text livesText = new Text(livesToString(numLives));
    livesText.setFill(Color.WHITE);
    livesText.setX(Main.SCENE_WIDTH * 0.75);  // Place text near right edge of screen
    livesText.setY(Main.SCENE_HEIGHT * 0.95); // Place text near bottom edge
    livesText.setFont(new Font(Main.BODY_FONT_SIZE));
    return livesText;
  }

  public static String scoreToString(int score) {
    return "Score: " + score;
  }

  public static String livesToString(int numLives) {
    return "Lives remaining: " + numLives;
  }

}
