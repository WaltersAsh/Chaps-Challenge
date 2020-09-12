package application;

import java.util.*;

import javax.tools.DocumentationTool.Location;

import maze.*;


/**
 * Testing text form GUI using sysin/out
 * 
 * @author Ian 300474717
 *
 */

public class TextGUI {
	Maze board;
	List<Treasure> treasure = new ArrayList<>();
	Chap chap;
	
	public TextGUI() {
		board = BoardRig.lesson1();
		listen();
	}
	
	public void displayBoard() {
		System.out.println(board);
	}
	
	public void listen() {
		while(true) {
			displayBoard();
			
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			switch(sc.next().toLowerCase()) {
				case "w":
					board.move(Maze.Direction.UP);
					break;
				case "s":
					board.move(Maze.Direction.DOWN);
					break;
				case "a":
					board.move(Maze.Direction.LEFT);
					break;
				case "d":
					board.move(Maze.Direction.RIGHT);
					break;
				case "q":
					return;
			}
		}
	}
}
