package nz.ac.vuw.ecs.swen225.gp20.maze.event;

/**
 * Base class for any Listeners of Maze.
 * Not abstract as you may not want to override every method.
 * 
 * Implements the Visitor Pattern: https://en.wikipedia.org/wiki/Visitor_pattern
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventListener {
  public void update(MazeEventInfoField e) {
  }
  
  public void update(MazeEventPickup e) {
  }
  
  public void update(MazeEventPushed e) {
  }
  
  public void update(MazeEventUnlocked e) {
  }
  
  public void update(MazeEventWalked e) {
  }
  
  public void update(MazeEventWon e) {
  }
  
  public void update(MazeEventPushedWater e) {
  }
  
  public void update(MazeEventExitUnlocked e) {
  }
  
  /**
   * Listen to update from any MazeEvent.
   * 
   * @param e the event
   */
  public void update(MazeEvent e) {
    
  }
}
