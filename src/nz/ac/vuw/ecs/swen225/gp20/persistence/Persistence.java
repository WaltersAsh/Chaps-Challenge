package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Persistence {
  static ObjectMapper mapper = new ObjectMapper();

  public static Maze fileToMaze(File file) {//TODO
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

  public static boolean mazeToFile(Maze maze, File file) {//TODO
    if (file == null) {
      return false;
    }
    System.out.println(maze.getListeners());
    System.out.println(maze);
    try {
      mapper.writeValue(file, maze);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

}
