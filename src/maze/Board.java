package maze;

/**
 * The game board, which keeps track of the Tiles in the game
 * @author Ian 300474717
 *
 */

public class Board {
	private Tile[][] tiles;
	
	/**
	 * Constuct empty Board with a width and height
	 * @param width		width of board
	 * @param height	height of board
	 */
	public Board(int width, int height) {
		tiles = new Tile[width][height];
	}
	
	/**
	 * Construct Board from predetermined Tiles
	 * @param t		Tile[][] of Tiles
	 */
	public Board(Tile[][] t) {
		tiles = t;
	}
	
	public Tile getTileAt(int row, int col) {
		return tiles[row][col];
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
}
