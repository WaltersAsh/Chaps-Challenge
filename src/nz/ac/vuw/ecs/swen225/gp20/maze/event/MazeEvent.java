package nz.ac.vuw.ecs.swen225.gp20.maze.event;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * Base class for all broadcastable events which happen in Maze.
 * 
 * @author Ian 300474717
 *
 */
public interface MazeEvent {
  void accept(MazeEventListener listener);
}
