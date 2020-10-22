package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import com.google.common.base.Preconditions;
import nz.ac.vuw.ecs.swen225.gp20.maze.Door;
import nz.ac.vuw.ecs.swen225.gp20.maze.Key;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.PathTile;

/**
 * Event for when Chap unlocks a Door.
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventUnlocked extends MazeEventWalked {
  protected Door door;
  protected Key key;

  /**
   * Construct a new instance.
   * 
   * @param maze  The maze which this event is tied to.
   * @param origin The original position of Chap.
   * @param target  The new position of Chap.
   * @param direction The direction moved.
   * @param door The Door unlocked.
   * @param key The Key used.
   */
  public MazeEventUnlocked(Maze maze, PathTile origin, PathTile target, Maze.Direction direction,
      Door door, Key key) {
    super(maze, origin, target, direction);
    this.door = door;
    this.key = key;
    Preconditions.checkArgument(door.getColor().equals(key.getColor()), "Door and key must match");
  }

  /**
   * Get the Door unlocked.
   * @return  The Door.
   */
  public Door getDoor() {
    return door;
  }

  /**
   * Get the Key used.
   * @return  The Key.
   */
  public Key getKey() {
    return key;
  }
  
  @Override
  public void receive(MazeEventListener l) {
    l.update(this);
  }
  
  @Override
  public void invert() {
    super.invert();
    target.place(door);
    if (key.getColor() != Maze.KeyColor.GREEN) {
      maze.getChap().pickup(key);
    }
    
  }
}
