package maze;

import java.util.*;

/**
 * The player controlled token
 * 
 * @author Ian 300474717
 *
 */

public class Chap extends Movable{
	List<Pickup> inventory = new ArrayList<>();
	
	public Chap(String filename) {
		super(filename, "CH");
	}
}
