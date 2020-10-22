package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import com.google.common.base.Preconditions;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when chap pushes another Movable (usually a Crate).
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventPushed extends MazeEventWalked {
  private Movable pushed;

  public MazeEventPushed(Maze maze, PathTile origin, PathTile target, Maze.Direction direction,
      Movable pushed) {
    super(maze, origin, target, direction);
    this.pushed = pushed;
  }

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
