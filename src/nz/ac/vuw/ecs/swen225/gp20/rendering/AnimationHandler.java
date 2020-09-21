package nz.ac.vuw.ecs.swen225.gp20.rendering;

import java.util.*;

import nz.ac.vuw.ecs.swen225.gp20.application.Gui;
import nz.ac.vuw.ecs.swen225.gp20.maze.*;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.*;

public class AnimationHandler extends MazeEventListener {
  BoardView bv;
  
  public AnimationHandler(Maze m, BoardView bv) {
    m.addListener(this);
    this.bv = bv;
  }
  
  @Override
  public void updateAny(MazeEvent e) {
    if(e instanceof MazeEventWalked) {
      MazeEventWalked w = (MazeEventWalked) e;
      System.out.println(w.getOrigin()+" "+ w.getDestination());
      bv.initaliseAnimation(w.getMaze().getChap(), w.getOrigin(), w.getDestination(), w.getDirection());
      bv.setAnimating(true);
    }
  }
}
