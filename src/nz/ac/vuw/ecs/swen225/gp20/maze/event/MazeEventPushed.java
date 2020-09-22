package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when chap pushes another Movable (usually a Crate).
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventPushed extends MazeEventWalked {
  private Movable pushed;

  public MazeEventPushed(Maze maze, PathTile origin, PathTile destination, Maze.Direction direction,
      Movable pushed) {
    super(maze, origin, destination, direction);
    this.pushed = pushed;
  }

  public Movable getPushed() {
    return pushed;
  }

  @Override
  public String toString() {
    return String.format("Walked onto %s,%s and pushed a %s %s", destination.getCol(),
        destination.getRow(), pushed, direction);
  }
  
  @Override
  public void receive(MazeEventListener l) {
    l.update(this);
  }
}
