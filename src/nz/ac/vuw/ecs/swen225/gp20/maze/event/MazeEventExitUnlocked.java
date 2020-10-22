package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import com.google.common.base.Preconditions;
import nz.ac.vuw.ecs.swen225.gp20.maze.ExitLock;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.PathTile;
import nz.ac.vuw.ecs.swen225.gp20.maze.Pickup;


/**
 * Event for when the ExitLock unlocks after picking up the last treasure.
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventExitUnlocked extends MazeEventPickup {
  protected ExitLock exitlock;
  private PathTile exitlocktile;

  /**
   * Construct a new instance.
   * 
   * @param maze  The maze which this event is tied to.
   * @param origin The original position of Chap.
   * @param target  The new position of Chap.
   * @param direction The direction moved.
   * @param picked The Pickup picked up.
   * @param exitlock The ExitLock opened.
   * @param exitlockposition The PathTile where the ExitLock was.
   */
  public MazeEventExitUnlocked(Maze maze, PathTile origin, PathTile target, 
      Maze.Direction direction, Pickup picked, ExitLock exitlock, PathTile exitlockposition) {
    super(maze, origin, target, direction, picked);
    Preconditions.checkArgument(maze.getTreasures().isEmpty(), 
        "Treasures must be empty to unlock exit");
    this.exitlock = exitlock;
    this.exitlocktile = exitlockposition;
  }

  @Override
  public void receive(MazeEventListener l) {
    l.update(this);
  }
  
  /**
   * Get the opened ExitLock.
   * @return the ExitLock.
   */
  public ExitLock getExitlock() {
    return exitlock;
  }
  
  @Override
  public void invert() {
    exitlocktile.place(exitlock);
    super.invert();
  }
}
