package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Shows an information popup when Chap walks over it
 *
 * @author Ian 300474717
 *
 */

public class Teleporter extends Pickup {
  private Teleporter other;

  public Teleporter(String filename, Maze.KeyColor color) {
    super(filename, "T" + color.toString().charAt(0));
  }

  public void setOther(Teleporter o) {
    other = o;
  }

  public Teleporter getOther() {
    return other;
  }
}
