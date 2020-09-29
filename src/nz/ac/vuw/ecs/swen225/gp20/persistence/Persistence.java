package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.vuw.ecs.swen225.gp20.maze.BoardRig;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * The type Persistence, to save and load levels.
 *
 * @author Fangyi Yan 300519195
 */
public class Persistence {
  static ObjectMapper mapper = new ObjectMapper();

  /**
   * Load maze.
   *
   * @param file the file object
   * @return the maze if loading is successful, null if failed.
   */
  public static Maze loadMaze(File file) {
    if (file == null) {
      return null;
    }
    try {
      Maze loadedMaze = mapper.readValue(file, Maze.class);
      if (mazeValidator(loadedMaze)) {
        fixMaze(loadedMaze);
        return loadedMaze;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Save maze.
   *
   * @param maze the maze object
   * @param file the file object
   * @return the boolean indicate saving is successful or not
   */
  public static boolean saveMaze(Maze maze, File file) {//TODO
    if (file == null) {
      return false;
    }
//    System.out.println(maze.getListeners());
//    System.out.println(maze);
    try {
      mapper.writeValue(file, maze);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Add things things are discarded during serialization.
   *
   * @param loadedMaze maze going to fix;
   */
  private static void fixMaze(Maze loadedMaze) {
    loadedMaze.getEnemies().forEach(enemy -> enemy.initPathFinder(loadedMaze));
  }

  private static boolean mazeValidator(Maze loadedMaze) throws Exception {
    if (loadedMaze == null) {
      return false;
    } else if (loadedMaze.getHeight() < 1 || loadedMaze.getWidth() < 1) {
      throw new Exception("Illegal Maze size, excepted both >0 but height = " + loadedMaze.getHeight() + " width = " + loadedMaze.getWidth());
    }

    return true;
  }

  public static boolean quickSave(Maze maze) {
    Path p = Paths.get(".", "levels", "quickSave.json");
    //check file exist

    try {
      p.toFile().createNewFile();
      System.out.println(Files.readAllLines(p));
      mapper.writeValue(p.toFile(), maze);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * The tester for quickSave.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    System.out.println(quickSave(BoardRig.lesson1()));
  }

}
