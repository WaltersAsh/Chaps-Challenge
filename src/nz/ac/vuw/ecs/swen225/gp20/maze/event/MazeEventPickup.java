package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

public class MazeEventPickup extends MazeEvent{
	private Pickup picked;
	public MazeEventPickup(Maze m, Pickup picked) {
		super(m);
		this.picked = picked;  
	}
	
	public Pickup getPicked() {
		return picked;
	}

}
