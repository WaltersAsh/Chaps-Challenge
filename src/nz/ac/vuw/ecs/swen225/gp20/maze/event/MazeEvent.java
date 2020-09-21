package nz.ac.vuw.ecs.swen225.gp20.maze.event;

/**
 * Base class for all broadcast-able events which happen in Maze.
 * 
 * @author Ian 300474717
 *
 */
public interface MazeEvent {
  /**
   * Receive this specific implementation/subclass of event
   * 
   * @param listener
   */
  public void receive(MazeEventListener listener);
}
