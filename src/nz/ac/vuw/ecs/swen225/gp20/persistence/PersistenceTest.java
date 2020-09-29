package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.vuw.ecs.swen225.gp20.maze.BoardRig;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PersistenceTest {
  static ObjectMapper mapper = new ObjectMapper();
  private static final Maze[] levels = new Maze[]{
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
   * Convert test 01. This test is to make sure all Tile in this
   */
  @Test
  public void convertTest01() {
    for (Maze level : levels) {
      try {
        String json = mapper.writeValueAsString(level);
        System.out.println(json);
        Maze mazeFromJson = mapper.readValue(json, Maze.class);
        String json2 = mapper.writeValueAsString(mazeFromJson);
        assertEquals(json, json2);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
        assert false;
      }
    }

  }

  @Test
  public void fileWritingTest01() {

  }

  public static void main(String[] args) {
    System.out.println(Paths.get(".").toAbsolutePath().normalize().toString());
    new JFileChooser(Paths.get(".").toFile());
  }

}
