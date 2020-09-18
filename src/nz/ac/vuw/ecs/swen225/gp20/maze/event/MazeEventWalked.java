package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when chap moves from one tile to another.
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventWalked implements MazeEvent {
  protected Tile origin, destination;
  protected Maze.Direction direction;
  protected Maze maze;
  
  public MazeEventWalked(Maze maze, Tile origin, Tile destination, Maze.Direction direction) {
    this.maze = maze;
    this.origin = origin;
    this.destination = destination;
    this.direction = direction;
  }

  public Tile getDestination() {
    return destination;
  }

  public Tile getOrigin() {
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
  public void accept(MazeEventListener l) {
    l.update(this);
  }
}
