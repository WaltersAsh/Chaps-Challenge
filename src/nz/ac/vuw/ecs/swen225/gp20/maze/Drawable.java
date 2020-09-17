package nz.ac.vuw.ecs.swen225.gp20.maze;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.UUID;

/**
 * Base class for anything which can be drawn on the board
 * 
 * @author Ian 300474717
 *
 */
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class Drawable {
	public String id = UUID.randomUUID().toString();
//	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	protected String initials; // the string representation of this Drawable for drawing in text form

	public Drawable(String id, String initials, String filename) {
		this.id = id;
		this.initials = initials;
		this.filename = filename;
	}

	//	public Image image;
	public String filename;
	public Drawable(String filename, String initials) {

		this.initials = initials;
//		image = toolkit.getImage(filename);
		this.filename = filename;
	}

	public Drawable() {}

	public String getInitials() {
		return initials;
	}

//	public Image getImage(){return image;}

	public String getFilename() {
		return filename;
	}

	public void changeFile(String newFile){filename = newFile;}
}
