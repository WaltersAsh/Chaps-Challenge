package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.*;
import java.util.*;

@JsonIgnoreType
public class UndoRedoHandler extends MazeEventListener {

  private Stack<MazeEvent> undo = new Stack<>();
  private Stack<MazeEvent> redo = new Stack<>();
  private Maze maze;

  public UndoRedoHandler(Maze m) {
    maze = m;
    maze.addListener(this);
  }
  
  @Override
  public void update(MazeEvent e) {
    //FIXME test for recnplay, only record chaps moves
    if(e instanceof MazeEventEnemyWalked || e instanceof MazeEventEnemyWalkedKilled) {
      return;
    }
    undo.push(e);
  }
  
  public void undo() {
    if(!undo.isEmpty()) {
      MazeEvent e = undo.pop();
      e.invert();
      redo.add(e);
    }
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
