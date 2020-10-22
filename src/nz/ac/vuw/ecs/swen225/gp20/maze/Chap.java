package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.util.ArrayList;
import java.util.List;

/**
 * The player controlled token.
 *
 * @author Ian 300474717
 *
 */

public class Chap extends Movable {
  /**
   * Empty constructor for Persistence.
   */

  public Chap() {
  }

  private List<Key> keys = new ArrayList<>();
  private List<Treasure> treasures = new ArrayList<>();

  private String left;
  private String right;

  /**
   * Construct a new instance.
   * @param left  Filename for left facing image.
   * @param right Filename for right facing image.
   */
  public Chap(String left, String right) {
    super(left, "CH");
    this.left = left;
    this.right = right;

  }
  
  /**
   * Set Keys.
   * @param keys New Keys.
   */
  public void setKeys(List<Key> keys) {
    this.keys = keys;
  }

  /**
   * Set Treasures.
   * @param treasures New Treasures.
   */
  public void setTreasures(List<Treasure> treasures) {
    this.treasures = treasures;
  }

  /**
   * Get Left image file.
   * @return  Current Left image file.
   */
  public String getLeft() {
    return left;
  }

  /**
   * Set Left image file.
   * @param left  New Left image file.
   */
  public void setLeft(String left) {
    this.left = left;
  }

  /**
   * Get Right image file.
   * @return  Current Right image file.
   */
  public String getRight() {
    return right;
  }

  /**
   * Set Right image file.
   * @param right  New Left image file.
   */
  public void setRight(String right) {
    this.right = right;
  }

  /**
   * Pick up a Pickup and add it to the appropriate inventory.
   * @param p The Pickup
   */
  public void pickup(Pickup p) {
    if (p instanceof Treasure) {
      treasures.add((Treasure) p);
    } else if (p instanceof Key) {
      keys.add((Key) p);
    }
    System.out.println(p.getContainer());
    if (p.getContainer() != null) {
      p.getContainer().remove(p);
    }
      
  }

  /**
   * Un-pickup a Pickup.
   * Note that this does not replace the Pickup onto its Tile
   * as it no longer knows what Tile it was on
   * @param m Current Maze
   * @param p The Pickup to remove
   */
  public void unPickup(Maze m, Pickup p) {
    if (p instanceof Treasure) {
      treasures.remove((Treasure) p);
      m.getTreasures().add((Treasure) p);
    } else if (p instanceof Key) {
      keys.remove((Key) p);
    }
  }

  /**
   * Check if we have a matching Key for this Door.
   * @param d Door to check.
   * @return  If we have the matching Key.
   */
  public Key hasMatchingKey(Door d) {
    for (Key key : keys) {
      if (key.getColor().equals(d.getColor())) {
        return key;
      }
    }
    return null;
  }

  /**
   * Get Treasures.
   * @return  Current Treasures.
   */
  public List<Treasure> getTreasures() {
    return treasures;
  }

  /**
   * Get Keys.
   * @return  Current Keys.
   */
  public List<Key> getKeys() {
    return keys;
  }
}
