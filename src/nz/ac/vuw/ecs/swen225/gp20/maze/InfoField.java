package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Shows an information popup when Chap walks over it
 * 
 * @author Ian 300474717
 *
 */

public class InfoField extends Pickup{
	String information;
	public InfoField(String filename, String information) {
		super(filename, "IF");
		this.information=information;
	}

	public InfoField() {
	}

	@Override
	public void onWalked(Maze m) {
		System.out.printf("[info field] %s\n",information);
	}
}
