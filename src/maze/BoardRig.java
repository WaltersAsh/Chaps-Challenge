package maze;

import java.util.*;
import java.util.regex.*;

import com.google.common.base.Preconditions;

/**
 * A testing class for setting up a Board
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
	private static Pattern keyPat = Pattern.compile("K([0-9]+)");
	private static Pattern lockPat = Pattern.compile("L([0-9]+)");
	
	public static void main(String[] args) {
		String board = "PA PA PA PA PA PA PA PA PA PA PA PA PA PA PA PA PA\n"+
					   "PA PA PA WA WA WA WA WA PA WA WA WA WA WA PA PA PA\n"+
					   "PA PA PA WA PA PA PA WA WA WA PA PA PA WA PA PA PA\n"+
					   "PA PA PA WA PA TR PA WA EX WA PA TR PA WA PA PA PA\n"+
					   "PA WA WA WA WA WA L1 WA EL WA L1 WA WA WA WA WA PA\n"+
					   "PA WA PA K2 PA L3 PA PA PA PA PA L4 PA K2 PA WA PA\n"+
					   "PA WA PA TR PA WA K3 PA IN PA K4 WA PA TR PA WA PA\n"+
					   "PA WA WA WA WA WA TR PA CH PA TR WA WA WA WA WA PA\n"+
					   "PA WA PA TR PA WA K3 PA PA PA K4 WA PA TR PA WA PA\n"+
					   "PA WA PA PA PA L3 PA PA TR PA PA L4 PA PA PA WA PA\n"+
					   "PA WA WA WA WA WA WA L2 WA L2 WA WA WA WA WA WA PA\n"+
					   "PA PA PA PA PA WA PA PA WA PA PA WA PA PA PA PA PA\n"+
					   "PA PA PA PA PA WA PA TR WA TR PA WA PA PA PA PA PA\n"+
					   "PA PA PA PA PA WA PA PA WA K1 PA WA PA PA PA PA PA\n"+
					   "PA PA PA PA PA WA WA WA WA WA WA WA PA PA PA PA PA\n"+
					   "PA PA PA PA PA PA PA PA PA PA PA PA PA PA PA PA PA";
		System.out.println(BoardRig.fromString(board));
	}
	
	/**
	 * Construct Board from string
	 * 
	 * @param input		string formatted as in class comment
	 * @return
	 */
	public static Board fromString(String input){
		// split the input into rows then tokens
		String[] lines = input.split("\n");
		Tile[][] tiles = new Tile[lines.length][];
		
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
				}else { // otherwise it's a Tile
					row[c]=(Tile)d;
				}
			}
		}
		
		return new Board(tiles);
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
				return new InfoField("");
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
					return new Key("", Integer.valueOf(keyMatch.group(1)));
				}else if(lockMatch.matches()) {
					return new Door("", Integer.valueOf(lockMatch.group(1)));
				}
		}
		return null;
	}
}
