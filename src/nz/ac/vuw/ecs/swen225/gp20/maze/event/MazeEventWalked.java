package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when chap moves from one tile to another.
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventWalked implements MazeEvent {
  protected PathTile origin, destination;
  protected Maze.Direction direction;
  protected Maze maze;
  
  public MazeEventWalked(Maze maze, PathTile origin, PathTile destination, Maze.Direction direction) {
    this.maze = maze;
    this.origin = origin;
    this.destination = destination;
    this.direction = direction;
  }

  public PathTile getDestination() {
    return destination;
  }

  public PathTile getOrigin() {
    return origin;
  }

  public Maze.Direction getDirection() {
    return direction;
  }

  @Override
  public String toString() {
    return String.format("Walked from tile %s,%s to tile %s,%s", origin.getCol(), origin.getRow(),
        destination.getCol(), destination.getRow());
  }
  
  @Override
  public void recieve(MazeEventListener l) {
    l.update(this);
  }
}
