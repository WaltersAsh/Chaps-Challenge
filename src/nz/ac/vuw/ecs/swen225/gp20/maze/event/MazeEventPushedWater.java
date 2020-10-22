package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when chap pushes another Movable (usually a Crate).
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventPushedWater extends MazeEventPushed {
  private Movable pushed;
  private Water water;

  public MazeEventPushedWater(Maze maze, PathTile origin, PathTile target, Maze.Direction direction,
      Movable pushed, Water water) {
    super(maze, origin, target, direction, pushed);
    this.pushed = pushed;
    this.water = water;
  }

  public Movable getPushed() {
    return pushed;
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
