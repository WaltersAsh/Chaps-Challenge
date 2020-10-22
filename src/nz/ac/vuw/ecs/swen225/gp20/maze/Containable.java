package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Base class for entities which may be inside PathTiles.
 *
 * @author Ian 300474717
 */

public abstract class Containable extends Drawable {

  /**
   * Empty constructor for Persistence.
   */
  public Containable() {
  }
  
  /**
   * Construct a new instance.
   * 
   * @param filename The filename of the image to use for this entity.
   * @param initials The initials to represent this entity.
   */
  public Containable(String filename, String initials) {
    super(filename, initials);

  }

  protected PathTile container;

  /**
   * Get the Container.
   * @return  Current Container.
   */
  public PathTile getContainer() {
    return container;
  }

  /**
   * Set the Container.
   * @param container New container.
   */
  public void setContainer(PathTile container) {
    this.container = container;
  }

  /**
   * Execute actions when Chap walks over this Containable.
   * @param m The maze which is contains this Containable.
   */
  public void onWalked(Maze m) {

  }
}
