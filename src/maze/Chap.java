package maze;

import java.util.*;

/**
 * The player controlled token
 * 
 * @author Ian 300474717
 *
 */

public class Chap extends Movable{
	private List<Key> keys = new ArrayList<>();
	private List<Treasure> treasures = new ArrayList<>();
	
	public Chap(String filename) {
		super(filename, "CH");
	}
	
	public void pickup(Pickup p) {
		if(p instanceof Treasure) {
			treasures.add((Treasure)p);
		}else if(p instanceof Key) {
			keys.add((Key)p);
		}
	}
	
	public Key hasMatchingKey(Door d) {
		for(Key key: keys) {
			if(key.getColor().equals(d.getColor())) {
				return key;
			}
		}
		return null;
	}

	public List<Treasure> getTreasures() {
		return treasures;
	}

	public List<Key> getKeys() {
		return keys;
	}
}
