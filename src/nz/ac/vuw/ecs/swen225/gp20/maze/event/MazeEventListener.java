package nz.ac.vuw.ecs.swen225.gp20.maze.event;

/**
 * Base class for any Listeners of Maze.
 * Not abstract as you may not want to override every method.
 * Implements the Visitor Pattern: https://en.wikipedia.org/wiki/Visitor_pattern
 *
 * @author Ian 300474717
 *
 */
public class MazeEventListener {
  /**
   * Update when an InfoField is walked on.
   * @param e Event which was received.
   */
  public void update(MazeEventInfoField e) {
  }

  /**
   * Update when a Pickup is obtained by Chap.
   * @param e Event which was received.
   */
  public void update(MazeEventPickup e) {
  }

  /**
   * Update when a Crate is pushed by Chap.
   * @param e Event which was received.
   */
  public void update(MazeEventPushed e) {
  }

  /**
   * Update when a Door is unlocked by Chap.
   * @param e Event which was received.
   */
  public void update(MazeEventUnlocked e) {
  }

  /**
   * Update when a Pickup is obtained by Chap.
   * @param e Event which was received.
   */
  public void update(MazeEventWalked e) {
  }
  
  /**
   * Update when Chap enters Exit.
   * @param e Event which was received.
   */
  public void update(MazeEventWon e) {
  }

  /**
   * Update when a Crate is pushed into water by Chap.
   * @param e Event which was received.
   */
  public void update(MazeEventPushedWater e) {
  }

  /**
   * Update when a the final Treasure is obtained and the ExitLock opens.
   * @param e Event which was received.
   */
  public void update(MazeEventExitUnlocked e) {
  }

  /**
   * Update when an Enemy path finds.
   * @param e Event which was received.
   */
  public void update(MazeEventEnemyWalked e) {
  }

  /**
   * Update when Chap teleports.
   * @param e Event which was received.
   */
  public void update(MazeEventTeleported e) {
  }

  /**
   * Update when Chap walks onto an Enemy and dies.
   * @param e Event which was received.
   */
  public void update(MazeEventWalkedKilled e) {
  }

  /**
   * Update when an Enemy walks onto Chap and kills Chap.
   * @param e Event which was received.
   */
  public void update(MazeEventEnemyWalkedKilled e) {
  }
  
  /**
   * Update when Chap walks onto Water and dies.
   * @param e Event which was received.
   */
  public void update(MazeEventWalkedDrowned e) {
  }

  /**
   * Listen to any update from any MazeEvent.
   * @param e Event which was received.
   */
  public void update(MazeEvent e) {

  }
}
