package nz.ac.vuw.ecs.swen225.gp20.rendering;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.*;

public class AnimationHandler extends MazeEventListener {
  BoardView bv;
  
  public AnimationHandler(Maze m, BoardView bv) {
    m.addListener(this);
    this.bv = bv;
  }
  
  @Override
  public void update(MazeEvent e) {
    if(e instanceof MazeEventWalked) {
      MazeEventWalked w = (MazeEventWalked) e;
      bv.initaliseAnimation(w.getMaze().getChap(), w.getOrigin(), w.getDestination(), w.getDirection());
      bv.setAnimating(true);
    }
  }

  @Override
  public void update(MazeEventPushed e) {
    bv.initaliseAnimation(e.getPushed(), e.getOrigin(), e.getDestination(), e.getDirection());
    bv.setAnimating(true);
  }
}
