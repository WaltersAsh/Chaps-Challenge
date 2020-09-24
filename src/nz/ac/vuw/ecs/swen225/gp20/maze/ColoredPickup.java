package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.KeyColor;

/**
 * The Door, should only unlock if the Chap has the matching Key
 *
 * @author Ian 300474717
 */

public class ColoredPickup extends Pickup {
  public void setColor(KeyColor color) {
    this.color = color;
  }

  /**
   * Instantiates a new empty Colored pickup.
   */
  public ColoredPickup() {
  }

  protected KeyColor color; // may not be set

  public ColoredPickup(String filename, String initials, KeyColor color) {
    super(filename, initials);
    this.color = color;
  }

  public KeyColor getColor() {
    return color;
  }
}
