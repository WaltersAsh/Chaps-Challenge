package maze;

/**
 * Base class for objects which can move or be moved around the board
 * 
 * @author Ian 300474717
 *
 */

public abstract class Movable extends Containable{

	public Movable(String filename, String initials) {
		super(filename, initials);
	}
	
	public boolean canMove(Tile destination) {
		return false;
	}
}
