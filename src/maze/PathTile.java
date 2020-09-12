package maze;

import java.util.*;

import com.google.common.base.Preconditions;


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
	 * Remove a Containable inside this Tile
	 * @param c		The Containable
	 */
	public void remove(Containable c) {
		contains.remove(c);
		c.setContainer(this);
	}
	
	/**
	 * Move a Containable inside this Tile and update the previous Container
	 * @param c		The Containable
	 */
	public void moveTo(Containable c) {
		c.getContainer().remove(c);
		place(c);
	}
	
	/**
	 * Get the initials of the top contained object instead
	 */
	@Override
	public String getInitials() {
		if(!contains.isEmpty())return contains.peek().getInitials();
		return super.getInitials();
	}
	
	/**
	 * Trigger any events when chap walks on this tile
	 * @param c		Chap
	 */
	public void onWalked(Chap c) {
		for(Containable cont: contains) {
			if(cont instanceof Pickup) {
				((Pickup)cont).onWalked(c);
			}
		}
	}
}
