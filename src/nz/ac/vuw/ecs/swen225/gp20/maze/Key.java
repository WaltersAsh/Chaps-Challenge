package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.rendering.SoundEffect;

/**
 * A Key which may be picked up by Chap and unlocks a matching Door
 * 
 * @author Ian 300474717
 *
 */

public class Key extends Pickup {
	private Maze.KeyColor color;

	public Key(String filename, Maze.KeyColor color, SoundEffect soundEffect) {
		super(filename, "K"+color.toString().charAt(0), soundEffect);
		this.color = color;
	}
	
	public Maze.KeyColor getColor() {
		return color;
	}

	public void setColor(Maze.KeyColor color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
		return color.toString() + " Key";
	}
}
