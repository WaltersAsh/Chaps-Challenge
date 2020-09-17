package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.rendering.SoundEffect;

/**
 * Base class for objects which may be inside FreeTiles
 * 
 * @author Ian 300474717
 *
 */

public abstract class Containable extends Drawable{

	public Containable(String filename, String initials) {
		super(filename, initials);

	}

	PathTile container;

	public PathTile getContainer() {
		return container;
	}

	public void setContainer(PathTile container) {
		this.container = container;
	}

}
