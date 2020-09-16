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
        String jsonString = mapper.writeValueAsString(m);
        System.out.println(jsonString);
        Maze m2 = mapper.readValue(jsonString, Maze.class);
        System.out.println(mapper.writeValueAsString(m2));
        //try recreate

    }
    public static Maze createM2()  {
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(m);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Maze m2 = null;
        try {
            m2 = mapper.readValue(jsonString, Maze.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return m2;
    }
    public MazeToJsonTest() {
    }
}
