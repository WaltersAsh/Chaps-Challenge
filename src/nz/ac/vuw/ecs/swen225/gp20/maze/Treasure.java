package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * A Treasure entity which may be picked up by Chap. Once all Treasures are
 * picked up, the ExitLock opens.
 *
 * @author Ian 300474717
 *
 */

public class Treasure extends Pickup {
  /**
   * Empty constructor for Persistence.
   */
  public Treasure() {
  }

  /**
   * Construct a new instance.
   * 
   * @param filename The filename of the image to use for this entity.
   */
  public Treasure(String filename) {
    super(filename, "TR");
  }

  @Override
  public String toString() {
    return "Treasure";
  }

  @Override
  public void onWalked(Maze m) {
    super.onWalked(m);
    m.getTreasures().remove(this);
  }
}
