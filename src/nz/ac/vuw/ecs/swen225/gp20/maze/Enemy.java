package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * The pathfinding enemy tokens
 *
 * @author Ian 300474717
 *
 */

public class Enemy extends Movable {
  PathFinder pf;

  public Enemy(String filename) {
    super(filename, "EN");
  }

  public void initPathFinder(Maze m) {
    pf = new PathFinder(m);
  }

  public Tile tickPathFinding() {
    return pf.next(container, PathFinder.Mode.STRAIGHT_RANDOM);
  }
}
