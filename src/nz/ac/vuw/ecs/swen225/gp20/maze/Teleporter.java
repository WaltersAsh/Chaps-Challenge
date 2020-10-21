package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.KeyColor;

/**
 * Teleports chap when walked on
 *
 * @author Ian 300474717
 *
 */

public class Teleporter extends Trigger {
  private Teleporter other;
  private KeyColor color;

  public Teleporter() {
  }

  public Teleporter(String filename, Maze.KeyColor color) {
    super(filename, "P" + color.toString().charAt(0));
    this.color = color;
  }

  public void setOther(Teleporter o) {
    other = o;
  }

  public Teleporter getOther() {
    return other;
  }

  public KeyColor getColor() {
    return color;
  }

  @JsonIgnore
  public PathTile getDestination() {
    return other.getContainer();
  }

  public void setColor(KeyColor color) {
    this.color = color;
  }

  @Override
  public void onWalked(Maze m) {
    getDestination().moveTo(m.getChap());
  }
}
