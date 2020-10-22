package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import com.google.common.base.Preconditions;

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
    Preconditions.checkArgument(door.getColor().equals(key.getColor()), "Door and key must match");
  }

  public Door getDoor() {
    return door;
  }

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
    if(key.getColor()!=Maze.KeyColor.GREEN) {
      maze.getChap().pickup(key);
    }
    
  }
}
