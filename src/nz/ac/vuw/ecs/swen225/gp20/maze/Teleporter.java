package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.KeyColor;

/**
 * An entity which teleports Chap when walked on.
 *
 * @author Ian 300474717
 *
 */

public class Teleporter extends Trigger {
  private Teleporter other;
  private KeyColor color;

  /**
   * Empty constructor for Persistence.
   */
  public Teleporter() {
  }

  /**
   * Construct a new instance.
   * 
   * @param filename The filename of the image to use for this entity.
   * @param color    The color of this Teleporter.
   */
  public Teleporter(String filename, Maze.KeyColor color) {
    super(filename, "P" + color.toString().charAt(0));
    this.color = color;
  }

  /**
   * Set the linked Teleporter.
   * 
   * @param o Other Teleporter to link to.
   */
  public void setOther(Teleporter o) {
    other = o;
  }

  /**
   * Get the linked Teleporter.
   * 
   * @return The linked Teleporter.
   */
  public Teleporter getOther() {
    return other;
  }

  /**
   * Get this Teleporter's color.
   * 
   * @return The color.
   */
  public KeyColor getColor() {
    return color;
  }

  /**
   * Get the destination.
   * 
   * @return Destination of this Teleporter (the location of the linked
   *         Teleporter).
   */
  @JsonIgnore
  public PathTile getDestination() {
    return other.getContainer();
  }

  /**
   * Set the color.
   * 
   * @param color New color.
   */
  public void setColor(KeyColor color) {
    this.color = color;
  }

  @Override
  public void onWalked(Maze m) {
    getDestination().moveTo(m.getChap());
  }
}
