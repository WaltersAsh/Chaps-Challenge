package maze;

/**
 * The Door, should only unlock if the Chap has the matching Key
 * 
 * @author Ian 300474717
 *
 */

public class Door extends LockableTile {

	public Door(String filename, String initials) {
		super(filename, initials);
	}

	@Override
	public boolean canUnlock(Chap c) {
		// TODO Auto-generated method stub
		return false;
	}

}
