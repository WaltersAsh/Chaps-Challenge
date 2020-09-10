package maze;

/**
 * Base class for all Tiles representable on the board
 * 
 * @author Ian 300474717
 *
 */

public abstract class Tile extends Drawable {
	
	public Tile(String filename, String initials) {
		super(filename, initials);
	}

	boolean walkable = false;
	
	public boolean isWalkable() {
		return walkable;
	}
}
