package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Base class for Containables which prevent other entities moving onto them.
 * 
 * @author Ian 300474717
 */
public abstract class BlockingContainable extends Containable {

  /**
   * Empty constructor for Persistence.
   */
  public BlockingContainable() {
  }

  /**
   * Construct a new instance.
   * 
   * @param filename The filename of the image to use for this entity.
   * @param initials The initials to represent this entity.
   */
  public BlockingContainable(String filename, String initials) {
    super(filename, initials);
  }
}
