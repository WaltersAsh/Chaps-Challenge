package nz.ac.vuw.ecs.swen225.gp20.rendering;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.Movable;
import nz.ac.vuw.ecs.swen225.gp20.maze.Tile;

public class Animation {

  private Movable m;
  private int fromX, fromY, toX, toY;
  private Maze.Direction d;

  public Animation(Movable m, int fromX, int toX, int fromY, int toY, Maze.Direction d){
    this.m = m;
    this.fromX = fromX;
    this.fromY = fromY;
    this.toX = toX;
    this.toY = toY;

    this.d = d;
  }

  public Movable getM() {
    return m;
  }

  public Maze.Direction getD() {
    return d;
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
