package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.KeyColor;

/**
 * A Key which may be picked up by Chap and unlocks a matching Door
 *
 * @author Ian 300474717
 */

public class Key extends Pickup {
  private KeyColor color; // may not be set

  /**
   * Instantiates a new empty Key.
   */
  public Key() {
  }


  public Key(String filename, Maze.KeyColor color) {
    super(filename, "K" + color.toString().charAt(0));
    this.color = color;
  }

  public KeyColor getColor() {
    return color;
  }

  @Override
  public String toString() {
    return color.toString() + " Key";
  }

}
