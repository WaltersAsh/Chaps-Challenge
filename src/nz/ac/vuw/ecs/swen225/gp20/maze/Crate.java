package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * A Crate which may be pushed by Chap
 * 
 * @author Ian 300474717
 *
 */

public class Crate extends Movable {
  /**
   * Instantiates a new Crate. Empty constructor for Jackson.
   */
  public Crate() {
  }

  public Crate(String filename) {
    super(filename, "XX");
  }

  @Override
  public String toString() {
    return "Crate";
  }
}
