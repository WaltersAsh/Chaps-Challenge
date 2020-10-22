package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.PathTile;
import nz.ac.vuw.ecs.swen225.gp20.maze.Teleporter;

/**
 * Event for when Chap walks onto a Teleporter and is teleported.
 *
 * @author Ian 300474717
 *
 */
public class MazeEventTeleported extends MazeEventWalked {
  private Teleporter teleporter;

  /**
   * Construct a new instance.
   * 
   * @param maze  The maze which this event is tied to.
   * @param origin The original position of Chap.
   * @param target  The new position of Chap.
   * @param direction The direction moved.
   * @param tele The Teleporter triggered.
   */
  public MazeEventTeleported(Maze maze, PathTile origin, PathTile target, Maze.Direction direction,
      Teleporter tele) {
    super(maze, origin, target, direction);
    this.teleporter = tele;
  }

  /**
   * Get the triggered Teleporter.
   * @return  The Teleporter.
   */
  public Teleporter getTeleporter() {
    return teleporter;
  }

  @Override
  public void receive(MazeEventListener l) {
    l.update(this);
  }

  @Override
  public PathTile getDestination() {
    return teleporter.getDestination();
  }
  
  @Override
  public void invert() {
    teleporter.getContainer().moveTo(maze.getChap());
    super.invert();
  }
}
