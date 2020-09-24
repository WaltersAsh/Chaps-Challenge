package nz.ac.vuw.ecs.swen225.gp20.maze;

public abstract class BlockingContainable extends Containable {

  /**
   * Instantiates a new empty Blocking containable. For Jackson.
   */
  public BlockingContainable() {
  }

  public BlockingContainable(String filename, String initials) {
    super(filename, initials);
  }
}
