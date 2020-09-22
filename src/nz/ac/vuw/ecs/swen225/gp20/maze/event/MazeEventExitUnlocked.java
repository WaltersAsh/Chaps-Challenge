package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when the exit lock unlocks after picking up the last treasure
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventExitUnlocked extends MazeEventPickup {
  protected ExitLock exitlock;

  public MazeEventExitUnlocked(Maze maze, PathTile origin, PathTile destination, Maze.Direction direction,
      Pickup picked, ExitLock exitlock) {
    super(maze, origin, destination, direction, picked);
    this.exitlock= exitlock;
  }

  @Override
  public String toString() {
    return String.format("Picked up final treasure %s at %s,%s and unlocked %s", picked, destination.getCol(),
        destination.getRow(), exitlock);
  }
  
  @Override
  public void receive(MazeEventListener l) {
    l.update(this);
  }
  
  public ExitLock getExitlock() {
    return exitlock;
  }

}
