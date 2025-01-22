# Breakout Design
## Calvin Chen


## Design Goals
* Create separate classes as needed for different object types
* Ensure that methods are relatively minimal
* Reduce code repetition
* Allow new features to be implemented without unnecessary complications
* Ensure a functional block-breaker game which behaves according to user expectation

## High-Level Design
* The Main class initializes the program.
  * This class also contains basic universal constants, such as SCENE_WIDTH and filename prefixes.
* GameManager monitors player aspects such as score, high score, and lives remaining. It directs the
  appearance of different Scenes and on-screen elements.
  * GameManager also initiates each object to try their respective checks or actions. For example,
    it commands each block in an active Scene to check if it is contacting the ball and to bounce
    the ball if so. Potential interactions between two game objects are facilitated by GameManager,
    while the methods themselves are more often defined within the objects' own classes.
* Individual elements within the game itself, such as block, ball, and paddle, each have their own
  respective classes.
  * For most in-game objects, the ball's bounce is defined by the object off which it bounces. For
    example, paddle-bouncing procedure is defined within the Paddle class, and Block-bouncing
    calculations are defined within the Block class. This allows any new objects/obstacles in the
    game to declare how they interact with the ball.
  * Constants for each object, such as BALL_SPEED and PADDLE_WIDTH, are defined within their
    respective classes.
* Block layout per level is organized by the LevelMap class, which reads data files and creates the
  proper Block objects accordingly.
  * LevelMap dynamically adjusts certain features of the blocks, such as their color and size,
    depending on elements of the data file, such as maximum health values and number of blocks
    within the file (respectively).
* Helper classes such as GameText and Util contain methods which are utilized by other class(es).

## Assumptions or Simplifications
* Ball bounce direction is at times simplified/estimated based on its overlap with the colliding
  block. A frictionless environment is assumed.
* All level data provided via the \resources directory must be in the proper format as outlined in
  the README and below. The code may not function properly if invalid data is provided.

## Changes from the Plan
* Unfortunately, I was not able to implement many of the extra features from the plan. I feel that
  most of them would have been possible to implement given my design, except I have run out of time.
* My code did not implement a "Round"/Level class that stores a score. Instead, it utilizes
  GameManager and LevelMap, which separately store the current player statistics and the level block
  data and other level information, respectively.

## How to Add New Levels
* Add a block map to \resources\maps titled lvl_[level number].txt
  * level number must be positive and in two-digit format (01, 02, etc.) and cannot exceed 99.
  * Blocks are represented within the file by their health values (as positive integers), and spaces
    are represented by zeros. These integers are separated by spaces. The in-game layout will match
    the layout of integers in the file such that the rows and columns match. However, every row must
    contain the same number of integers, and every column must also be the same size.
* Add a color hex value to \resources\colors titled lvl_[level number]_color.txt
  * File must only contain the color hex value with a hashtag (#) at the beginning. See examples:
    * #00539b
    * #FFFFFF
    * #006000
  * The level will NOT load without this color file.
  * This color describes the color of a block with "middle" health. Blocks with more
    health appear darker, and blocks with less health appear brighter.
* The game will automatically recognize the new level and add it based on its level number,
  assuming both files are present.

