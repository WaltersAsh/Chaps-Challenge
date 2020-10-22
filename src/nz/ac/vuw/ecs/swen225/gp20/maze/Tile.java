package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Base class for all Tiles representable on the board.
 *
 * @author Ian 300474717
 */

public abstract class Tile extends Drawable {
  int row;
  int col;
  protected boolean walkable = false;

  /**
   * Empty constructor for Persistence.
   */
  public Tile() {
  }

  /**
   * Construct a new instance.
   * 
   * @param filename The filename of the image to use for this entity.
   * @param initials The initials to represent this entity.
   */
  public Tile(String filename, String initials) {
    super(filename, initials);
  }

  /**
   * Whether the Chap may walk on this Tile.
   *
   * @return Whether Chap may walk on this Tile.
   */
  public boolean isWalkable() {
    return walkable;
  }

  /**
   * Whether an Enemy may walk on this Tile.
   *
   * @return Whether Enemy may walk on this Tile.
   */
  public boolean enemyWalkable() {
    return walkable;
  }

  /**
   * Set the coordinates of this Drawable.
   *
   * @param r the row
   * @param c the column
   */
  public void setCoords(int r, int c) {
    row = r;
    col = c;
  }

  /**
   * Get the row of this Tile.
   * 
   * @return Current row.
   */
  public int getRow() {
    return row;
  }

  /**
   * Get the column of this Tile.
   * 
   * @return Current column.
   */
  public int getCol() {
    return col;
  }

  @Override
  public String toString() {
    return this.getClass() + " at " + row + ", " + col;
  }
}
