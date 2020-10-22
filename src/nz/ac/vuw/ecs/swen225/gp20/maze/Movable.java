package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Base class for objects which can move or be moved around the board.
 * 
 * @author Ian 300474717
 *
 */

public abstract class Movable extends BlockingContainable {
  /**
   * Empty constructor for Persistence.
   */
  public Movable() {
  }

  /**
   * Construct a new instance.
   * 
   * @param filename The filename of the image to use for this entity.
   * @param initials The initials to represent this entity.
   */
  public Movable(String filename, String initials) {
    super(filename, initials);
  }
}
