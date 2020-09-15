package nz.ac.vuw.ecs.swen225.gp20.maze;
import java.util.ArrayList;
import java.util.List;

/**
 * The player controlled token
 * 
 * @author Ian 300474717
 *
 */

public class Chap extends Movable{
	private List<Key> keys = new ArrayList<>();
	private List<Treasure> treasures = new ArrayList<>();

	String filename;
	
	public Chap(String filename) {
		super(filename, "CH");
		this.filename = filename;
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

//	@Override
//	public Image getImage() {
//		Image icon = new ImageIcon(filename).getImage();
//		return icon;
//
//	}

	public List<Treasure> getTreasures() {
		return treasures;
	}

	public List<Key> getKeys() {
		return keys;
	}
}
