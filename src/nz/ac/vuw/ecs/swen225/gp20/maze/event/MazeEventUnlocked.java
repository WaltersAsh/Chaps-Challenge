package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when chap unlocks a door
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventUnlocked extends MazeEventWalked {
  private Door door;
  private Key key;

  public MazeEventUnlocked(Maze maze, Tile origin, Tile destination, Maze.Direction direction,
      Door door, Key key) {
    super(maze, destination, destination, direction);
    this.door = door;
    this.key = key;

  }

  public Door getDoor() {
    return door;
  }

  public Key getKey() {
    return key;
  }

  @Override
  public String toString() {
    return String.format("Unlocked a %s at %s,%s with a %s", door, destination.getCol(),
        destination.getRow(), key);
  }
  
  @Override
  public void accept(MazeEventListener l) {
    l.update(this);
  }
}
