package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * A Crate which may be pushed by Chap.
 * 
 * @author Ian 300474717
 *
 */

public class Crate extends Movable {
  /**
   * Empty constructor for Persistence.
   */
  public Crate() {
  }
  
  /**
   * Construct a new instance.
   * 
   * @param filename The filename of the image to use for this entity.
   */
  public Crate(String filename) {
    super(filename, "XX");
  }

  
  @Override
  public String toString() {
    return "Crate";
  }
}
