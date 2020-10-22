package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.util.Stack;

/**
 * A tile which Chap may walk on, and may contain Containables.
 *
 * @author Ian 300474717
 *
 */

public class PathTile extends Tile {
  private Stack<Containable> contains = new Stack<Containable>(); // the items on this FreeTile
  private BlockingContainable blocker;

  /**
   * Empty constructor for Persistence.
   */
  public PathTile() {
  }

  /**
   * Construct a new instance.
   * 
   * @param filename The filename of the image to use for this entity.
   */
  public PathTile(String filename) {
    super(filename, "PA");
    this.walkable = true;
  }

  /**
   * If the enemy can walk on this Tile.
   * Differs to isWalkable as an Enemy could
   * walk on a Tile which contains Chap and kill him.
   *
   * @return if an Enemy could walk on this Tile
   */
  @Override
  public boolean enemyWalkable() {
    if (!walkable) {
      if (blocker instanceof Chap) {
        return true;
      }
    }
    return walkable;
  }

  /**
   * Place a Containable inside this Tile.
   *
   * @param c The Containable
   */
  public void place(Containable c) {
    contains.push(c);
    c.setContainer(this);
    if (c instanceof BlockingContainable) {
      walkable = false;
      blocker = (BlockingContainable) c;
    }
  }

  /**
   * Remove a Containable inside this Tile.
   *
   * @param c The Containable
   */
  public void remove(Containable c) {
    contains.remove(c);
    if ((c instanceof BlockingContainable && this.getContainedEntities().isEmpty())
        || !this.containsBlocker()) {
      walkable = true;
      blocker = null;
    }
    c.setContainer(null);
  }

  /**
   * Check whether this tile contains any BlockingContainables.
   *
   * @return true if it does contain a blocking containable, false if not
   */
  public boolean containsBlocker() {
    for (Containable containable : this.contains) {
      if (containable instanceof BlockingContainable) {
        return true;
      }
    }
    return false;
  }

  /**
   * Move a Containable inside this Tile and update the previous Container.
   *
   * @param c The Containable
   */
  public void moveTo(Containable c) {
    if (c.getContainer() != null) {
      c.getContainer().remove(c);
    }
    place(c);
  }

  @Override
  public String getInitials() {
    // Gets the initials of the top contained object instead
    if (!contains.isEmpty()) {
      return contains.peek().getInitials();
    }
    return super.getInitials();
  }

  /**
   * Get the current BlockingContainable.
   * 
   * @return the current BlockingContainable.
   */
  public BlockingContainable getBlocker() {
    return blocker;
  }

  /**
   * Set a new BlockingContainable.
   * @param blocker the new BlockingContainable.
   */
  public void setBlocker(BlockingContainable blocker) {
    this.blocker = blocker;
  }

  /**
   * Get all currently contained entities.
   * @return  All current contained entities.
   */
  public Stack<Containable> getContainedEntities() {
    return contains;
  }
}
