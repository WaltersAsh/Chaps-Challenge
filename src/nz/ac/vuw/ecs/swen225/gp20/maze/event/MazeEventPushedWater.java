package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when chap pushes another Movable (usually a Crate).
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventPushedWater extends MazeEventPushed {
  private Movable pushed;

  public MazeEventPushedWater(Maze maze, Tile origin, Tile destination, Maze.Direction direction,
      Movable pushed) {
    super(maze, origin, destination, direction, pushed);
    this.pushed = pushed;
  }

  public Movable getPushed() {
    return pushed;
  }

  @Override
  public String toString() {
    return String.format("Walked onto %s,%s and pushed a %s %s into water", destination.getCol(),
        destination.getRow(), pushed, direction);
  }
  
  @Override
  public void accept(MazeEventListener l) {
    l.update(this);
  }
}
