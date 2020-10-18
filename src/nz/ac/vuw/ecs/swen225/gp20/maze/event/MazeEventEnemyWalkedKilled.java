package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when an enemy moves from one tile to another.
 *
 * @author Ian 300474717
 *
 */
public class MazeEventEnemyWalkedKilled extends MazeEventEnemyWalked {
  public MazeEventEnemyWalkedKilled(Maze maze, Enemy enemy, PathTile origin, PathTile target, Maze.Direction direction) {
    super(maze, enemy, origin, target, direction);
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
    return String.format("Enemy walked from tile %s,%s to tile %s,%s and killed Chap", origin.getCol(), origin.getRow(),
        target.getCol(), target.getRow());
  }

  @Override
  public void receive(MazeEventListener l) {
    l.update(this);
  }

  @Override
  public void invert() {
    origin.moveTo(enemy);
    //throw new UnsupportedOperationException("Not implemented.");
  }
}
