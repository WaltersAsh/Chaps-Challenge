package nz.ac.vuw.ecs.swen225.gp20.rendering;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.*;
import java.awt.Toolkit;

public class AnimationHandler extends MazeEventListener {
  BoardView bv;
  protected Toolkit toolkit = Toolkit.getDefaultToolkit();

  public AnimationHandler(Maze m, BoardView bv) {
    m.addListener(this);
    this.bv = bv;

  }

  @Override
  public void update(MazeEvent e) {
    if(e instanceof MazeEventWalked) {
      MazeEventWalked w = (MazeEventWalked) e;
      bv.initaliseAnimation(w.getMaze().getChap(), w.getOrigin(), w.getTarget(), w.getDirection());
      bv.setAnimating(true);
    }
  }

  @Override
  public void update(MazeEventPushed e) {
    bv.initaliseAnimation(e.getPushed(), e.getTarget(), e.getPushed().getContainer(), e.getDirection());
    bv.setAnimating(true);
  }

  @Override
  public void update(MazeEventPushedWater e) {
    bv.initaliseAnimation(e.getPushed(), e.getTarget(), e.getPushed().getContainer(), e.getDirection());
    bv.setAnimating(true);
  }

  @Override
  public void update(MazeEventEnemyWalked e) {
    bv.initaliseAnimation(e.getEnemy(), e.getEnemyOrigin(), e.getEnemyTarget(), e.getEnemyDirection());
    bv.setAnimating(true);
  }

  @Override
  public void update(MazeEventWalkedKilled e) {
    Chap c = e.getMaze().getChap();
    c.changeFile("resources/textures/board/moveable/character_skins/new_player_skin/SteveDeathLeft.gif");
    toolkit.getImage(c.getFilename()).flush();
  }

  @Override
  public void update(MazeEventEnemyWalkedKilled e) {
    Chap c = e.getMaze().getChap();
    c.changeFile("resources/textures/board/moveable/character_skins/new_player_skin/SteveDeathLeft.gif");
    toolkit.getImage(c.getFilename()).flush();
  }

  @Override
  public void update(MazeEventWalkedDrowned e) {
    Chap c = e.getMaze().getChap();
    c.changeFile("resources/textures/board/moveable/character_skins/new_player_skin/SteveDeathLeft.gif");
    toolkit.getImage(c.getFilename()).flush();
  }
}
