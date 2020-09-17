package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.util.*;

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
	private Set<Treasure> treasures = new HashSet<Treasure>();
	private List<Enemy> enemies = new ArrayList<Enemy>();
	private ExitLock exitlock;
	private boolean levelFinished = false;

	private int width,height;

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
	 * @param entities	what entities need to be placed
	 */
	public Maze(Tile[][] t, List<Containable> entities) {
		tiles = t;

		height = tiles.length;
		width = tiles[0].length;

		for(Containable c: entities) {
			if(c instanceof Treasure) {
				treasures.add((Treasure)c);
			}else if(c instanceof Chap) {
				chap = (Chap) c;
			}else if(c instanceof ExitLock) {
				exitlock = (ExitLock) c;
			}else if(c instanceof Enemy) {
				Enemy e = (Enemy)c;
				e.initPathFinder(this);
				enemies.add(e);

			}
		}
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
		if(d == Direction.LEFT){
			chap.changeFile(chap.left);
		}
		if(d == Direction.RIGHT){
			chap.changeFile(chap.right);
		}

		if(next instanceof PathTile) {
			PathTile ptnext = (PathTile) next;
			if(ptnext.isBlocked()) {
				if(checkBlocking(ptnext, d)) {
					// we can move
				}else {
					// return if we can't move to the blocked tile
					return;
				}
			}
			ptnext.moveTo(chap);
			ptnext.onWalked(this);

		}
	}

	/**
	 * Check if we can move onto a door PathTile
	 *
	 * We could move onto it if we
	 * had the matching key to the blocking door.
	 *
	 * @param blocked	the PathTile to check
	 * @return			if we could move onto it
	 */
	public boolean checkBlocking(PathTile blocked, Direction d) {
		BlockingContainable bc = blocked.getBlocker();
		if(bc instanceof Door) {
			return tryUnlockDoor((Door)bc);
		}else if(bc instanceof Crate) {
			return tryPushCrate((Crate)bc, d);
		}
		return false;
	}

	public boolean tryUnlockDoor(Door door) {
		Key key = chap.hasMatchingKey(door);
		if(key != null) {
			door.getContainer().remove(door);
			// Green key may be used unlimited times
			if(!key.getColor().equals(KeyColor.GREEN)) {
				chap.getKeys().remove(key);
			}
			System.out.println("[door] unlocked with "+key.getColor()+" key");
			return true;
		}
		return false;
	}

	/**
	 * Try to push a crate.
	 *
	 * @param c		the crate to push
	 * @param d		the direction in which to push
	 * @return		if we pushed the crate
	 */
	public boolean tryPushCrate(Crate c, Direction d) {
		Tile destination = tileTo(c.container, d);
		// if the tile we try to push to is a pathtile
		if(destination instanceof PathTile){
			PathTile pt = (PathTile)destination;
			// if the pathtile we try to push to is free, push the crate
			if(!pt.isBlocked()) {
				pt.moveTo(c);
				return true;
			// can also push crate onto water to make a path
			}else if(pt.getBlocker() instanceof Water) {
				pt.remove(pt.getBlocker());
				c.getContainer().remove(c);
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

	public void tickPathFinding() {
		for (Enemy e: enemies) {
			Tile destination = e.tickPathFinding();
			if(destination instanceof PathTile) {
				PathTile pt = (PathTile)destination;
				if(!pt.isBlocked()) {
					pt.moveTo(e);
				}
			}
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

	public boolean isLevelFinished() {
		return levelFinished;
	}

	public void setLevelFinished(boolean levelFinished) {
		this.levelFinished = levelFinished;
	}

	public int getWidth(){return width;}

	public int getHeight(){return height;}


	public Set<Treasure> getTreasures() {
		return treasures;
	}

	public int numTreasures() {
		return treasures.size();
	}

	public void openExitLock() {
		exitlock.getContainer().remove(exitlock);
	}
}
