package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Base class for objects the Chap can pick up
 *
 * @author Ian 300474717
 *
 */

public abstract class Trigger extends Containable {

  public Trigger(String filename, String initials) {
    super(filename, initials);
  }
}
