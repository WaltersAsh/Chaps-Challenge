package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Base class for entities which have an action when Chap walks onto them.
 *
 * @author Ian 300474717
 *
 */

public abstract class Trigger extends Containable {
  /**
   * Empty constructor for Persistence.
   */
  public Trigger() {
  }

  /**
   * Construct a new instance.
   * 
   * @param filename The filename of the image to use for this entity.
   * @param initials The initials to represent this entity.
   */
  public Trigger(String filename, String initials) {
    super(filename, initials);
  }
}
