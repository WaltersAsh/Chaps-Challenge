package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

import java.io.File;
import java.io.IOException;


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
      fixMaze(loadedMaze);
      return loadedMaze;
    } catch (IOException e) {
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
   * @param loadedMaze maze going to fix;
   */
  private static void fixMaze(Maze loadedMaze) {
    loadedMaze.getEnemies().forEach(enemy -> enemy.initPathFinder(loadedMaze));
  }


}
