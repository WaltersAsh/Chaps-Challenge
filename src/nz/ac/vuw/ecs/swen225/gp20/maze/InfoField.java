package nz.ac.vuw.ecs.swen225.gp20.maze;

/**
 * Shows an information popup when Chap walks over it
 *
 * @author Ian 300474717
 */

public class InfoField extends Trigger {
  private String information;

  /**
   * Instantiates a new empty Info field.
   */
  public InfoField() {
  }

  public InfoField(String filename, String information) {
    super(filename, "IN");
    this.information = information;
  }

  public String getInformation() {
    return information;
  }
}
