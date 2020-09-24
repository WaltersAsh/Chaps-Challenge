package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when chap moves from one tile to another.
 *
 * @author Ian 300474717
 *
 */
public class MazeEventWalked implements MazeEvent {
  protected PathTile origin, target;
  protected Maze.Direction direction;
  protected Maze maze;

  public MazeEventWalked(Maze maze, PathTile origin, PathTile target, Maze.Direction direction) {
    this.maze = maze;
    this.origin = origin;
    this.target = target;
    this.direction = direction;
  }

  /**
   * The tile that Chap moved onto intially (barring teleports)
   * @return
   */
  public PathTile getTarget() {
    return target;
  }

  /**
   * Target and destination are not always the same in subclasses
   * eg: if teleported, then the Teleporter is the target
   * but the other end of the Teleporter is the destination
   *
   * @return
   */
  public PathTile getDestination() {
    return target;
  }

  public PathTile getOrigin() {
    return origin;
  }

  public Maze.Direction getDirection() {
    return direction;
  }

  public Maze getMaze() {
    return maze;
  }


  @Override
  public String toString() {
    return String.format("Walked from tile %s,%s to tile %s,%s", origin.getCol(), origin.getRow(),
        target.getCol(), target.getRow());
  }

  @Override
  public void receive(MazeEventListener l) {
    l.update(this);
  }
}
