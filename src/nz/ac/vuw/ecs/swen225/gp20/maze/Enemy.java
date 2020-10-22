package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.Direction;

/**
 * The pathfinding enemy tokens.
 *
 * @author Ian 300474717
 */

public class Enemy extends Movable {
  private PathFinder pf;
  
  /**
   * Empty constructor for Persistence.
   */
  public Enemy() {
  }

  /**
  * Construct a new instance.
  * @param filename  The filename of the image to use for this entity.
  */
  public Enemy(String filename) {
    super(filename, "EN");
  }


  /**
   * Get PathFinder.
   * @return Current Pathfinder.  
   */
  public PathFinder getPf() {
    return pf;
  }

  /**
   * Set PathFinder.
   * @param pf new PathFinder.
   */
  public void setPf(PathFinder pf) {
    this.pf = pf;
  }
  
  /**
   * Initialise the PathFinder.
   * @param m Maze to use for path finding.
   */
  @JsonIgnore
  public void initPathFinder(Maze m) {
    pf = new PathFinder(m);
  }

  /** 
   * Tick the PathFinder.
   * @return  The next Direction to move in.
   */
  public Direction tickPathFinding() {
    return tickPathFinding(PathFinder.Mode.STRAIGHT_ANTICLOCKWISE);
  }

  /**
   * Tick the PathFinder, specifying a mode.
   * @param mode  The path finding move.
   * @return  The next Direction to move in.
   */
  public Direction tickPathFinding(PathFinder.Mode mode) {
    return pf.next(container, mode);
  }
}
