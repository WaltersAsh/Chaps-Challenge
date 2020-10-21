package test.nz.ac.vuw.ecs.swen225.gp20.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.IntStream;
import nz.ac.vuw.ecs.swen225.gp20.maze.BoardRig;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;



/**
 * The Persistence test class. Contains unit test for the class Persistence.
 *
 * @author Fangyi Yan 300519195
 */
public class PersistenceTest {
  static ObjectMapper mapper = new ObjectMapper();

  /**
   * Delete quick save after each test.
   */
  @AfterEach
  public void deleteQuickSave() {
    Path p = Paths.get(".", "levels", "quickSave.json");
    try {
      Files.deleteIfExists(p);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Convert test 01. This test is to check all Tile we developed
   * can be serialize. Uses parallelStream to speed up process.
   */
  @Test
  public void convertTest01() {
    Maze[] levels = new Maze[]{
        BoardRig.lesson1(),
        BoardRig.crateTest(),
        BoardRig.crateAndWaterTest(),
        BoardRig.pathFindTest1(),
        BoardRig.levelEditorTest1(),
        BoardRig.levelEditorTest2(),
        BoardRig.levelEditorTest3(),
        BoardRig.teleporterTest1(),
        BoardRig.enemyKillTest1(),
    };
    Arrays.stream(levels).parallel().forEach(level -> {
      try {
        level.pause();
        String json = mapper.writeValueAsString(level);
        Maze loadedMaze = mapper.readValue(json, Maze.class);
        IntStream.range(0, level.getHeight()).forEach(x ->
            IntStream.range(0, level.getTiles()[x].length).forEach(y -> {
              assertEquals(level.getTileAt(x, y), loadedMaze.getTileAt(x, y));
            }));
      } catch (Exception e) {
        e.printStackTrace();
        assert false;
      }
    });
  }


  /**
   * Save maze test 01. Test if sample level can be serialize
   * and save into a file and deserialize correctly.
   */
  @Test
  public void saveMazeTest01() {
    Path p = Paths.get(".", "levels", "quickSave.json");
    Maze m = BoardRig.lesson1();
    m.pause();
    try {
      String s = mapper.writeValueAsString(m);
      Persistence.saveMaze(m, p.toFile());
      assertEquals(s, Files.readString(p));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Save maze fail test 01. This test case check Persistence.saveMaze()
   * won't crush when args are null.
   */
  @Test
  public void saveMazeFailTest01() {
    try {
      assertFalse(Persistence.saveMaze(null, null));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Save maze fail test 02. This test case check Persistence.saveMaze()
   * won't crush when Path passed to it is Illegal.
   */
  @Test
  public void saveMazeFailTest02() {
    try {
      Path p = Paths.get(".");
      assertFalse(Persistence.saveMaze(BoardRig.lesson1(), p.toFile()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Maze validator test 01. This test check if Persistence.mazeValidator() can work correctly.
   */
  @Test
  public void mazeValidatorTest01() {
    Maze m = BoardRig.lesson1();
    m.setHeight(-5);
    m.setWidth(-5);
    assertFalse(Persistence.quickSave(m));
  }

  /**
   * Maze validator test 02. This test check if Persistence.mazeValidator() can work correctly.
   */
  @Test
  public void mazeValidatorTest02() {
    Path p = Paths.get(".", "levels", "quickSave.json");
    Maze m = BoardRig.lesson1();
    m.setHeight(-114);
    m.setWidth(-514);
    try {
      mapper.writeValue(p.toFile(), m);
      assertNull(Persistence.quickLoad());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Maze validator test 03. This test check if Persistence.mazeValidator() can work correctly.
   */
  @Test
  public void mazeValidatorTest03() {
    Maze m = BoardRig.lesson1();
    m.getChap().filename = null;
    assertFalse(Persistence.quickSave(m));
  }

  /**
   * Maze validator test 04. This test check if Persistence.mazeValidator() can work correctly.
   */
  @Test
  public void mazeValidatorTest04() {
    Maze m = BoardRig.lesson1();
    m.setExitlock(null);
    assertFalse(Persistence.quickSave(m));
  }

  /**
   * Maze validator test 05. This test check if Persistence.mazeValidator() can work correctly.
   */
  @Test
  public void mazeValidatorTest05() {
    Maze m = BoardRig.lesson1();
    m.setChap(null);
    assertFalse(Persistence.quickSave(m));
  }

  /**
   * Maze validator test 06. This test check if Persistence.mazeValidator() can work correctly.
   */
  @Test
  public void mazeValidatorTest06() {
    Maze m = BoardRig.lesson1();
    m.getTileAt(0, 0).filename = null;
    assertFalse(Persistence.quickSave(m));
  }

  /**
   * Maze validator test 07. This test check if Persistence.mazeValidator() can work correctly.
   */
  @Test
  public void mazeValidatorTest07() {
    Maze m = BoardRig.lesson1();
    m.getTileAt(0, 0).filename = "";
    assertFalse(Persistence.quickSave(m));
  }

  /**
   * Maze validator test 08. This test check if Persistence.mazeValidator() can work correctly.
   */
  @Test
  public void mazeValidatorTest08() {
    Maze m = BoardRig.lesson1();
    assert Files.notExists(Path.of("thisFileShouldNotExist"));
    m.getTileAt(0, 0).filename = "thisFileShouldNotExist";
    assertFalse(Persistence.quickSave(m));
  }

  /**
   * Quick load test 01. This test check Persistence.quickLoad() works correctly or not.
   */
  @Test
  public void quickLoadTest01() {
    Path p = Paths.get(".", "levels", "quickSave.json");
    Maze m = BoardRig.lesson1();
    try {
      mapper.writeValue(p.toFile(), m);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Quick load fail test 01. This test case check what Persistence.quickLoad() will return
   * when loading from an invalidate file (should be null).
   */
  @Test
  public void quickLoadFailTest01() {
    Path p = Paths.get(".", "levels", "quickSave.json");
    Maze m = BoardRig.lesson1();
    m.setHeight(-5);
    m.setWidth(-5);
    try {
      mapper.writeValue(p.toFile(), m);
      assert Persistence.quickLoad() == null;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Quick load fail test 02. This test case check what Persistence.quickLoad() will return
   * when loading from an not exist file (should be null).
   */
  @Test
  public void quickLoadFailTest02() {
    Path p = Paths.get(".", "levels", "quickSave.json");
    try {
      Files.deleteIfExists(p);
      assertFalse(p.toFile().exists());
      Persistence.quickLoad();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Load maze test 01. This test case check if Persistence.loadMaze()
   * can load maze from a valid File correctly.
   */
  @Test
  public void loadMazeTest01() {
    Path p = Paths.get(".", "levels", "quickSave.json");
    try {
      Files.deleteIfExists(p);
      assertFalse(p.toFile().exists());
    } catch (IOException e) {
      e.printStackTrace();
    }
    Maze m = BoardRig.lesson1();
    Persistence.quickSave(m);
    Maze loadedMaze = Persistence.loadMaze(p.toFile());
    assertEquals(m.toString(), loadedMaze.toString());
  }

  /**
   * Load maze fail test 01. This test case check what
   * Persistence.loadMaze() will return when a invalid path
   * is passed to it (Should return null).
   */
  @Test
  public void loadMazeFailTest01() {
    Path p = Paths.get(".");
    assertNull(Persistence.loadMaze(p.toFile()));
  }

  /**
   * Load maze fail test 02. This test case check what
   * Persistence.loadMaze() will return when a invalid file
   * is passed to it (Should return null).
   */
  @Test
  public void loadMazeFailTest02() {
    Path p = Paths.get(".", "levels", "quickSave.json");
    Maze m = BoardRig.lesson1();
    m.setHeight(-5);
    m.setWidth(-5);
    try {
      mapper.writeValue(p.toFile(), m);
      assertNull(Persistence.loadMaze(p.toFile()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Load maze fail test 03. This test case check
   * what Persistence.loadMaze() will return when null is passed to it
   * (Should return null).
   */
  @Test
  public void loadMazeFailTest03() {
    assertNull(Persistence.loadMaze(null));
  }

}
