package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.Direction;

/**
 * Event for when chap walks onto the exit tile.
 *
 * @author Ian 300474717
 *
 */
public class MazeEventWalkedKilled extends MazeEventWalked {
  Enemy enemy;
  public MazeEventWalkedKilled(Maze maze, Enemy enemy, PathTile origin, PathTile target, Maze.Direction direction) {
    super(maze, origin, target, direction);
    this.enemy = enemy;
  }

  public Enemy getEnemy() {
    return enemy;
  }

  @Override
  public String toString() {
    return String.format("Walked onto the an enemy at %s,%s and got killed.",
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
