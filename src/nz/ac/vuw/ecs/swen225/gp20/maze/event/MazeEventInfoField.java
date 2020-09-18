package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when chap walks onto an InfoField
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventInfoField extends MazeEventWalked{
	private InfoField info;
	public MazeEventInfoField(Maze m, Tile origin, Tile destination, Maze.Direction direction, InfoField info) {
		super(m, destination, destination, direction);
		this.info = info;  
	}
	
	public InfoField getInfoField() {
		return info;
	}

}
