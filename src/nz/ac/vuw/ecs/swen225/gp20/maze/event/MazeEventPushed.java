package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.Movable;
import nz.ac.vuw.ecs.swen225.gp20.maze.PathTile;

/**
 * Event for when chap pushes another Movable (usually a Crate).
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventPushed extends MazeEventWalked {
  private Movable pushed;

  /**
   * Construct a new instance.
   * 
   * @param maze  The maze which this event is tied to.
   * @param origin The original position of Chap.
   * @param target  The new position of Chap.
   * @param direction The direction moved.
   * @param pushed The Movable pushed.
   */
  public MazeEventPushed(Maze maze, PathTile origin, PathTile target, Maze.Direction direction,
      Movable pushed) {
    super(maze, origin, target, direction);
    this.pushed = pushed;
  }

  /**
   * Get the pushed Movable.
   * @return The Movable.
   */
  public Movable getPushed() {
    return pushed;
  }
  
  @Override
  public void receive(MazeEventListener l) {
    l.update(this);
  }
  
  @Override
  public void invert() {
    target.moveTo(pushed);
    super.invert();
  }
}
