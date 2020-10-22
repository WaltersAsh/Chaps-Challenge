package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.InfoField;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.PathTile;


/**
 * Event for when chap walks onto an InfoField.
 *
 * @author Ian 300474717
 *
 */
public class MazeEventInfoField extends MazeEventWalked {
  private InfoField info;

  /**
   * Construct a new instance.
   * 
   * @param maze  The maze which this event is tied to.
   * @param origin The original position of Chap.
   * @param target  The new position of Chap.
   * @param direction The direction moved.
   * @param info The InfoField.
   */
  public MazeEventInfoField(Maze maze, PathTile origin, PathTile target, Maze.Direction direction,
      InfoField info) {
    super(maze, origin, target, direction);
    this.info = info;
  }

  /**
   * Get the InfoField.
   * @return  InfoField walked on by Chap.
   */
  public InfoField getInfoField() {
    return info;
  }

  @Override
  public void receive(MazeEventListener l) {
    l.update(this);
  }
}
