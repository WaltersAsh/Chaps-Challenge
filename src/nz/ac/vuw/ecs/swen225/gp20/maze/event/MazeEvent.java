package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Base class for all broadcastable events which happen in Maze.
 * 
 * @author Ian 300474717
 *
 */
abstract public class MazeEvent {
	private Maze maze;
	public MazeEvent(Maze m) {
		maze = m;
	}
	
	public Maze getMaze() {
		return maze;
	}
}
