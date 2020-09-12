package maze;

/**
 * The Maze board, which keeps track of the Tiles and logic in the game
 * @author Ian 300474717
 *
 */

public class Maze {
	public enum Direction{
		UP,
		DOWN,
		LEFT,
		RIGHT
	}
	
	public enum KeyColor{
		BLUE,
		RED,
		GREEN,
		YELLOW
	}
	
	private Tile[][] tiles;
	private Chap chap;
	
	/**
	 * Constuct empty Board with a width and height
	 * 
	 * @param width		width of board
	 * @param height	height of board
	 */
	public Maze(int width, int height) {
		tiles = new Tile[width][height];
	}
	
	/**
	 * Construct Board from predetermined Tiles
	 * 
	 * @param t		Tile[][] of Tiles
	 * @param c 	Main chap token
	 */
	public Maze(Tile[][] t, Chap c) {
		tiles = t;
		chap = c;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for(Tile[] rows: tiles) {
			for(Tile t: rows) {
				s.append(t.getInitials());
			}
			s.append("\n");
		}
		return s.toString();
	}
	
	public void move(Direction d) {
		Tile current = chap.getContainer();
		Tile next = tileTo(current,d);
		
		if(next instanceof PathTile) {
			PathTile ptnext = (PathTile) next;
			if(ptnext.isBlocked()) {
				if(!checkBlocking(ptnext)) return; // return if we can't move to the blocked tile
			}
			ptnext.moveTo(chap);
			ptnext.onWalked(chap);
		}
	}
	
	/**
	 * Check if we can move onto a "blocked" PathTile
	 * 
	 * We could move onto it for example if we
	 * had the matching key to the blocking door.
	 * 
	 * @param blocked	the PathTile to check
	 * @return			if we could move onto it
	 */
	public boolean checkBlocking(PathTile blocked) {
		BlockingContainable bc = blocked.getBlocker();
		if(bc instanceof Door) {
			Door door = (Door) bc;
			Key key = chap.hasMatchingKey(door);
			if(key != null) {
				blocked.remove(door);
				// Green key may be used unlimited times
				if(!key.getColor().equals(KeyColor.GREEN)) {
					chap.getInventory().remove(key);
				}
				System.out.println("[door] unlocked with "+key.getColor()+" key");
				return true;
			}
		}
		return false;
	}
	
	public Tile tileTo(Tile t, Direction d) {
		switch(d) {
		case DOWN:
			return tiles[t.row+1][t.col];
		case LEFT:
			return tiles[t.row][t.col-1];
		case RIGHT:
			return tiles[t.row][t.col+1];
		case UP:
			return tiles[t.row-1][t.col];
		default:
			return null;
		}
	}
	
	
	public Tile getTileAt(int row, int col) {
		return tiles[row][col];
	}
	
	public Chap getChap() {
		return chap;
	}

	public Tile[][] getTiles() {
		return tiles;
	}
}
