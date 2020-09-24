package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when chap walks onto an InfoField
 *
 * @author Ian 300474717
 *
 */
public class MazeEventTeleported extends MazeEventWalked {
  private Teleporter teleporter;

  public MazeEventTeleported(Maze maze, PathTile origin, PathTile destination, Maze.Direction direction,
      Teleporter tele) {
    super(maze, origin, destination, direction);
    this.teleporter = tele;
  }

  public Teleporter getTeleporter() {
    return teleporter;
  }

  @Override
  public String toString() {
    return String.format("Teleported to %s after walking onto teleporter at tile %s,%s",
        teleporter.getOther().getContainer(), destination.getCol(), destination.getRow());
  }

  @Override
  public void receive(MazeEventListener l) {
    l.update(this);
  }
}
