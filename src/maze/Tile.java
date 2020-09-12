package maze;

/**
 * Base class for all Tiles representable on the board
 * 
 * @author Ian 300474717
 *
 */

public abstract class Tile extends Drawable {
	int row, col;
	boolean walkable = false;
	
	public Tile(String filename, String initials) {
		super(filename, initials);
	}
	
	/**
	 * can the Chap walk on this Tile?
	 * @return		whether Chap can walk on this Tile
	 */
	public boolean isWalkable() {
		return walkable;
	}
	
	/**
	 * Set the coordinates of this Drawable
	 * @param r		the row
	 * @param c		the column
	 */
	public void setCoords(int r, int c) {
		row = r;
		col = c;
	}
	

	@Override
	public String toString() {
		return this.getClass() + " at " + row + ", "+ col; 
	}
}
