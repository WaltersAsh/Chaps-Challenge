package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Wall Tile, which Chap cannot walk on under any circumstance.
 * 
 * @author Ian 300474717
 *
 */

public class WallTile extends Tile {

  /**
   * Empty constructor for Persistence.
   */
  public WallTile() {
  }

  /**
   * Construct a new instance.
   * 
   * @param filename The filename of the image to use for this entity.
   */
  public WallTile(String filename) {
    super(filename, "▓▓");
    this.walkable = false;
  }
}
