package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * The Door, should only unlock if the Chap has the matching Key
 *
 * @author Ian 300474717
 *
 */

public class Water extends BlockingContainable {
  /**
   * Instantiates a new Water. Empty constructor for Jackson.
   */
  public Water() {
  }

  public Water(String filename) {
    super(filename, "WT");
  }
}
