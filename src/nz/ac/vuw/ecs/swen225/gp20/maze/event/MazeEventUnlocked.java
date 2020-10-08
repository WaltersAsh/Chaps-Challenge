package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when chap unlocks a door
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventUnlocked extends MazeEventWalked {
  protected Door door;
  protected Key key;

  public MazeEventUnlocked(Maze maze, PathTile origin, PathTile target, Maze.Direction direction,
      Door door, Key key) {
    super(maze, origin, target, direction);
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
    return String.format("Unlocked a %s at %s,%s with a %s", door, target.getCol(),
        target.getRow(), key);
  }
  
  @Override
  public void receive(MazeEventListener l) {
    l.update(this);
  }
  
  @Override
  public void invert() {
    target.place(door);
    maze.getChap().pickup(key);
    super.invert();
  }
}
