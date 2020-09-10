package maze;

import java.util.*;

/**
 * A tile which Chap may walk on, and may contain Containables
 * 
 * @author Ian 300474717
 *
 */

public class FreeTile extends Tile{
	Stack<Containable> contains = new Stack<Containable>(); // the items on this FreeTile
	
	public FreeTile(String filename, String initials) {
		super(filename, initials);
		this.walkable = true;
	}
	
	/**
	 * Place a Containable inside this Tile
	 * @param c		The Containable
	 */
	public void place(Containable c) {
		contains.push(c);
		c.setContainer(this);
	}
}
