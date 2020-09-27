package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


/**
 * The type Persistence, to save and load levels.
 * @author Fangyi Yan 300519195
 */
public class Persistence {
  static ObjectMapper mapper = new ObjectMapper();

  public static Maze loadMaze(File file) {//TODO
    if (file == null) {
      return null;
    }
    try {
      System.out.println(Files.readString(file.toPath()));
      return mapper.readValue(file, Maze.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

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

}
