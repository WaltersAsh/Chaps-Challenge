package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.vuw.ecs.swen225.gp20.maze.BoardRig;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import org.junit.jupiter.api.Test;

public class Tester {
  static ObjectMapper mapper = new ObjectMapper();
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

  @Test
  public void convertTest01() {
    for (Maze level : levels) {
      try {
        String json = mapper.writeValueAsString(level);
        System.out.println(json);
        mapper.readValue(json, Maze.class);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
        assert false;
      }
    }
  }
}
