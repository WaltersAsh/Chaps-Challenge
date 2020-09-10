package maze;

/**
 * Base class for tiles which can change from walkable to non walkable state
 * 
 * @author Ian 300474717
 *
 */

abstract public class LockableTile extends Tile{
	public LockableTile(String filename, String initials) {
		super(filename, initials);
	}
	
	public abstract boolean canUnlock(Chap c);	
}
