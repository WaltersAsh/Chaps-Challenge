package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when chap walks onto Teleporter and is Teleported
 *
 * @author Ian 300474717
 *
 */
public class MazeEventTeleported extends MazeEventWalked {
  private Teleporter teleporter;

  public MazeEventTeleported(Maze maze, PathTile origin, PathTile target, Maze.Direction direction,
      Teleporter tele) {
    super(maze, origin, target, direction);
    this.teleporter = tele;
  }

  public Teleporter getTeleporter() {
    return teleporter;
  }

  @Override
  public String toString() {
    return String.format("Teleported to %s after walking onto teleporter at tile %s,%s",
        teleporter.getOther().getContainer(), target.getCol(), target.getRow());
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
