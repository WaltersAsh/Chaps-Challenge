package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * A wall tile, which Chap may not walk on
 * 
 * @author Ian 300474717
 *
 */

public class WallTile extends Tile {
  public WallTile(String filename) {
    super(filename, "▓▓");
    this.walkable = false;
  }
}
