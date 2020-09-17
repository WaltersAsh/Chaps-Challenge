package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.util.Random;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.Direction;

public class PathFinder {
	Maze maze;
	Random random = new Random();
	public PathFinder(Maze m) {
		maze=m;
	}

	public Tile next(Tile current) {
		Direction d = Direction.values()[random.nextInt(Direction.values().length)];
		return maze.tileTo(current, d);
	}
}
