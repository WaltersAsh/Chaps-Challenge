package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Base class for objects the Chap can pick up
 *
 * @author Ian 300474717
 *
 */

public abstract class Pickup extends Containable {
  /**
   * Instantiates a new empty Pickup. For Jackson
   */
  public Pickup() {
  }

  public Pickup(String filename, String initials) {
    super(filename, initials);
  }

  @Override
  public void onWalked(Maze m) {
    m.getChap().pickup(this);
  }
}
