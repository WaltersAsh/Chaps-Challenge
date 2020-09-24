package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * A Treasure which may be picked up by Chap and unlocks the final ExitLock once
 * all have been obtained
 *
 * @author Ian 300474717
 *
 */

public class Treasure extends Pickup {
  public Treasure(String filename) {
    super(filename, "TR");
  }

  /**
   * Instantiates a new empty Treasure. For Jackson.
   */
  public Treasure() {
  }

  @Override
  public String toString() {
    return "Treasure";
  }
}
