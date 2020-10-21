package test.nz.ac.vuw.ecs.swen225.gp20.maze;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
  //================================================
  // Tests for Maze Class
  // ================================================
  
  // Test if we can initialise the lesson 1 maze
  @Test
  public void maze_test_01() {
    BoardRig.lesson1();
  }

  // Test if we can initialise with enemies
  @Test
  public void maze_test_02() {
    BoardRig.enemyKillTest1();
  }

  // Test if we can move on the maze
  @Test
  public void maze_test_03() {
    printDivider();
    applyMove(BoardRig.lesson1(), Maze.Direction.UP);
  }

  // Test if we are able to pick up a treasure
  @Test
  public void maze_test_04() {
    printDivider();
    Maze m = BoardRig.lesson1();
    applyMove(m, Maze.Direction.RIGHT);
    applyMoveAndListenFor(m, Maze.Direction.RIGHT, MazeEventPickup.class, true);
    assertEquals(m.getChap().getTreasures().size(), 1); // should have 1 treasure now
  }

  // Test if we are able to pick up a treasure
  @Test
  public void maze_test_05() {
    printDivider();
    Maze m = BoardRig.lesson1();
    System.out.println(m);
    applyMove(m, Maze.Direction.RIGHT);
    applyMoveAndListenFor(m, Maze.Direction.RIGHT, MazeEventPickup.class, true);
    assertEquals(m.getChap().getTreasures().size(), 1); // should have 1 treasure now
  }

  // Test if we are able to open a door
  @Test
  public void maze_test_06() {
    printDivider();
    Maze m = BoardRig.lesson1();
    System.out.println(m);
    applyMove(m, Maze.Direction.LEFT);
    applyMove(m, Maze.Direction.LEFT);
    applyMoveAndListenFor(m, Maze.Direction.UP, MazeEventPickup.class, true); // pick up a key
    applyMove(m, Maze.Direction.UP);
    applyMoveAndListenFor(m, Maze.Direction.LEFT, MazeEventUnlocked.class, true); // open door
  }

  // Test walls
  @Test
  public void maze_test_07() {
    printDivider();
    Maze m = BoardRig.lesson1();
    System.out.println(m);
    applyMove(m, Maze.Direction.LEFT);
    applyMove(m, Maze.Direction.LEFT);
    applyMove(m, Maze.Direction.LEFT);
  }

  // Make sure we can't step onto exitlock before it's open
  @Test
  public void maze_test_08() {
    printDivider();
    Maze m = BoardRig.lesson1();
    System.out.println(m);
    applyMove(m, Maze.Direction.UP);
    applyMove(m, Maze.Direction.UP);
    applyMoveEnsureNotMoved(m, Maze.Direction.UP);
  }

  // Test pushing of crates
  @Test
  public void maze_test_09() {
    printDivider();
    Maze m = BoardRig.crateAndWaterTest();
    System.out.println(m);
    applyMove(m, Maze.Direction.RIGHT);
    applyMove(m, Maze.Direction.DOWN);
    applyMoveAndListenFor(m, Maze.Direction.RIGHT, MazeEventPushed.class, true);
    applyMoveAndListenFor(m, Maze.Direction.DOWN, MazeEventPushedWater.class, true);
  }

  // Test drowning
  @Test
  public void maze_test_10() {
    printDivider();
    Maze m = BoardRig.crateAndWaterTest();
    System.out.println(m);
    applyMove(m, Maze.Direction.DOWN);
    applyMove(m, Maze.Direction.DOWN);
    applyMoveAndListenFor(m, Maze.Direction.DOWN, MazeEventWalkedDrowned.class, true);
  }

  // Test walking into an enemy and dying
  @Test
  public void maze_test_11() {
    printDivider();
    Maze m = BoardRig.enemyKillTest1();
    m.setDoPathfinding(false);
    System.out.println(m);
    applyMove(m, Maze.Direction.LEFT);
    applyMoveAndListenFor(m, Maze.Direction.LEFT, MazeEventWalkedKilled.class, false);
  }

  // Test exit lock opening and winning
  @Test
  public void maze_test_12() {
    printDivider();
    Maze m = BoardRig.exitLockTest1();
    System.out.println(m);
    applyMoveAndListenFor(m, Maze.Direction.RIGHT, MazeEventExitUnlocked.class, true);
    applyMove(m, Maze.Direction.DOWN);
    applyMove(m, Maze.Direction.DOWN);
    applyMove(m, Maze.Direction.DOWN);
    applyMoveAndListenFor(m, Maze.Direction.LEFT, MazeEventWon.class, true);
  }

  // Test teleporters
  @Test
  public void maze_test_13() {
    printDivider();
    Maze m = BoardRig.teleporterTest1();
    System.out.println(m);
    applyMove(m, Maze.Direction.UP);
    applyMoveAndListenFor(m, Maze.Direction.LEFT, MazeEventTeleported.class, true);
  }

  // Test pushing of crates where they can't be pushed
  @Test
  public void maze_test_14() {
    printDivider();
    Maze m = BoardRig.crateAndWaterTest();
    System.out.println(m);
    applyMove(m, Maze.Direction.RIGHT);
    applyMove(m, Maze.Direction.RIGHT);
    applyMoveEnsureNotMoved(m, Maze.Direction.DOWN);
  }

  // Test pushing of crates into walls
  @Test
  public void maze_test_15() {
    printDivider();
    Maze m = BoardRig.crateAndWaterTest();
    System.out.println(m);
    applyMove(m, Maze.Direction.RIGHT);
    applyMove(m, Maze.Direction.DOWN);
    applyMove(m, Maze.Direction.RIGHT);
    applyMove(m, Maze.Direction.RIGHT);
    applyMoveEnsureNotMoved(m, Maze.Direction.RIGHT);
  }
 
  // Test diamond pick not being used up
  @Test
  public void maze_test_16() {
    printDivider();
    Maze m = BoardRig.diamondPickTest1();
    System.out.println(m);
    applyMoveAndListenFor(m, Maze.Direction.DOWN, MazeEventPickup.class, true);
    applyMoveAndListenFor(m, Maze.Direction.DOWN, MazeEventUnlocked.class, true);
    applyMoveAndListenFor(m, Maze.Direction.DOWN, MazeEventUnlocked.class, true);
  }

  // Test enemy killing chap
  @Test
  public void maze_test_17() {
    printDivider();
    Maze m = BoardRig.enemyKillTest1();
    m.setDoPathfinding(false);
    System.out.println(m);
    tickPathFinding(m);
    tickPathFinding(m);
    tickPathFinding(m);
    tickPathFindingAndListenFor(m, MazeEventEnemyWalkedKilled.class);
  }
  
  

  // Utility methods

  public static void printDivider() {
    System.out.println("###################################################");
  }
  
  public static void applyMove(Maze m, Maze.Direction d) {
    m.move(d);
    System.out.println(m);
  }
  

  /**
   * Apply a move and ensure that we did actually move.
   * 
   * @param m the maze to move on
   * @param d the direction to move in
   */
  public static void applyMoveEnsureMoved(Maze m, Maze.Direction d) {
    PathTile original = m.getChap().getContainer();
    applyMove(m, d);
    assertNotEquals(original, m.getChap().getContainer()); // ensure we didn't move
  }

  /**
   * Apply a move and ensure that we didn't actually move.
   * 
   * @param m the maze to move on
   * @param d the direction to move in
   */
  public static void applyMoveEnsureNotMoved(Maze m, Maze.Direction d) {
    PathTile original = m.getChap().getContainer();
    applyMove(m, d);
    assertEquals(original, m.getChap().getContainer()); // ensure we didn't move
  }


  /**
   * Utility method to test if a certain move will give us a certain MazeEvent.
   * 
   * @param m     the maze to move on
   * @param d     the direction to move in
   * @param event the event we expect
   */
  public static void applyMoveAndListenFor(Maze m, Maze.Direction d, Class<? extends MazeEvent> event, boolean ensure) {
    // Add a listener which assertEquals on our incoming and expected event
    m.addListener(new MazeEventListener() {
      @Override
      public void update(MazeEvent e) {
        assertEquals(e.getClass(), event);
      }
    });
    
    if(ensure) {
      applyMoveEnsureMoved(m, d);
    }else {
      applyMoveEnsureNotMoved(m, d);
    }
    // Clear the listeners so we could keep using this maze without getting further
    // errors
    m.clearListeners();
  }
  
  public void tickPathFinding(Maze m) {
    m.tickPathFinding();
    System.out.println(m);
  }
  
  /**
   * Utility method to test if a certain pathfind will give us a certain MazeEvent.
   * 
   * @param m     the maze to tick pathfinding
   * @param event the event we expect
   */
  public static void tickPathFindingAndListenFor(Maze m, Class<? extends MazeEvent> event) {
    // Add a listener which assertEquals on our incoming and expected event
    m.addListener(new MazeEventListener() {
      @Override
      public void update(MazeEvent e) {
        assertEquals(e.getClass(), event);
      }
    });
    m.tickPathFinding();
    // Clear the listeners so we could keep using this maze without getting further
    // errors
    m.clearListeners();
  }
}
