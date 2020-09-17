package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.rendering.SoundEffect;

/**
 * Base class for objects which can move or be moved around the board
 * 
 * @author Ian 300474717
 *
 */

public abstract class Movable extends BlockingContainable{

	public Movable(String filename, String initials, SoundEffect soundEffect) {
		super(filename, initials, soundEffect);
	}
}
