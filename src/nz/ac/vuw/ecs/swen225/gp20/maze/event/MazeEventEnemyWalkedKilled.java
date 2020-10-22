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

  @Override
  public void receive(MazeEventListener l) {
    l.update(this);
  }

  @Override
  public void invert() {
    throw new UnsupportedOperationException("Not implemented.");
  }
}
