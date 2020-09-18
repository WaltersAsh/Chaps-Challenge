package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.rendering.SoundEffect;

/**
 * A Crate which may be pushed by Chap
 * 
 * @author Ian 300474717
 *
 */

public class Crate extends Movable{

	public Crate(String filename) {
		super(filename, "XX");
	}
	
	@Override
	public String toString() {
		return "Crate";
	}
}
