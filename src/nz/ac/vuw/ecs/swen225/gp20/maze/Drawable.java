package nz.ac.vuw.ecs.swen225.gp20.maze;


import java.awt.Image;
import java.awt.Toolkit;

/**
 * Base class for anything which can be drawn on the board
 * 
 * @author Ian 300474717
 *
 */

public class Drawable {
	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	protected String initials; // the string representation of this Drawable for drawing in text form

	public Image image;

	public Drawable(String filename, String initials) {

		this.initials = initials;
		image = toolkit.getImage(filename);
	}
	
	public String getInitials() {
		return initials;
	}

	public Image getImage(){return image;}
}
