package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Base class for all Tiles representable on the board
 *
 * @author Ian 300474717
 */

public abstract class Tile extends Drawable {
  /**
   * Instantiates a new Tile. For Jackson.
   */
  public Tile() {
  }

  int row, col;
  protected boolean walkable = false;

  public Tile(String filename, String initials) {
    super(filename, initials);
  }

  /**
   * can the Chap walk on this Tile?
   *
   * @return whether Chap can walk on this Tile
   */
  public boolean isWalkable() {
    return walkable;
  }


  public boolean enemyWalkable() {
    return walkable;
  }

  /**
   * Set the coordinates of this Drawable
   *
   * @param r the row
   * @param c the column
   */
  public void setCoords(int r, int c) {
    row = r;
    col = c;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  @Override
  public String toString() {
    return this.getClass() + " at " + row + ", " + col;
  }
}
