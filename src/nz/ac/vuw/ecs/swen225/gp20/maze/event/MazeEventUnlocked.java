package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Event for when chap unlocks a door
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventUnlocked extends MazeEventWalked{
	private Door door;
	private Key key;
	public MazeEventUnlocked(Maze m, Tile origin, Tile destination, Maze.Direction direction, Door door, Key key) {
		super(m, destination, destination, direction);
		this.door = door;
		this.key = key;
		
	}
	public Door getDoor() {
		return door;
	}
	public Key getKey() {
		return key;
	}
}
