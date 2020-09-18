package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;
import nz.ac.vuw.ecs.swen225.gp20.application.*;


public class MazeEventTest {
	public static void main(String[] args) {
		Gui g = new Gui();
		g.getBoard().getMaze().addListener(new MazeEventListener() {
			
			@Override
			public void notify(MazeEvent e) {
				System.out.println(e);
			}
		});
		g.getFrame().setVisible(true);
	}
}
