package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nz.ac.vuw.ecs.swen225.gp20.maze.BoardRig;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.Tile;


public class MazeToJsonTest {
    static Maze m = BoardRig.lesson1();

    public static void main(String[] args) {
        System.out.println();
//        for (Tile[] row : m.getTiles()) {
//            for (Tile col : row) {
//                System.out.println(col.getClass().getSimpleName());
//            }
//        }

        Gson gson = new GsonBuilder().create();
        RuntimeTypeAdapterFactory<Tile> adapterFactory = RuntimeTypeAdapterFactory.of(Tile.class, "type");


    }
}
