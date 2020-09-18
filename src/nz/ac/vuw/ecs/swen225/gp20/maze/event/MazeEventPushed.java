package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

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
