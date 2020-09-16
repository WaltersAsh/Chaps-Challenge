package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * A wall tile, which Chap may not walk on
 * 
 * @author Ian 300474717
 *
 */

public class WallTile extends Tile{
	public WallTile(String filename) {
		super(filename, "▓▓");
		this.walkable = false;
	}

	public WallTile(String id, String initials, String filename, int row, int col, boolean walkable) {
		super(id, initials, filename, row, col, walkable);
	}

	public WallTile() {
	}
}
