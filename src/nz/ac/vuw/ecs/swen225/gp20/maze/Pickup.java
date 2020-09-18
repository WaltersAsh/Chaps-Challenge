package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Base class for objects the Chap can pick up
 * 
 * @author Ian 300474717
 *
 */

public abstract class Pickup extends Containable{

	public Pickup(String filename, String initials) {
		super(filename, initials);
	}

	/**
	 * Pick up this Pickup
	 * @param c 	Chap
	 */
	public void addToInventory(Chap c) {
		this.container.remove(this);
		c.pickup(this);
	}
}
