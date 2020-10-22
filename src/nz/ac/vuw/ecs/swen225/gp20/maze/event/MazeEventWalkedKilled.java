package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import com.google.common.base.Preconditions;
import nz.ac.vuw.ecs.swen225.gp20.maze.Enemy;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.PathTile;

/**
 * Event for when chap walks onto the exit tile.
 *
 * @author Ian 300474717
 *
 */
public class MazeEventWalkedKilled extends MazeEventWalked {
  private Enemy enemy;
  
  /**
   * Construct a new instance.
   * 
   * @param maze  The maze which this event is tied to.
   * @param enemy The Enemy which killed Chap
   * @param origin The original position of Chap.
   * @param target  The new position of Chap.
   * @param direction The direction moved.
   */
  public MazeEventWalkedKilled(Maze maze, Enemy enemy, PathTile origin,
      PathTile target, Maze.Direction direction) {
    super(maze, origin, target, direction);
    this.enemy = enemy;
    Preconditions.checkArgument(target.getBlocker().equals(enemy),
        "Enemy on target must match enemy who killed");
  }

  /**
   * Get the Enemy which killed Chap.
   * @return The Enemy.
   */
  public Enemy getEnemy() {
    return enemy;
  }

  @Override
  public void receive(MazeEventListener l) {
    l.update(this);
  }
  
  @Override
  public void invert() {
    throw new UnsupportedOperationException("Not implemented."); 
  }
}
