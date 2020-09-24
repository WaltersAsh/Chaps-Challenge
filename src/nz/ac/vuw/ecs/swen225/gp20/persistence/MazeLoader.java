package nz.ac.vuw.ecs.swen225.gp20.persistence;

import nz.ac.vuw.ecs.swen225.gp20.maze.BoardRig;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

import java.io.File;

public class MazeLoader {
    public static Maze loadLevel1() {
        return null;
    }

    public static Maze fileToMaze(File file) {//TODO
        return BoardRig.lesson1();
    }

    public static File mazeToFile(Maze maze) {//TODO
        return null;
    }

}
