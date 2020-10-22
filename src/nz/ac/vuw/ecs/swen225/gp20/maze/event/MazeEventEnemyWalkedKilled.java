package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.Enemy;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.PathTile;

/**
 * Event for when an Enemy kills Chap by moving.
 *
 * @author Ian 300474717
 *
 */
public class MazeEventEnemyWalkedKilled extends MazeEventEnemyWalked {
  /**
   * Construct a new instance.
   * 
   * @param maze  The maze which this event is tied to.
   * @param enemy The enemy which walked.
   * @param origin The original position of the Enemy.
   * @param target  The new position of the Enemy.
   * @param direction The direction moved.
   */
  public MazeEventEnemyWalkedKilled(Maze maze, Enemy enemy, PathTile origin, PathTile target,
      Maze.Direction direction) {
    super(maze, enemy, origin, target, direction);
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
