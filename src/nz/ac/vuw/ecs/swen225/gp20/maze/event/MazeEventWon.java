package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.Direction;

/**
 * Event for when chap walks onto the exit tile.
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventWon extends MazeEventWalked {
  public MazeEventWon(Maze maze, PathTile origin, PathTile destination, Direction direction) {
    super(maze, origin, destination, direction);
  }

  @Override
  public String toString() {
    return String.format("Walked onto the exit tile at %s,%s and beat the level!",
        destination.getCol(), destination.getRow());
  }
  
  @Override
  public void recieve(MazeEventListener l) {
    l.update(this);
  }
}
