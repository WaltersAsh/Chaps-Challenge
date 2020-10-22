package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.PathTile;
import nz.ac.vuw.ecs.swen225.gp20.maze.Pickup;

/**
 * Event for when chap picks something up off a tile they just walked onto.
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventPickup extends MazeEventWalked {
  protected Pickup picked;

  /**
   * Construct a new instance.
   * 
   * @param maze  The maze which this event is tied to.
   * @param origin The original position of Chap.
   * @param target  The new position of Chap.
   * @param direction The direction moved.
   * @param picked The Pickup obtained.
   */
  public MazeEventPickup(Maze maze, PathTile origin, PathTile target, Maze.Direction direction,
      Pickup picked) {
    super(maze, origin, target, direction);
    this.picked = picked;
  }

  /**
   * Get the obtained Pickup.
   * @return  the Pickup.
   */
  public Pickup getPicked() {
    return picked;
  }

  @Override
  public String toString() {
    return String.format("Picked up a %s at %s,%s", picked, target.getCol(),
        target.getRow());
  }
  
  @Override
  public void receive(MazeEventListener l) {
    l.update(this);
  }
  
  @Override
  public void invert() {
    target.place(picked);
    maze.getChap().unPickup(maze, picked);
    super.invert();
  }
}
