package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.util.*;

/**
 * A tile which Chap may walk on, and may contain Containables
 * 
 * @author Ian 300474717
 *
 */

public class PathTile extends Tile {
  private Stack<Containable> contains = new Stack<Containable>(); // the items on this FreeTile
  private boolean isBlocked = false;
  private BlockingContainable blocker;

  public PathTile(String filename) {
    super(filename, "PA");
    this.walkable = true;

  }

  /**
   * Place a Containable inside this Tile
   * 
   * @param c The Containable
   */
  public void place(Containable c) {
    contains.push(c);
    c.setContainer(this);
    if (c instanceof BlockingContainable) {
      isBlocked = true;
      blocker = (BlockingContainable) c;
    }
  }

  /**
   * Remove a Containable inside this Tile
   * 
   * @param c The Containable
   */
  public void remove(Containable c) {
    contains.remove(c);
    if (c instanceof BlockingContainable) {
      isBlocked = false;
      blocker = null;
    }

    // might have to check all other objects in contains, but
    // theoretically if it correctly blocks then we never
    // have 2 BlockingContainable at once
  }

  /**
   * Move a Containable inside this Tile and update the previous Container
   * 
   * @param c The Containable
   */
  public void moveTo(Containable c) {
    c.getContainer().remove(c);
    place(c);
  }

  /**
   * Get the initials of the top contained object instead
   */
  @Override
  public String getInitials() {
    if (!contains.isEmpty())
      return contains.peek().getInitials();
    return super.getInitials();
  }

  public boolean isBlocked() {
    return isBlocked;
  }

  public BlockingContainable getBlocker() {
    return blocker;
  }

  public Stack<Containable> getContainedEntities() {
    return contains;
  }
}
