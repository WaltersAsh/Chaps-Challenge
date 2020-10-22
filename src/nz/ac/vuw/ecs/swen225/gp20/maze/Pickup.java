package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Base class for entities the Chap can pick up.
 *
 * @author Ian 300474717
 *
 */

public abstract class Pickup extends Containable {
  /**
   * Empty constructor for Persistence.
   */
  public Pickup() {
  }

  /**
   * Construct a new instance.
   * 
   * @param filename The filename of the image to use for this entity.
   * @param initials The initials to represent this entity.
   */
  public Pickup(String filename, String initials) {
    super(filename, initials);
  }

  @Override
  public void onWalked(Maze m) {
    m.getChap().pickup(this);
  }
}
