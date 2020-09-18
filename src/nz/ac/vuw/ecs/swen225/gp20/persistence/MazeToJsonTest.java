package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.vuw.ecs.swen225.gp20.maze.BoardRig;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

public class MazeToJsonTest {
  static Maze m = BoardRig.lesson1();
  static ObjectMapper mapper = new ObjectMapper();

  public static void main(String[] args) throws JsonProcessingException {

    System.out.println();
//        for (Tile[] row : m.getTiles()) {
//            for (Tile col : row) {
//                System.out.println(col.getClass().getSimpleName());
//            }
//        }
    String jsonString = mapper.writeValueAsString(m.getTiles()[3]);
    System.out.println(jsonString);

  }
}
