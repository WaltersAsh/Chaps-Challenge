package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when chap picks something up off a tile they just walked onto.
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventPickup extends MazeEventWalked {
  private Pickup picked;

  public MazeEventPickup(Maze maze, Tile origin, Tile destination, Maze.Direction direction,
      Pickup picked) {
    super(maze, destination, destination, direction);
    this.picked = picked;
  }

  public Pickup getPicked() {
    return picked;
  }

  @Override
  public String toString() {
    return String.format("Picked up a %s at %s,%s", picked, destination.getCol(),
        destination.getRow());
  }
  
  @Override
  public void accept(MazeEventListener l) {
    l.update(this);
  }
}
