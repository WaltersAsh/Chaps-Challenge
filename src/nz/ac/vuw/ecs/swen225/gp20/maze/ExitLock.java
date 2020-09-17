package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.rendering.SoundEffect;

/**
 * The ExitLock, should only unlock if the Chap has collected all Treasures
 * 
 * @author Ian 300474717
 *
 */

public class ExitLock extends BlockingContainable {
	public ExitLock(String filename) {
		super(filename, "EL");
	}
}
