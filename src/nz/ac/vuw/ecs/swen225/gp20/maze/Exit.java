package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * The Exit tile and goal of the level.
 *
 * @author Ian 300474717
 *
 */

public class Exit extends Trigger {

  /**
   * Empty constructor for Persistence.
   */
  public Exit() {
  }
  
  /**
   * Construct a new instance.
   * 
   * @param filename The filename of the image to use for this entity.
   */
  public Exit(String filename) {
    super(filename, "EX");
  }

}
