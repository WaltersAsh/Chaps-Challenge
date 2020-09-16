package nz.ac.vuw.ecs.swen225.gp20.maze;

public abstract class BlockingContainable extends Containable {
	public BlockingContainable(String filename, String initials) {
		super(filename, initials);
	}

	public BlockingContainable(String id, String initials, String filename, PathTile container) {
		super(id, initials, filename, container);
	}

	public BlockingContainable() {
	}
}
