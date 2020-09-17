package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.rendering.SoundEffect;

/**
 * The pathfinding enemy tokens
 *
 * @author Ian 300474717
 *
 */

public class Enemy extends Movable{
	PathFinder pf;

	public Enemy(String filename, SoundEffect soundEffect) {
		super(filename, "EN", soundEffect);
	}

	public void initPathFinder(Maze m) {
		pf = new PathFinder(m);
	}


	public Tile tickPathFinding() {
		return pf.next(container);
	}
}
