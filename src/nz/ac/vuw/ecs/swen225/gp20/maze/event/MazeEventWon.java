package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.Direction;

/**
 * Event for when chap walks onto the exit tile.
 * 
 * @author Ian 300474717
 *
 */
public class MazeEventWon extends MazeEventWalked{
	public MazeEventWon(Maze m, Tile origin, Tile destination, Direction direction) {
		super(m, origin, destination, direction);
	}
}
