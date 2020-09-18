package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

public class MazeEventWalked extends MazeEvent{
	private Tile origin, destination;
	private Maze.Direction direction;
	
	public MazeEventWalked(Maze m, Tile origin, Tile destination, Maze.Direction direction) {
		super(m);
		this.origin = origin;
		this.destination = destination;
		this.direction = direction;
	}

	public Tile getDestination() {
		return destination;
	}

	public Tile getOrigin() {
		return origin;
	}

	public Maze.Direction getDirection() {
		return direction;
	}
}
