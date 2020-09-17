package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.rendering.SoundEffect;

/**
 * Shows an information popup when Chap walks over it
 * 
 * @author Ian 300474717
 *
 */

public class InfoField extends Pickup{
	String information;
	public InfoField(String filename, String information) {
		super(filename, "IN");
		this.information=information;
	}

	@Override
	public void onWalked(Maze m) {
		System.out.printf("[info field] %s\n",information);
	}
}
