package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * The Door, should only unlock if the Chap has the matching Key
 * 
 * @author Ian 300474717
 *
 */

public class Door extends BlockingContainable {
  private Maze.KeyColor color;

  public Door(String filename, Maze.KeyColor color) {
    super(filename, "D" + color.toString().charAt(0));
    this.color = color;
  }

  public Maze.KeyColor getColor() {
    return color;
  }

  public void setColor(Maze.KeyColor color) {
    this.color = color;
  }

  @Override
  public String toString() {
    return color.toString() + " Door";
  }
}
