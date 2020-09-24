package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * A Key which may be picked up by Chap and unlocks a matching Door
 *
 * @author Ian 300474717
 */

public class Key extends ColoredPickup {
  /**
   * Instantiates a new empty Key.
   */
  public Key() {
  }


  public Key(String filename, Maze.KeyColor color) {
    super(filename, "K" + color.toString().charAt(0), color);
  }

  @Override
  public String toString() {
    return color.toString() + " Key";
  }
}
