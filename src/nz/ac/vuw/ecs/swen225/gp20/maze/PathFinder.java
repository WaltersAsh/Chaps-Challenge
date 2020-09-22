package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.util.Random;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.Direction;

public class PathFinder {
  Direction previous;
  public enum Mode{
    STRAIGHT_CLOCKWISE,
    STRAIGHT_ANTICLOCKWISE,
    STRAIGHT_RANDOM,
    RANDOM,
    ASTAR,
  }
  
  Maze maze;
  Random random = new Random();

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

  public Tile next(Tile current, Mode mode) {
    if(trapped(current)) {
      return current;
    }
    switch(mode) {
    case ASTAR:
      return nextAStar(current);
    case RANDOM:
      return nextRandom(current);
    case STRAIGHT_ANTICLOCKWISE:
      return nextStraightAntiClockwise(current);
    case STRAIGHT_CLOCKWISE:
      return nextStraightClockwise(current);
    case STRAIGHT_RANDOM:
      return nextStraight(current);
    default:
      return null;
    }
  }
  
  private Tile nextAStar(Tile current) {
    return current;
  }
  
  private Tile nextRandom(Tile current) {
    Tile next = maze.tileTo(current, randomDirection());
    while(!next.isWalkable()) {
      next = maze.tileTo(current, randomDirection());
    }
    return next;
  }
  
  private Tile nextStraightClockwise(Tile current) {
    return current;
  }
  
  private Tile nextStraightAntiClockwise(Tile current) {
    return current;
  }
  
  private Tile nextStraight(Tile current) {
    return current;
  }

}
