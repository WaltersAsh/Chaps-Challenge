package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.util.*;
import java.util.regex.*;

import com.google.common.base.Preconditions;

/**
 * A testing class for setting up a text only Board
 * @author Ian 300474717
 *	
 * WA = wall
 * PA = path
 * KX = key with #X
 * LX = lock with #X
 * TR = treasure
 * IN = info square
 * EL = exit lock
 * EX = exit
 * CH = chap
 * XX = crate
 */

public class BoardRig {
	private static Pattern keyPat = Pattern.compile("K(.)");
	private static Pattern lockPat = Pattern.compile("L(.)");
	
	public static void main(String[] args) {
		System.out.println(lesson1());
	}
	
	public static Maze lesson1() {
		String board = "PA PA PA PA PA PA PA PA PA PA PA PA PA PA PA PA PA\n"+
				   "PA PA PA WA WA WA WA WA PA WA WA WA WA WA PA PA PA\n"+
				   "PA PA PA WA PA PA PA WA WA WA PA PA PA WA PA PA PA\n"+
				   "PA PA PA WA PA TR PA WA EX WA PA TR PA WA PA PA PA\n"+
				   "PA WA WA WA WA WA LG WA EL WA LG WA WA WA WA WA PA\n"+
				   "PA WA PA KY PA LB PA PA PA PA PA LR PA KY PA WA PA\n"+
				   "PA WA PA TR PA WA KB PA IN PA KR WA PA TR PA WA PA\n"+
				   "PA WA WA WA WA WA TR PA CH PA TR WA WA WA WA WA PA\n"+
				   "PA WA PA TR PA WA KB PA PA PA KR WA PA TR PA WA PA\n"+
				   "PA WA PA PA PA LR PA PA TR PA PA LB PA PA PA WA PA\n"+
				   "PA WA WA WA WA WA WA LY WA LY WA WA WA WA WA WA PA\n"+
				   "PA PA PA PA PA WA PA PA WA PA PA WA PA PA PA PA PA\n"+
				   "PA PA PA PA PA WA PA TR WA TR PA WA PA PA PA PA PA\n"+
				   "PA PA PA PA PA WA PA PA WA KG PA WA PA PA PA PA PA\n"+
				   "PA PA PA PA PA WA WA WA WA WA WA WA PA PA PA PA PA\n"+
				   "PA PA PA PA PA PA PA PA PA PA PA PA PA PA PA PA PA";
		return BoardRig.fromString(board);
	}
	
	/**
	 * Construct Board from string
	 * 
	 * @param input		string formatted as in class comment
	 * @return
	 */
	public static Maze fromString(String input){
		// split the input into rows then tokens
		String[] lines = input.split("\n");
		Tile[][] tiles = new Tile[lines.length][];
		List<Containable> entities = new ArrayList<>();
		
		for(int r=0; r<lines.length; r++) {
				
			String[] line = lines[r].split("\\s");
			Tile[] row = new Tile[line.length];
			
			for(int c=0; c<line.length; c++) {
				Drawable d = fromToken(line[c]);
				// if it's a containable, make a PathTile and place the containable inside
				if(d instanceof Containable) {
					PathTile p = new PathTile("");
					p.place((Containable)d);
					row[c]=p;
					entities.add((Containable)d);
				}else { // otherwise it's a Tile
					row[c]=(Tile)d;
				}
				row[c].setCoords(r, c); // ensure the 2 way link
			}
			tiles[r]=row;
		}
		return new Maze(tiles, entities);
	}
	
	/**
	 * Get a Drawable from a string token
	 * @param token
	 * @return
	 */
	public static Drawable fromToken(String token) {
		Preconditions.checkArgument(token.length()==2, "Token has length %s but expected 2", token.length());
		switch(token) {
			case "WA":
				return new WallTile("");
			case "PA":
				return new PathTile("");
			case "TR":
				return new Treasure("");
			case "IN":
				return new InfoField("", "testing");
			case "EL":
				return new ExitLock("");
			case "EX":
				return new Exit("");
			case "CH":
				return new Chap("");
			case "XX":
				return new Crate("");
			default:
				Matcher keyMatch = keyPat.matcher(token);
				Matcher lockMatch = lockPat.matcher(token);
				
				if(keyMatch.matches()) {
					return new Key("", colorFromToken(keyMatch.group(1)));
				}else if(lockMatch.matches()) {
					return new Door("", colorFromToken(lockMatch.group(1)));
				}
		}
		return null;
	}
	
	public static Maze.KeyColor colorFromToken(String token){
		switch(token) {
			case "B":
				return Maze.KeyColor.BLUE;
			case "R":
				return Maze.KeyColor.RED;
			case "G":
				return Maze.KeyColor.GREEN;
			case "Y":
				return Maze.KeyColor.YELLOW;
			default:
				throw new IllegalArgumentException();
		}
	}
}
