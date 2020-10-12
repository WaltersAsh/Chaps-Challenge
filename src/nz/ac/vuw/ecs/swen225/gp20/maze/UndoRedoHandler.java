package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.Direction;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.*;
import java.util.*;
@JsonIgnoreType
public class UndoRedoHandler extends MazeEventListener {
  Maze maze;
  public UndoRedoHandler(Maze m) {
    maze = m;
    maze.addListener(this);
  }
  
  private Stack<MazeEvent> undo = new Stack<>();
  private Stack<MazeEvent> redo = new Stack<>();
  
  @Override
  public void update(MazeEvent e) {
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
