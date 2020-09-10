package maze;

/**
 * Base class for objects which may be inside FreeTiles
 * 
 * @author Ian 300474717
 *
 */

public class Containable extends Drawable{
	public Containable(String filename, String initials) {
		super(filename, initials);
	}

	FreeTile container;

	public FreeTile getContainer() {
		return container;
	}

	public void setContainer(FreeTile container) {
		this.container = container;
	}
	
}
