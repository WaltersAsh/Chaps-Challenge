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
  private BlockingContainable blocker;

  public PathTile(String filename) {
    super(filename, "PA");
    this.walkable = true;
  }

  /**
   * Instantiates a new Path tile. For Jackson.
   */
  public PathTile() {
  }

  /**
   * If the enemy can walk on this Tile
   * Different to isWalkable as the enemy can walk on
   * a Tile which contains Chap and kill him
   *
   * @return if the enemy can walk on the Tile
   */
  @Override
  public boolean enemyWalkable() {
    if(!walkable) {
      if(blocker instanceof Chap) {
        return true;
      }
    }
    return walkable;
  }


  /**
   * Place a Containable inside this Tile
   *
   * @param c The Containable
   */
  public void place(Containable c) {
    if(c == null) {
      int breakpoint = 0;
//      c = new Crate();
    }
    contains.push(c);
    c.setContainer(this);
    if (c instanceof BlockingContainable) {
      walkable = false;
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
    if (c instanceof BlockingContainable && this.getContainedEntities().isEmpty()) {
      walkable = true;
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

  public BlockingContainable getBlocker() {
    return blocker;
  }

  public void setBlocker(BlockingContainable blocker) {
    this.blocker = blocker;
  }

  public Stack<Containable> getContainedEntities() {
    return contains;
  }
}
