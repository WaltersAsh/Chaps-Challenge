package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.vuw.ecs.swen225.gp20.maze.BoardRig;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MazeToJsonTest {
  static Maze m = BoardRig.lesson1();
  static ObjectMapper mapper = new ObjectMapper();

  public static void main(String[] args) throws JsonProcessingException {
    try {
      mapper.writeValue(Paths.get(".","levels",java.time.LocalDate.now()+"_"+java.time.LocalTime.now()+".json").toFile(),BoardRig.enemyKillTest1());
    } catch (IOException e) {
      e.printStackTrace();
    }
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
