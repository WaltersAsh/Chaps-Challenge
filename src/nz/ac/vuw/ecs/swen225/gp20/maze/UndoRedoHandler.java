package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.*;
import java.util.*;

@JsonIgnoreType
public class UndoRedoHandler extends MazeEventListener {

  private Stack<MazeEvent> undo = new Stack<>();
  private Stack<MazeEvent> redo = new Stack<>();
  private Maze maze;
  private boolean recordEnemies;

  public UndoRedoHandler(Maze m, boolean recordEnemies) {
    maze = m;
    maze.addListener(this);
    this.recordEnemies = recordEnemies;
  }
  
  @Override
  public void update(MazeEvent e) {

    //FIXME test for recnplay, only record chaps moves
    if(e instanceof MazeEventEnemyWalked || e instanceof MazeEventEnemyWalkedKilled) {
      if(!recordEnemies) {
        return;
      };
    }
    undo.push(e);
  }
  
  public MazeEvent undo() {
    if(!undo.isEmpty()) {
      MazeEvent e = undo.pop();
      System.out.println(e);
      e.invert();
      redo.add(e);
      return e;
    }
    return null;
  }
  
  public void redo() {
    if(!redo.isEmpty()) {
      MazeEvent e = redo.pop();
    }
  }
  
  public void clearRedoStack() {
    redo.clear();
  }
}
