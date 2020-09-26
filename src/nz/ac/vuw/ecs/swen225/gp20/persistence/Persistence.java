package nz.ac.vuw.ecs.swen225.gp20.persistence;

import nz.ac.vuw.ecs.swen225.gp20.maze.BoardRig;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

import java.io.File;

public class Persistence {

  public static Maze fileToMaze(File file) {//TODO
    return BoardRig.lesson1();
  }

  public static File mazeToFile(Maze maze, File file) {//TODO
    System.out.println(111);
    System.out.println(maze);
    return null;
  }

}
