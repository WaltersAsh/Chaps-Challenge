package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.rendering.SoundEffect;

/**
 * The Door, should only unlock if the Chap has the matching Key
 *
 * @author Ian 300474717
 *
 */

public class Water extends BlockingContainable {
  public Water(String filename) {
    super(filename, "WT");
  }
}
