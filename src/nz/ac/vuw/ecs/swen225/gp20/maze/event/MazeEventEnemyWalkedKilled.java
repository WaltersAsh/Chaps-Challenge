package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when chap moves from one tile to another.
 *
 * @author Ian 300474717
 *
 */
public class MazeEventEnemyWalkedKilled implements MazeEvent {
  protected PathTile origin, target;
  protected Maze.Direction direction;
  protected Maze maze;
  protected Enemy enemy;

  public MazeEventEnemyWalkedKilled(Maze maze, Enemy enemy, PathTile origin, PathTile target, Maze.Direction direction) {
    this.maze = maze;
    this.origin = origin;
    this.target = target;
    this.direction = direction;
    this.enemy = enemy;
  }

  public PathTile getEnemyTarget() {
    return target;
  }

  public PathTile getEnemyOrigin() {
    return origin;
  }

  public Maze.Direction getEnemyDirection() {
    return direction;
  }

  public Maze getMaze() {
    return maze;
  }

  public Enemy getEnemy() {
    return enemy;
  }


  @Override
  public String toString() {
    return String.format("Enemy walked from tile %s,%s to tile %s,%s", origin.getCol(), origin.getRow(),
        target.getCol(), target.getRow());
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
