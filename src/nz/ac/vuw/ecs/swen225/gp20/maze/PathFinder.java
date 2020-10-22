package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import java.util.Random;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.Direction;

/**
 * PathFinder utility class, implementing various primitive path finding
 * algorithms.
 * 
 * @author Ian 300474717
 */
@JsonIgnoreType
public class PathFinder {

  /**
   * Path finding modes.
   * 
   * @author Ian 300474717
   *
   */
  public enum Mode {
    /**
     * Go straight, and turn clockwise when a wall is encountered.
     */
    STRAIGHT_CLOCKWISE,

    /**
     * Go straight, and turn anticlockwise when a wall is encountered.
     */
    STRAIGHT_ANTICLOCKWISE,

    /**
     * Go straight, and turn in a random direction when a wall is encountered.
     */
    STRAIGHT_RANDOM,

    /**
     * Pick a random direction to move in.
     */
    RANDOM,

    /**
     * A-Star pathfinding algorithm.
     */
    ASTAR,
  }

  Maze maze;
  Random random = new Random(777);
  Direction previous = randomDirection();

  /**
   * Construct a new instance.
   * 
   * @param m the Maze to use for path finding.
   */
  public PathFinder(Maze m) {
    maze = m;
  }

  private Direction randomDirection() {
    return Direction.values()[random.nextInt(Direction.values().length)];
  }

  /**
   * Utility method to return the next direction clockwise.
   * 
   * @param direction The current direction.
   * @return The next direction.
   */
  public static Direction clockwise(Direction direction) {
    switch (direction) {
      case DOWN:
        return Direction.LEFT;
      case LEFT:
        return Direction.UP;
      case RIGHT:
        return Direction.DOWN;
      case UP:
        return Direction.RIGHT;
      default:
        throw new IllegalArgumentException();
    }
  }

  /**
   * Utility method to return the next direction anticlockwise.
   * 
   * @param direction The current direction.
   * @return The next direction.
   */
  public static Direction antiClockwise(Direction direction) {
    switch (direction) {
      case DOWN:
        return Direction.RIGHT;
      case LEFT:
        return Direction.DOWN;
      case RIGHT:
        return Direction.UP;
      case UP:
        return Direction.LEFT;
      default:
        throw new IllegalArgumentException();
    }
  }

  /**
   * Whether this PathFinder is trapped (can't move).
   *
   * @param current The current tile.
   * @return Whether we are trapped or not.
   */
  public boolean trapped(Tile current) {
    return !(maze.tileTo(current, Direction.UP).enemyWalkable()
        || maze.tileTo(current, Direction.DOWN).enemyWalkable()
        || maze.tileTo(current, Direction.LEFT).enemyWalkable()
        || maze.tileTo(current, Direction.RIGHT).enemyWalkable());

  }

  /**
   * Get the next Direction to move to.
   * 
   * @param current Current Tile we are on.
   * @param mode    Path finding mode to pick the next Direction with.
   * @return Next direction to move to.
   */
  public Direction next(Tile current, Mode mode) {
    if (trapped(current)) {
      return null;
    }
    Direction next = null;

    switch (mode) {
      case ASTAR:
        next = nextAStar(current);
        break;
      case RANDOM:
        next = nextRandom(current);
        break;
      case STRAIGHT_ANTICLOCKWISE:
        next = nextStraightAntiClockwise(current);
        break;
      case STRAIGHT_CLOCKWISE:
        next = nextStraightClockwise(current);
        break;
      case STRAIGHT_RANDOM:
        next = nextStraight(current);
        break;
      default:
        break;
    }
    if (next != null) {
      previous = next;
    }
    return next;
  }

  private Direction nextAStar(Tile current) {
    throw new UnsupportedOperationException("Not implemented.");
  }

  private Direction nextRandom(Tile current) {
    Direction next = randomDirection();
    while (!maze.tileTo(current, next).enemyWalkable()) {
      next = randomDirection();
    }
    return next;
  }

  private Direction nextStraightRotation(Tile current, boolean clockwise) {
    // go straight if possible
    Direction next = previous;
    if (maze.tileTo(current, previous).enemyWalkable()) {
      return next;
    } else { // not possible, so turn clockwise, return next one possible
      while (!maze.tileTo(current, next).enemyWalkable()) {
        if (clockwise) {
          next = clockwise(next);
        } else {
          next = antiClockwise(next);
        }
      }
      return next;
    }
  }

  private Direction nextStraightClockwise(Tile current) {
    return nextStraightRotation(current, true);
  }

  private Direction nextStraightAntiClockwise(Tile current) {
    return nextStraightRotation(current, false);
  }

  private Direction nextStraight(Tile current) {
    Direction next = clockwise(clockwise(previous)); // 180 degrees
    if (maze.tileTo(current, previous).enemyWalkable()) {
      return previous;
    } else if (maze.tileTo(current, next).enemyWalkable()) {
      return next;
    }
    return null;
  }

}
