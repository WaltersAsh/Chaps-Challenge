package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.application.*;
import test.nz.ac.vuw.ecs.swen225.gp20.maze.BoardRig;

/**
 * An example class of how to implement MazeEventListener.
 * Just prints out the event which occurred.
 *
 * @author Ian 300474717
 *
 */
public class MazeEventListenerExample extends MazeEventListener{
  public static void main(String[] args) {
    Gui g = new Gui(BoardRig.crateAndWaterTest());
    g.getBoard().getMaze().addListener(new MazeEventListenerExample());

    g.getFrame().setVisible(true);
  }

  @Override
  public void update(MazeEventInfoField e) {
    System.out.println("walked on an info field");
  }

  @Override
  public void update(MazeEventPickup e) {
    System.out.println("picked something up");
  }

  @Override
  public void update(MazeEventPushed e) {
    System.out.println("pushed something");
  }

  @Override
  public void update(MazeEventUnlocked e) {
    System.out.println("unlocked a door");
  }

  @Override
  public void update(MazeEventWalked e) {
    System.out.println("walked");
  }

  @Override
  public void update(MazeEventWon e) {
    System.out.println("won the game");
  }

  @Override
  public void update(MazeEventPushedWater e) {
    System.out.println("pushed something into water");
  }

  @Override
  public void update(MazeEventEnemyWalked e) {
    System.out.println("enemy walked");
  }

  @Override
  public void update(MazeEventTeleported e) {
    System.out.println("teleported");
  }

  @Override
  public void update(MazeEventWalkedKilled e) {
    System.out.println("Chap walked onto an enemy and got killed");
  }

  @Override
  public void update(MazeEventEnemyWalkedKilled e) {
    System.out.println("Enemy walked onto chap and chap got killed");
  }

  @Override
  public void update(MazeEventWalkedDrowned e) {
    System.out.println("Chap walked into water and drowned");
  }

}
