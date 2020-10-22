package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.KeyColor;

/**
 * The Door, should only unlock if the Chap has the matching Key.
 *
 * @author Ian 300474717
 */

public class Door extends BlockingContainable {
  private KeyColor color;

  /**
   * Empty constructor for Persistence.
   */
  public Door() {
  }
  
  /**
   * Construct a new instance.
   * 
   * @param filename The filename of the image to use for this entity.
   * @param color The key color.
   */
  public Door(String filename, Maze.KeyColor color) {
    super(filename, "D" + color.toString().charAt(0));
    this.color = color;
  }


  /**
   * Get the Color.
   * @return  Current Color.
   */
  public KeyColor getColor() {
    return color;
  }
  
  /**
   * Set the Color.
   * @param color New Color.
   */
  public void setColor(Maze.KeyColor color) {
    this.color = color;
  }

  @Override
  public String toString() {
    return color.toString() + " Door";
  }
}
