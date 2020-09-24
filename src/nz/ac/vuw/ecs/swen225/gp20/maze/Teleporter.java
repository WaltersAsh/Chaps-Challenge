package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Shows an information popup when Chap walks over it
 *
 * @author Ian 300474717
 *
 */

public class Teleporter extends ColoredPickup {
  private Teleporter other;

  public Teleporter(String filename, Maze.KeyColor color) {
    super(filename, "P" + color.toString().charAt(0), color);
    this.color = color;
  }

  public void setOther(Teleporter o) {
    other = o;
  }

  public Teleporter getOther() {
    return other;
  }
}
