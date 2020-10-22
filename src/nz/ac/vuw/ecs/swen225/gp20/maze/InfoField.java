package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * An entity which shows an information pop up when Chap walks over it.
 *
 * @author Ian 300474717
 */

public class InfoField extends Trigger {
  private String information;

  /**
   * Empty constructor for Persistence.
   */
  public InfoField() {
  }


  /**
   * Construct a new instance.
   * 
   * @param filename The filename of the image to use for this entity.
   * @param information The information to be shown when walked over.
   */
  public InfoField(String filename, String information) {
    super(filename, "IN");
    this.information = information;
  }

  /**
   * Set Information.
   * @param i New Information.
   */
  public void setInformation(String i) {
    this.information = i;
  }

  /**
   * Get Information.
   * @return Current Information.
   */
  public String getInformation() {
    return information;
  }
}
