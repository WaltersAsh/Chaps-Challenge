package maze;

/**
 * A testing class for setting up a Board
 * @author Ian 300474717
 *
 */

public class BoardRig {
	
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
	}
	
	/**
	 * Construct Board from string
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
	 * 
	 * @param s 	a string formatted as above
	 * @return
	 */
	public Tile[][] fromString(String s){
		return null;
		
	}
}
