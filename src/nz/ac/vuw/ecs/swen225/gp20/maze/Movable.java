package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Base class for objects which can move or be moved around the board
 * 
 * @author Ian 300474717
 *
 */

public abstract class Movable extends BlockingContainable{

	public Movable(String filename, String initials) {
		super(filename, initials);
	}
}
