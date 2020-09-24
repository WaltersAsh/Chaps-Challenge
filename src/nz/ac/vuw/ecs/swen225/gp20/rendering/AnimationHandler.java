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
    bv.initaliseAnimation(e.getPushed(), e.getDestination(), e.getPushed().getContainer(), e.getDirection());
    bv.setAnimating(true);
  }

  @Override
  public void update(MazeEventPushedWater e) {
    bv.initaliseAnimation(e.getPushed(), e.getDestination(), e.getPushed().getContainer(), e.getDirection());
    bv.setAnimating(true);
  }

  @Override
  public void update(MazeEventEnemyWalked e) {
    bv.initaliseAnimation(e.getEnemy(), e.getEnemyOrigin(), e.getEnemyDestination(), e.getEnemyDirection());
    bv.setAnimating(true);
  }
}
