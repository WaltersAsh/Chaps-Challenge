package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import com.google.common.base.Preconditions;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.PathTile;
import nz.ac.vuw.ecs.swen225.gp20.maze.Water;

/**
 * Event for when chap walks onto the exit tile.
 *
 * @author Ian 300474717
 *
 */
public class MazeEventWalkedDrowned extends MazeEventWalked {
  Water water;
  
  /**
   * Construct a new instance.
   * 
   * @param maze  The maze which this event is tied to.
   * @param water The Water walked onto.
   * @param origin The original position of Chap.
   * @param target  The new position of Chap.
   * @param direction The direction moved.
   */
  public MazeEventWalkedDrowned(Maze maze, Water water,
      PathTile origin, PathTile target, Maze.Direction direction) {
    super(maze, origin, target, direction);
    this.water = water;
    Preconditions.checkArgument(target.getBlocker().equals(water),
        "Water does not match target PathTile blocker");
  }

  /**
   * Get the Water walked onto.
   * @return  the Water.
   */
  public Water getWater() {
    return water;
  }

  @Override
  public void receive(MazeEventListener l) {
    l.update(this);
  }
  
  @Override
  public void invert() {
    throw new UnsupportedOperationException("Not implemented."); 
  }
}
