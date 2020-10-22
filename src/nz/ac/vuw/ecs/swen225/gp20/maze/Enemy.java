package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.Direction;

/**
 * The pathfinding enemy tokens
 *
 * @author Ian 300474717
 */

public class Enemy extends Movable {
  PathFinder pf;

  public PathFinder getPf() {
    return pf;
  }

  public void setPf(PathFinder pf) {
    this.pf = pf;
  }

  public Enemy() {
  }

  @JsonIgnore
  public void initPathFinder(Maze m) {
    pf = new PathFinder(m);
  }

  public Enemy(String filename) {
    super(filename, "EN");
  }

  public Direction tickPathFinding() {
    return tickPathFinding(PathFinder.Mode.STRAIGHT_ANTICLOCKWISE);
  }
  
  public Direction tickPathFinding(PathFinder.Mode mode) {
    return pf.next(container, mode);
  }
}
