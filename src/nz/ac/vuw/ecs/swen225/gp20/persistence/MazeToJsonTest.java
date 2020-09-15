package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nz.ac.vuw.ecs.swen225.gp20.maze.*;


public class MazeToJsonTest {
    static Maze m = BoardRig.lesson1();

    public static void main(String[] args) {
        System.out.println();
        for (Tile[] row : m.getTiles()) {
            for (Tile col : row) {
                System.out.println(col.getClass().getSimpleName());
            }
        }

        RuntimeTypeAdapterFactory<Tile> typeAdapterFactory = RuntimeTypeAdapterFactory.of(Tile.class, "type").registerSubtype(PathTile.class).registerSubtype(WallTile.class);
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory).create();
        System.out.println(gson.toJson(m.getTiles()));


    }
}
