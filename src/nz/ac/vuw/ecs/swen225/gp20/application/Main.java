package nz.ac.vuw.ecs.swen225.gp20.application;

import nz.ac.vuw.ecs.swen225.gp20.maze.BoardRig;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.*;

/**
 * Main class for running Chap's Challenge.
 */
public class Main {
  private static final Maze[] levels = new Maze[] {
          BoardRig.lesson1(),
          BoardRig.crateTest(),
          BoardRig.crateAndWaterTest(),
          BoardRig.pathFindTest1(),
          BoardRig.levelEditorTest1(),
          BoardRig.levelEditorTest2(),
          BoardRig.levelEditorTest3(),
          BoardRig.teleporterTest1(),
  };

  /**
   * Key objects and components are invoked here to run the game.
   *
   * @param args the commandline arguments
   */
  public static void main(String[] args) {
    //new TextGUI();
    //TODO: Boardview object from the renderer package should be here (instead of in gui)
    // when dependency is fixed
    MazeLoader mazeLoader = new MazeLoader();

    Maze maze = levels[7];
    Maze persistenceTestMaze = MazeToJsonTest.testLoader();
    Gui gui = new Gui(BoardRig.lesson1());
//    Gui gui = new Gui(persistenceTestMaze);
    gui.getFrame().setVisible(true);
  }
}
