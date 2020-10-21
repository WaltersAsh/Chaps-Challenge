package test.nz.ac.vuw.ecs.swen225.gp20.maze;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.*;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Maze package. BoardRig class is used for setting up board for
 * testing but not explicitly covered by these tests, as it is not used in the
 * actual game.
 * 
 * @author Ian 300474717
 *
 */
public class MazeTests {

  // Test if we can initialise the lesson 1 maze
  @Test
  public void test_01() {
    BoardRig.lesson1();
  }

  // Test if we can initialise with enemies
  @Test
  public void test_02() {
    BoardRig.enemyKillTest1();
  }

  // Test if we can move on the maze
  @Test
  public void test_03() {
    printDivider();
    applyMove(BoardRig.lesson1(), Maze.Direction.UP);
  }

  // Test if we are able to pick up a treasure
  @Test
  public void test_04() {
    printDivider();
    Maze m = BoardRig.lesson1();
    applyMove(m, Maze.Direction.RIGHT);
    applyMoveAndListenFor(m, Maze.Direction.RIGHT, MazeEventPickup.class);
    assertEquals(m.getChap().getTreasures().size(), 1); // should have 1 treasure now
  }

  // Test if we are able to pick up a treasure
  @Test
  public void test_05() {
    printDivider();
    Maze m = BoardRig.lesson1();
    System.out.println(m);
    applyMove(m, Maze.Direction.RIGHT);
    applyMoveAndListenFor(m, Maze.Direction.RIGHT, MazeEventPickup.class);
    assertEquals(m.getChap().getTreasures().size(), 1); // should have 1 treasure now
  }

  // Test if we are able to open a door
  @Test
  public void test_06() {
    printDivider();
    Maze m = BoardRig.lesson1();
    System.out.println(m);
    applyMove(m, Maze.Direction.LEFT);
    applyMove(m, Maze.Direction.LEFT);
    applyMoveAndListenFor(m, Maze.Direction.UP, MazeEventPickup.class); // pick up a key
    applyMove(m, Maze.Direction.UP);
    applyMoveAndListenFor(m, Maze.Direction.LEFT, MazeEventUnlocked.class); // open door
  }

  // Test walls
  @Test
  public void test_07() {
    printDivider();
    Maze m = BoardRig.lesson1();
    System.out.println(m);
    applyMove(m, Maze.Direction.LEFT);
    applyMove(m, Maze.Direction.LEFT);
    applyMove(m, Maze.Direction.LEFT);
  }

  // Make sure we can't step onto exitlock before it's open
  @Test
  public void test_08() {
    printDivider();
    Maze m = BoardRig.lesson1();
    System.out.println(m);
    applyMove(m, Maze.Direction.UP);
    applyMove(m, Maze.Direction.UP);
    applyMoveEnsureNotMoved(m, Maze.Direction.UP);
  }

  // Test pushing of crates
  @Test
  public void test_09() {
    printDivider();
    Maze m = BoardRig.crateAndWaterTest();
    System.out.println(m);
    applyMove(m, Maze.Direction.RIGHT);
    applyMove(m, Maze.Direction.DOWN);
    applyMoveAndListenFor(m, Maze.Direction.RIGHT, MazeEventPushed.class);
    applyMoveAndListenFor(m, Maze.Direction.DOWN, MazeEventPushedWater.class);
  }

  // Test drowning
  @Test
  public void test_10() {
    printDivider();
    Maze m = BoardRig.crateAndWaterTest();
    System.out.println(m);
    applyMove(m, Maze.Direction.DOWN);
    applyMove(m, Maze.Direction.DOWN);
    applyMoveAndListenFor(m, Maze.Direction.DOWN, MazeEventWalkedDrowned.class);
  }

  //Test walking into an enemy and dying
  @Test
  public void test_11() {
    printDivider();
    Maze m = BoardRig.enemyKillTest1();
    m.setDoPathfinding(false);
    System.out.println(m);
    applyMove(m, Maze.Direction.LEFT);
    applyMoveAndListenFor(m, Maze.Direction.LEFT, MazeEventWalkedKilled.class);
  }
  
  //Test exit lock opening and winning
  @Test
  public void test_12() {
    printDivider();
    Maze m = BoardRig.exitLockTest1();
    System.out.println(m);
    applyMoveAndListenFor(m, Maze.Direction.RIGHT, MazeEventExitUnlocked.class);
    applyMove(m, Maze.Direction.DOWN);
    applyMove(m, Maze.Direction.DOWN);
    applyMove(m, Maze.Direction.DOWN);
    applyMoveAndListenFor(m, Maze.Direction.LEFT, MazeEventWon.class);
  }

  // Utility methods

  static void applyMove(Maze m, Maze.Direction d) {
    m.move(d);
    System.out.println(m);
  }

  static void printDivider() {
    System.out.println("###################################################");
  }

  /**
   * Utility method to test if a certain move will give us a certain MazeEvent.
   * 
   * @param m     the maze to move on
   * @param d     the direction to move in
   * @param event the event we expect
   */
  static void applyMoveAndListenFor(Maze m, Maze.Direction d, Class<? extends MazeEvent> event) {
    // Add a listener which assertEquals on our incoming and expected event
    m.addListener(new MazeEventListener() {
      @Override
      public void update(MazeEvent e) {
        assertEquals(e.getClass(), event);
      }
    });
    applyMove(m, d);
    // Clear the listeners so we could keep using this maze without getting further
    // errors
    m.clearListeners();
  }

  /**
   * Apply a move and ensure that we didn't actually move.
   * 
   * @param m the maze to move on
   * @param d the direction to move in
   */
  static void applyMoveEnsureNotMoved(Maze m, Maze.Direction d) {
    PathTile original = m.getChap().getContainer();
    applyMove(m, d);
    assertEquals(original, m.getChap().getContainer()); // ensure we didn't move
  }
}
