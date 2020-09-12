package maze;

/**
 * The Door, should only unlock if the Chap has the matching Key
 * 
 * @author Ian 300474717
 *
 */

public class Door extends LockableTile {

	public Door(String filename, int id) {
		super(filename, "D"+id);
	}

	@Override
	public boolean canUnlock(Chap c) {
		// TODO Auto-generated method stub
		return false;
	}

}
