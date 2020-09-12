package maze;

/**
 * A Key which may be picked up by Chap and unlocks a matching Door
 * 
 * @author Ian 300474717
 *
 */

public class Key extends Pickup {

	public Key(String filename, int id) {
		super(filename, "K"+id);
	}

	@Override
	public void onWalked(Chap c) {
		// TODO Auto-generated method stub
		
	}

}
