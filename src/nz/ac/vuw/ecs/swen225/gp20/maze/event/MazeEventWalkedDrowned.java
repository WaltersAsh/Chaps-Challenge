package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.Direction;

/**
 * Event for when chap walks onto the exit tile.
 *
 * @author Ian 300474717
 *
 */
public class MazeEventWalkedDrowned extends MazeEventWalked {
  Water water;

  public MazeEventWalkedDrowned(Maze maze, Water water, PathTile origin, PathTile target, Maze.Direction direction) {
    super(maze, origin, target, direction);
    this.water = water;
  }

  public Water getWater() {
    return water;
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
}