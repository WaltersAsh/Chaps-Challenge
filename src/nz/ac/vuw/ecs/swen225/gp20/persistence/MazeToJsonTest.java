package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.vuw.ecs.swen225.gp20.maze.BoardRig;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

public class MazeToJsonTest {
  static Maze m = BoardRig.levelEditorTest3();
  static ObjectMapper mapper = new ObjectMapper();

  public static void main(String[] args) throws JsonProcessingException {

    System.out.println();
//        for (Tile[] row : m.getTiles()) {
//            for (Tile col : row) {
//                System.out.println(col.getClass().getSimpleName());
//            }
//        }
    String jsonString = mapper.writeValueAsString(m);
    System.out.println(jsonString);
    mapper.readValue(jsonString, Maze.class);
  }

  public static Maze pathfindLoader() {
    String json = null;
    try {
      json = mapper.writeValueAsString(BoardRig.pathFindTest1());
      System.out.println(json);
      return mapper.readValue(json, Maze.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;

  }

  public static Maze testLoader() {
    String jsonString = null;
    try {
      jsonString = mapper.writeValueAsString(m);
      System.out.println(jsonString);
      Maze m2 = mapper.readValue(jsonString, Maze.class);
      return m2;
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

}
