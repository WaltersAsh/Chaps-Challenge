package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when chap pushes another Movable (usually a Crate).
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventPushed extends MazeEventWalked{
	private Movable pushed;
	public MazeEventPushed(Maze m, Tile origin, Tile destination, Maze.Direction direction, Movable pushed) {
		super(m, origin, destination, direction);
		this.pushed = pushed; 
	}
	
	public Movable getPushed() {
		return pushed;
	}

}
