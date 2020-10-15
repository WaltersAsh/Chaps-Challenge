package nz.ac.vuw.ecs.swen225.gp20.application;

import java.io.File;
import nz.ac.vuw.ecs.swen225.gp20.maze.BoardRig;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;

/**
 * Main class for running Chap's Challenge.
 */
public class Main {

  public static final File main = new File("levels/quickSave.json");
  public static final File level1 = new File("levels/official_levels/level1.json");
  public static final File level2 = new File("levels/official_levels/level2.json");

  /**
   * Key objects and components are invoked here to run the game.
   *
   * @param args the commandline arguments
   */
  public static void main(String[] args) {
    //new TextGUI();
    //Maze maze = BoardRig.crateAndWaterTest();
    Maze maze = Persistence.loadMaze(level1);

    //game has already been played before
    if (main.length() != 0)  {
      maze = Persistence.quickLoad();
    } else {
      maze.setLevelID(1);
    }

    //initialise the gui
    Gui gui = new Gui(maze);
    gui.getFrame().setVisible(true);
  }
}
