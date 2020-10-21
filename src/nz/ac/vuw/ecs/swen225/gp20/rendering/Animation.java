package nz.ac.vuw.ecs.swen225.gp20.rendering;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.Movable;
import nz.ac.vuw.ecs.swen225.gp20.maze.Tile;

public class Animation {

  private Movable move;
  private int fromX;
  private int fromY;
  private int toX;
  private int toY;
  private Maze.Direction dir;

  /**
   * THe constructor for storing info for an animation.
   *
   * @param m the object moving.
   * @param fromX x coordinate of where its from.
   * @param toX x coordinate of where its going
   * @param fromY y coordinate of where its from.
   * @param toY y coordinate of where its going.
   * @param d the direction of the movement.
   */
  public Animation(Movable m, int fromX, int toX, int fromY, int toY, Maze.Direction d) {
    this.move = m;
    this.fromX = fromX;
    this.fromY = fromY;
    this.toX = toX;
    this.toY = toY;

    this.dir = d;
  }

  public Movable getM() {
    return move;
  }

  public Maze.Direction getD() {
    return dir;
  }

  public int getFromX() {
    return fromX;
  }

  public int getFromY() {
    return fromY;
  }

  public int getToX() {
    return toX;
  }

  public int getToY() {
    return toY;
  }

  public void setFromX(double fromX) {
    this.fromX = (int) fromX;
  }

  public void setFromY(double fromY) {
    this.fromY = (int) fromY;
  }

  public void setToX(double toX) {
    this.toX = (int) toX;
  }

  public void setToY(double toY) {
    this.toY = (int) toY;
  }
}
