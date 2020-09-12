package maze;

import java.util.*;

/**
 * A tile which Chap may walk on, and may contain Containables
 * 
 * @author Ian 300474717
 *
 */

public class PathTile extends Tile{
	Stack<Containable> contains = new Stack<Containable>(); // the items on this FreeTile
	
	public PathTile(String filename) {
		super(filename, "░░");
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
	
	/**
	 * Get the initials of the top contained object instead
	 */
	@Override
	public String getInitials() {
		if(!contains.isEmpty())return contains.peek().getInitials();
		return super.getInitials();
	}
}
