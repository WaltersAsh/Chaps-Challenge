package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.Direction;

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

  public Direction tickPathFinding() {
    return pf.next(container, PathFinder.Mode.STRAIGHT_ANTICLOCKWISE);
  }
}
