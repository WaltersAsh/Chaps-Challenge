package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Water entity. Kills Chap when walked on, but may be transformed into PathTile
 * if a Crate is pushed onto it.
 *
 * @author Ian 300474717
 *
 */

public class Water extends BlockingContainable {
  /**
   * Empty constructor for Persistence.
   */
  public Water() {
  }

  /**
   * Construct a new instance.
   * 
   * @param filename The filename of the image to use for this entity.
   */
  public Water(String filename) {
    super(filename, "WT");
  }
}
