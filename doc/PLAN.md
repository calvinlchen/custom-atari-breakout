# Breakout Plan
### Calvin Chen


## Interesting Breakout Variants

 * The pinball variant was very interesting to me because of the nature of the pinball paddles.
Depending on the position and speed of the ball as well as the timing of the paddle, the ball
bounces off of the paddle at different speeds and angles. The game has to account for the "physics"
of the ball and the paddle.

 * "Bricks n Balls" intrigued me because of how many lives the blocks had and how the user loses the
game. Instead of the ball hitting the bottom edge a certain number of times, the blocks themselves
move downwards after each round, and there are tens of balls used per round. Also, not all the
blocks were rectangular; some were triangular with different rotations.


## Paddle Ideas

 * (custom) the ball bounces differently depending on where on the paddle it makes contact. (i.e.
left third changes ball direction 45 degrees to the left, right third changes ball to travel 45
degrees right, and middle third is treated as non-angled surface)
 * "catching" the ball when it hits the paddle and releasing it at a later time when a key is
pressed (activated by power up)


## Block Ideas

 * taking multiple hits before being destroyed (based on block color)

 * power-up block (randomly assigned power-up per power-up block, denoted by question mark icon on
the block)

 * unbreakable block: block does not lose health due to contact or explosive power-up. It exists
permanently and has a different appearance (stone texture?) but do not need to be broken to complete
the level.


## Power-up Ideas

 * extra ball: when broken, a random ball appears from the start point, freezing temporarily before
launching diagonally left/right (by random). If the ball hits the bottom edge, a life is still lost.
So this power-up is helpful in progressing faster but also more difficult to handle.

 * shield: a shield covers the bottom edge, so if the ball hits the bottom edge, no life is lost
and the ball bounces normally, but the shield is removed. (Lasts for the duration of the round)

 * explosive: for 10 seconds, all ball(s) on screen have "explosive" effect where whenever they make
contact with a surface (edge, paddle, block), they remove a life from all blocks within a
pre-defined radius.


## Cheat Key Ideas

 * BREAK-19C (cheat key 1-9)

 * BREAK-19D (cheat key S to return to start menu)

 * BREAK-19X (cheat key R: restarts the level, but resets score to state at the start of the level)

 * BREAK-19X (cheat key ".": adds a ball, like the extra-ball power-up. Can add up to 10 per level.)


## Level Descriptions

**SEE _/doc/maps_ DIRECTORY FOR LEVEL "IMAGES"**

 * "UNC" (the university. My game has a Duke Basketball theme). Would contain some "unbreakable"
blocks as well as some power-up blocks.

 * "NCSU" (NC State). Would contain more "unbreakable" blocks and fewer power-up blocks than lvl_01.


## Class Ideas

 * Ball. getPosition() returns x and y coordinates

 * Paddle. getBounce(Ball b) returns direction of bounce based on paddle position
and a ball's position/velocity (because the ends of the paddle bounce differently than middle)

 * Block. isUnbreakable() returns whether a block is an unbreakable block or not.

 * Round. addPoints(int points) adds a given number of points to the round's total score.

