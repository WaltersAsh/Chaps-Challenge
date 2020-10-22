package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.Movable;
import nz.ac.vuw.ecs.swen225.gp20.maze.PathTile;
import nz.ac.vuw.ecs.swen225.gp20.maze.Water;

/**
 * Event for when chap pushes another Movable (usually a Crate).
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventPushedWater extends MazeEventPushed {
  private Movable pushed;
  private Water water;

  /**
   * Construct a new instance.
   * 
   * @param maze  The maze which this event is tied to.
   * @param origin The original position of Chap.
   * @param target  The new position of Chap.
   * @param direction The direction moved.
   * @param pushed The Movable pushed.
   * @param water The Water that the Movable was pushed onto.
   */
  public MazeEventPushedWater(Maze maze, PathTile origin, PathTile target, 
      Maze.Direction direction, Movable pushed, Water water) {
    super(maze, origin, target, direction, pushed);
    this.pushed = pushed;
    this.water = water;
  }

  /**
   * Get the Water.
   * @return the Water.
   */
  public Water getWater() {
    return water;
  }
  
  @Override
  public void receive(MazeEventListener l) {
    l.update(this);
  }
  
  @Override
  public void invert() {
    //PathTile watertile = pushed.getContainer();
    PathTile waterTile = (PathTile) maze.tileTo(target, direction);
    target.moveTo(pushed);
    System.out.println(water);
    waterTile.place(water);
    super.invert();
  }
}
