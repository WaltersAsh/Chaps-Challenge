package maze;

/**
 * The ExitLock, should only unlock if the Chap has collected all Treasures
 * 
 * @author Ian 300474717
 *
 */

public class ExitLock extends LockableTile {

	public ExitLock(String filename, String initials) {
		super(filename, initials);
	}

	@Override
	public boolean canUnlock(Chap c) {
		// TODO Auto-generated method stub
		return false;
	}

}
