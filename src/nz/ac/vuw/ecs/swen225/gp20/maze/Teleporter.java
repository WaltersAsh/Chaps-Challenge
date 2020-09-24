package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.KeyColor;

/**
 * Shows an information popup when Chap walks over it
 *
 * @author Ian 300474717
 *
 */

public class Teleporter extends Trigger {
  private Teleporter other;
  private KeyColor color;

  public Teleporter(String filename, Maze.KeyColor color) {
    super(filename, "P" + color.toString().charAt(0));
    this.color = color;
  }

  public void setOther(Teleporter o) {
    other = o;
  }

  public Teleporter getOther() {
    return other;
  }

  public KeyColor getColor() {
    return color;
  }

  public PathTile getDestination() {
    return other.getContainer();
  }
  
  @Override
  public void onWalked(Maze m) {
    getDestination().moveTo(m.getChap());
  }
}
