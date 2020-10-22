package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import com.google.common.base.Preconditions;
import nz.ac.vuw.ecs.swen225.gp20.maze.Enemy;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.PathTile;


/**
 * Event for when an enemy moves from one tile to another.
 *
 * @author Ian 300474717
 *
 */
public class MazeEventEnemyWalked implements MazeEvent {
  protected PathTile origin;
  protected PathTile target;
  protected Maze.Direction direction;
  protected Maze maze;
  protected Enemy enemy;

  /**
   * Construct a new instance.
   * 
   * @param maze  The maze which this event is tied to.
   * @param enemy The enemy which walked.
   * @param origin The original position of the Enemy.
   * @param target  The new position of the Enemy.
   * @param direction The direction moved.
   */
  public MazeEventEnemyWalked(Maze maze, Enemy enemy, PathTile origin, PathTile target,
      Maze.Direction direction) {
    Preconditions.checkArgument(target.isWalkable() 
        || maze.getChap().equals(target.getBlocker()), "Target must be walkable");
    this.maze = maze;
    this.origin = origin;
    this.target = target;
    this.direction = direction;
    this.enemy = enemy;
  }
  
  /**
   * Get the targeted PathTile.
   * @return the targeted PathTile.
   */
  public PathTile getEnemyTarget() {
    return target;
  }

  /**
   * Get original PathTile.
   * @return  Original PathTile.
   */
  public PathTile getEnemyOrigin() {
    return origin;
  }

  /**
   * Get direction moved.
   * @return Current direction moved.
   */
  public Maze.Direction getEnemyDirection() {
    return direction;
  }

  /**
   * Get maze.
   * @return  Current maze.
   */
  public Maze getMaze() {
    return maze;
  }

  /**
   * Get enemy.
   * @return Current enemy.
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
    origin.moveTo(enemy);
  }
}
