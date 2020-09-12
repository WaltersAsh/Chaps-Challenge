package maze;

/**
 * A Treasure which may be picked up by Chap and unlocks the final ExitLock once all have been obtained
 * 
 * @author Ian 300474717
 *
 */

public class Treasure extends Pickup {

	public Treasure(String filename) {
		super(filename, "TR");
	}
}
