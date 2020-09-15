package persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import maze.BoardRig;
import maze.Maze;
import maze.Tile;


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
