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
	 * Trigger any events when Chap walks over the Tile which contains this Containable
	 * @param c 	Chap
	 */
	public void onWalked(Maze m) {
		this.container.remove(this);
		m.getChap().pickup(this);
		System.out.printf("[pickup] you picked up a %s\n", this);
	}
}