package maze;

/**
 * A wall tile, which Chap may not walk on
 * 
 * @author Ian 300474717
 *
 */

public class WallTile extends Tile{
	public WallTile(String filename, String initials) {
		super(filename, initials);
		this.walkable = false;
	}
}
