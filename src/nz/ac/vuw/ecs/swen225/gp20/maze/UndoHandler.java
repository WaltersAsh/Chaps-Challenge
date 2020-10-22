package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import java.util.Stack;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEvent;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventEnemyWalked;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventEnemyWalkedKilled;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventListener;

/**
 * Undo class, for handling the undo functionality.
 * 
 * @author Ian 300474717
 *
 */
@JsonIgnoreType
public class UndoHandler extends MazeEventListener {

  private Stack<MazeEvent> undo = new Stack<>();
  private Stack<MazeEvent> redo = new Stack<>();
  private Maze maze;
  private boolean recordEnemies;

  /**
   * Constructor for the UndoHandler.
   * 
   * @param m             The maze to record moves and call undo functionality on
   * @param recordEnemies Whether to record Enemy moves or not
   */
  public UndoHandler(Maze m, boolean recordEnemies) {
    maze = m;
    maze.addListener(this);
    this.recordEnemies = recordEnemies;
  }

  @Override
  public void update(MazeEvent e) {
    if (e instanceof MazeEventEnemyWalked || e instanceof MazeEventEnemyWalkedKilled) {
      if (!recordEnemies) {
        return;
      }
    }
    undo.push(e);
  }

  /**
   * Undo the last move.
   * 
   * @return the MazeEvent which was just undone
   */
  public MazeEvent undo() {
    if (!undo.isEmpty()) {
      MazeEvent e = undo.pop();
      System.out.println(e);
      e.invert();
      redo.add(e);
      return e;
    }
    return null;
  }

  /**
   * Redo the last undone move.
   */
  public void redo() {
    throw new UnsupportedOperationException("Redo is not implemented.");
  }

  /**
   * Clear the Redo stack.
   */
  public void clearRedoStack() {
    redo.clear();
  }
}
