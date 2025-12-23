# breakout
## Calvin Chen

### Description

Custom Java implementation of the classic Breakout arcade game featuring multi-level play, file-driven level and color definitions, and dynamically sized blocks that adapt to each map layout.

### Timeline

 * Start Date: 1/15/2025

 * Finish Date: 1/23/2025

 * Hours Spent: Maybe 2-3 hours per day on average, concentrated mainly within the last 3-4 days.



### Attributions

 * Resources used for learning (including AI assistance)
   * [docs.oracle.com](Oracle)
   * ChatGPT o1
 
 * Resources used directly (including AI assistance)
   * El Capitan spinning beachball GIF by madebyjw.com -- https://gfycat.com/rapidathleticilladopsis
   * Basketball image -- https://www.rawpixel.com/image/6535646/png-sticker-public-domain
   * [docs.oracle.com](Oracle)
   * ChatGPT o1


### Running the Program

 * Main class: Main.java starts the game, but the actual game functionality is mainly managed in
   GameManager.java

 * Data files needed: 
   * Level maps within \resources\maps. Filename lvl_xx.txt. Needs to contain positive integers
     representing blocks (integer = block health) and zeros to represent spaces. The .txt file must
     have the same number of integers per row. No more than 50 integers per row or column. The game
     will _automatically_ adjust block width and height to accommodate the given layout.
   * Color values within \resources\colors. Filename lvl_xx_color.txt. File must contain only a hex
     code, such as _#00539b_. This becomes the primary block color for the given level. A level will
     not load unless both this file and the level map file are present.
 * Key/Mouse inputs:
   * Left and right arrow keys: control paddle movement during gameplay
   * Space bar: used to progress past any text/splash screens, and to set the ball into motion.
 * Cheat keys:
   * Q - updates high score with current score if applicable, then resets game to start screen.
   * R - resets paddle and ball to default positions during level



### Notes/Assumptions

 * Assumptions or Simplifications:
   * All provided level file data in \resources\ is in the correct format.
   * The physics of the ball may not always be entirely mathematically accurate. For instance, when
     a ball is estimated to have hit a corner, then its velocity is exactly reversed.

 * Known Bugs:
   * Ball can _occasionally_ take an erroneous bounce off a block, such as in the wrong direction.

 * Features implemented:
   * Ball movement
   * Ball interaction with wall and paddle
   * Basic block breaking upon ball contact
   * Paddle control using arrow keys
   * Life Mechanics (by default, 3 lives per game)
   * Level progression
   * Multiple levels
   * Game status display during gameplay (shows current score and lives remaining)
   * Splash screen at start containing rules
   * Score keeping
   * Restart game after losing all lives
   * High score during single program instance
   * Multi-hit blocks
   * Custom and non-custom cheat-key

 * Features unimplemented:
   * Unbreakable blocks and other special block type(s)
   * Special paddle behaviors
   * Power ups
   * Additional cheat keys

 * Noteworthy Features:
   * Block dimensions are dynamically adjust based on how many rows and columns of blocks are
     scanned within the \maps file.
   * Blocks change color upon contact. This color is a range of colors surrounding the level's
     primary color (defined in the \colors file), with block color getting lighter as health is
     reduced (compared to the health of the highest-health block at the start of the level).



### Assignment Impressions


