package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.rendering.SoundEffect;

/**
 * The Exit tile and goal of the level
 * 
 * @author Ian 300474717
 *
 */

public class Exit extends Pickup {
	
	public Exit(String filename, SoundEffect soundEffect) {
		super(filename, "EX", soundEffect);
	}
	
	@Override
	public void onWalked(Maze m) {
		m.setLevelFinished(true);
		System.out.println("[exit] you win!");
	}
}
