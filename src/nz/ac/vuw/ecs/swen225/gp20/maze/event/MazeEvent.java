package nz.ac.vuw.ecs.swen225.gp20.maze.event;

/**
 * Base class for all broadcast-able events which happen in Maze.
 * 
 * @author Ian 300474717
 *
 */
public interface MazeEvent {
  /**
   * Dispatch this event to a listener.
   * 
   * @param listener  The listener to send this event to.
   */
  public void receive(MazeEventListener listener);
  
  /**
   * Undo this MazeEvent.
   */
  public void invert();
}
