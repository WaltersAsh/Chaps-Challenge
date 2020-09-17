package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.rendering.SoundEffect;

/**
 * A Treasure which may be picked up by Chap and unlocks the final ExitLock once all have been obtained
 *
 * @author Ian 300474717
 *
 */

public class Treasure extends Pickup {
	public Treasure(String filename, SoundEffect soundEffect) {
		super(filename, "TR", soundEffect);
	}

	@Override
	public String toString() {
		return "Treasure";
	}

	@Override
	public void onWalked(Maze m) {
		super.onWalked(m);
		if(m.getChap().hasAllTreasures(m)) {
			m.openExitLock();
		}
	}
}
