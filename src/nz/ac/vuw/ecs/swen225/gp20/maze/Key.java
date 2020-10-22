package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.KeyColor;

/**
 * A Key which may be picked up by Chap and unlocks a matching Door.
 *
 * @author Ian 300474717
 */

public class Key extends Pickup {
  private KeyColor color; // may not be set

  /**
   * Empty constructor for Persistence.
   */
  public Key() {
  }

  /**
   * Construct a new instance.
   * 
   * @param filename The filename of the image to use for this entity.
   * @param color The key color.
   */
  public Key(String filename, Maze.KeyColor color) {
    super(filename, "K" + color.toString().charAt(0));
    this.color = color;
  }

  /**
   * Get the Color.
   * @return  Current Color.
   */
  public KeyColor getColor() {
    return color;
  }

  @Override
  public String toString() {
    return color.toString() + " Key";
  }

}
