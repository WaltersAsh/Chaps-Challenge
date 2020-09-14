package nz.ac.vuw.ecs.swen225.gp20.application;

import java.util.*;

import javax.tools.DocumentationTool.Location;
import nz.ac.vuw.ecs.swen225.gp20.maze.BoardRig;
import nz.ac.vuw.ecs.swen225.gp20.maze.Chap;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.Treasure;


/**
 * Testing text form GUI using sysin/out
 * 
 * @author Ian 300474717
 *
 */

public class TextGUI {
	Maze maze;
	List<Treasure> treasure = new ArrayList<>();
	Chap chap;
	
	public TextGUI() {
		maze = BoardRig.lesson1();
		displayBoard();
		listen();
	}
	
	public void displayBoard() {
		System.out.println(maze);
	}
	
	public void listen() {
		while(!maze.isLevelFinished()) {
			
			
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			switch(sc.next().toLowerCase()) {
				case "w":
					maze.move(Maze.Direction.UP);
					break;
				case "s":
					maze.move(Maze.Direction.DOWN);
					break;
				case "a":
					maze.move(Maze.Direction.LEFT);
					break;
				case "d":
					maze.move(Maze.Direction.RIGHT);
					break;
				case "q":
					return;
			}
			displayBoard();
			System.out.println(maze.getChap().getKeys());
			System.out.println(maze.getChap().getTreasures());
		}
	}
}
