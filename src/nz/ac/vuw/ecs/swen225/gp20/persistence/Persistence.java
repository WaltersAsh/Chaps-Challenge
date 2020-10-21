package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import test.nz.ac.vuw.ecs.swen225.gp20.maze.BoardRig;


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
    Maze loadedMaze;
    if (file == null) {
      return null;
    }
    try {
      loadedMaze = mapper.readValue(file, Maze.class);
      if (mazeValidator(loadedMaze)) {
        fixMaze(loadedMaze);
      } else {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return loadedMaze;
  }

  /**
   * Save maze.
   *
   * @param maze the maze object
   * @param file the file object
   * @return the boolean indicate saving is successful or not
   */
  public static boolean saveMaze(Maze maze, File file) {
    if (file == null) {
      return false;
    }
    try {
      mapper.writeValue(file, maze);
      return true;
    } catch (Exception e) {
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
    AtomicBoolean valid = new AtomicBoolean(true);
    if (loadedMaze == null) {
      return false;
    }
    if (loadedMaze.getHeight() < 1 || loadedMaze.getWidth() < 1) {
      valid.set(false);
      throw new Exception("Illegal Maze size, excepted both >0 but height = "
          + loadedMaze.getHeight() + " width = " + loadedMaze.getWidth());
    }
    if (loadedMaze.getExitlock() == null) {
      valid.set(false);
      throw new Exception("Maze has no ExitLock");
    }
    if (loadedMaze.getChap() == null) {
      valid.set(false);
      throw new Exception("Chap is null");
    }
    if (loadedMaze.getChap().filename == null) {
      valid.set(false);
      throw new Exception("Chap's image is missing");
    }
    IntStream.range(0, loadedMaze.getHeight()).parallel().forEach(x -> {
      IntStream.range(0, loadedMaze.getTiles()[x].length).forEach(y -> {
        try {
          if (loadedMaze.getTileAt(x, y).getFilename() == null) {
            valid.set(false);
            throw new Exception("Filename for Tile at [" + x + "," + y + "] " + "is null");
          }
          if (loadedMaze.getTileAt(x, y).getFilename().isEmpty()) {
            valid.set(false);
            throw new Exception("Filename for Tile at [" + x + "," + y + "] " + "is empty");
          }
          if (!Files.exists(Path.of(loadedMaze.getTileAt(x, y).getFilename()))) {
            valid.set(false);
            throw new Exception("File not exist for Tile at [" + x + "," + y + "] ");
          }
        } catch (Exception e) {
          valid.set(false);
          e.printStackTrace();
        }
      });
    });
    return valid.get();
  }

  /**
   * Quick save maze.
   *
   * @param maze the maze
   * @return the boolean indicate saving is successful or not
   */
  public static boolean quickSave(Maze maze) {
    try {
      if (mazeValidator(maze)) {
        Path p = Paths.get(".", "levels", "quickSave.json");
        BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8);
        bw.write(mapper.writeValueAsString(maze));
        bw.close();
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return false;
  }

  /**
   * Quick load maze.
   *
   * @return the loaded maze
   */
  public static Maze quickLoad() {
    try {
      Path p = Paths.get(".", "levels", "quickSave.json");
      //check file exist
      if (p.toFile().exists()) {
        Maze loadedMaze = mapper.readValue(p.toFile(), Maze.class);
        if (mazeValidator(loadedMaze)) {
          fixMaze(loadedMaze);
          return loadedMaze;
        }
      } else { //if file not exist
        throw new IOException("levels/quickSave.json not existed");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    try {
      mapper.writeValue(Paths.get(".", "levels", java.time.LocalDate.now() + "_"
          + java.time.LocalTime.now().getHour() + "_" + java.time.LocalTime.now().getMinute() + "_"
          + java.time.LocalTime.now().getSecond() + ".json").toFile(), BoardRig.enemyKillTest1());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

