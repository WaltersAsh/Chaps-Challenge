package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

abstract public class MazeEvent {
	private Maze maze;
	public MazeEvent(Maze m) {
		maze = m;
	}
	
	public Maze getMaze() {
		return maze;
	}
}
