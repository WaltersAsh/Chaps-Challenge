package maze;


import java.awt.Image;
import java.awt.Toolkit;

/**
 * Base class for anything which can be drawn on the board
 * 
 * @author Ian 300474717
 *
 */

public class Drawable {
	private static Toolkit toolkit;
	protected String initials; // the string representation of this Drawable for drawing in text form
	

	public Drawable(String filename, String initials) {
		this.initials = initials;
	}
	
	public String getInitials() {
		return initials;
	}
}
