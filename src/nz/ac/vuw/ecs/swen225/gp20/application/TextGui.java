package nz.ac.vuw.ecs.swen225.gp20.application;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import test.nz.ac.vuw.ecs.swen225.gp20.maze.BoardRig;

/**
 * Testing text form GUI using sysin/out.
 *
 * @author Ian 300474717
 */
public class TextGui {

  private Maze maze;

  /**
   * Instantiates a new Text gui.
   */
  public TextGui() {
    maze = BoardRig.lesson1();
    displayBoard();
    listen();
  }

  /**
   * Display board.
   */
  public void displayBoard() {
    System.out.println(maze);
  }

  /**
   * Listen.
   */
  public void listen() {
    while (!maze.isLevelFinished()) {

      Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);
      switch (sc.next().toLowerCase()) {
        case "w":
          maze.move(Maze.Direction.UP);
          break;
        case "s":
          maze.move(Maze.Direction.DOWN);
          break;
        case "a":
          maze.move(Maze.Direction.LEFT);
          break;
        case "d":
          maze.move(Maze.Direction.RIGHT);
          break;
        case "q":
          return;
        default:
          break;
      }
      displayBoard();
      System.out.println(maze.getChap().getKeys());
      System.out.println(maze.getChap().getTreasures());
    }
  }
}
