package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when chap moves from one tile to another.
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventEnemyWalked implements MazeEvent {
  protected PathTile origin, destination;
  protected Maze.Direction direction;
  protected Maze maze;
  protected Enemy enemy;
  
  public MazeEventEnemyWalked(Maze maze, Enemy enemy, PathTile origin, PathTile destination, Maze.Direction direction) {
    this.maze = maze;
    this.origin = origin;
    this.destination = destination;
    this.direction = direction;
    this.enemy = enemy;
  }

  public PathTile getEnemyDestination() {
    return destination;
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
        destination.getCol(), destination.getRow());
  }
  
  @Override
  public void receive(MazeEventListener l) {
    l.update(this);
  }
}
