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


## Changes from the Plan


## How to Add New Levels

