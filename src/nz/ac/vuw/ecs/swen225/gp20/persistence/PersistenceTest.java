package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.vuw.ecs.swen225.gp20.maze.BoardRig;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;


public class PersistenceTest {
  static ObjectMapper mapper = new ObjectMapper();
//  private static final Maze[] levels = new Maze[]{
//      BoardRig.lesson1(),
//      BoardRig.crateTest(),
//      BoardRig.crateAndWaterTest(),
//      BoardRig.pathFindTest1(),
//      BoardRig.levelEditorTest1(),
//      BoardRig.levelEditorTest2(),
//      BoardRig.levelEditorTest3(),
//      BoardRig.teleporterTest1(),
//  };

  /**
   * Convert test 01. This test is to make sure all Tile in this
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
    };
    for (Maze level : levels) {
      try {
        String json = mapper.writeValueAsString(level);
        Persistence.quickSave(level);
        String json2 = mapper.writeValueAsString(Persistence.quickLoad());
        assertEquals(json, json2);
      } catch (Exception e) {
        e.printStackTrace();
        assert false;
      }
    }
  }


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

  @Test
  public void saveMazeFailTest01() {
    try {
      assertFalse(Persistence.saveMaze(null, null));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void saveMazeFailTest02() {
    try {
      Path p = Paths.get(".");
      assertFalse(Persistence.saveMaze(BoardRig.lesson1(), p.toFile()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void mazeValidatorTest01() {
    Maze m = BoardRig.lesson1();
    m.setHeight(-5);
    m.setWidth(-5);
    assertFalse(Persistence.quickSave(m));
  }

  @Test
  public void mazeValidatorTest02() {
    Path p = Paths.get(".", "levels", "quickSave.json");
    Maze m = BoardRig.lesson1();
    m.setHeight(-5);
    m.setWidth(-5);
    try {
      mapper.writeValue(p.toFile(), m);
      assertNull(Persistence.quickLoad());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

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

  @Test
  public void loadMazeFailTest01() {
    Path p = Paths.get(".");
    assertNull(Persistence.loadMaze(p.toFile()));
  }

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
  @Test
  public void loadMazeFailTest03() {
    assertNull(Persistence.loadMaze(null));
  }

}
