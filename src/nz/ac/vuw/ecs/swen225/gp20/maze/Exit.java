package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * The Exit tile and goal of the level
 * 
 * @author Ian 300474717
 *
 */

public class Exit extends Pickup {
	
	public Exit(String filename) {
		super(filename, "EX");
	}

	public Exit() {
	}

	public Exit(String id, String initials, String filename, PathTile container) {
		super(id, initials, filename, container);
	}

	@Override
	public void onWalked(Maze m) {
		m.setLevelFinished(true);
		System.out.println("[exit] you win!");
	}
}
