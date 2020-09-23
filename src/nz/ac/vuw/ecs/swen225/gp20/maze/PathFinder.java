package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.util.Random;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.Direction;

public class PathFinder {
  
  public enum Mode{
    STRAIGHT_CLOCKWISE,
    STRAIGHT_ANTICLOCKWISE,
    STRAIGHT_RANDOM,
    RANDOM,
    ASTAR,
  }
  
  Maze maze;
  Random random = new Random();
  Direction previous = randomDirection();

  public PathFinder(Maze m) {
    maze = m;
  }
  
  private Direction randomDirection() {
    return Direction.values()[random.nextInt(Direction.values().length)];
  }

  public static Direction clockwise(Direction direction){
    switch(direction) {
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
  
  public static Direction antiClockwise(Direction direction){
    switch(direction) {
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
   * Whether this PathFinder is trapped (can't move)
   * 
   * @param current
   * @return
   */
  public boolean trapped(Tile current) {
    return !(maze.tileTo(current, Direction.UP).isWalkable() ||
           maze.tileTo(current, Direction.DOWN).isWalkable() ||
           maze.tileTo(current, Direction.LEFT).isWalkable() ||
           maze.tileTo(current, Direction.RIGHT).isWalkable());
    
  }

  public Direction next(Tile current, Mode mode) {
    if(trapped(current)) {
      return null;
    }
    Direction next = null;
    
    switch(mode) {
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
    if(next!=null) {
      previous = next;
    }
    return next;
  }
  
  private Direction nextAStar(Tile current) {
    return null;
  }
  
  private Direction nextRandom(Tile current) {
    
    Direction next = randomDirection();
    while(!maze.tileTo(current, next).isWalkable()) {
      next = randomDirection();
    }
    return next;
  }
  
  private Direction nextStraightRotation(Tile current, boolean clockwise) {
    // go straight if possible
    Direction next = previous;
    if(maze.tileTo(current, previous).isWalkable()) {
      return next;
    }else { // not possible, so turn clockwise, return next one possible
      while(!maze.tileTo(current, next).isWalkable()) {
        if(clockwise) {
          next = clockwise(next);
        }else {
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
    if(maze.tileTo(current, previous).isWalkable()) {
      return previous;
    }else if(maze.tileTo(current, next).isWalkable()){
      return next;
    }
    return null;
  }

}
