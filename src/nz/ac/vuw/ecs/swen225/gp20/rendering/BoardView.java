package nz.ac.vuw.ecs.swen225.gp20.rendering;

import nz.ac.vuw.ecs.swen225.gp20.application.Gui;
import nz.ac.vuw.ecs.swen225.gp20.maze.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class BoardView extends JComponent implements ActionListener {

  private Maze m;
  private final Maze startMaze;

  private Tile[][] tiles;
  private int blockSize = 40;
  private int width, height, minPanel;

  private int viewTiles = 3;

  // Stuff for animation
  private Tile from, to;
  private int fromX, fromY, toX, toY;

  private int startRow, startCol;
  private int windowSize = (2 * viewTiles) + 1;

  private int drawX, drawY;
  private Maze.Direction d;
  private Movable entity;

  private int prevRow = 1;
  private int prevCol = 1;

  int startCount = 0;

  Movable toAnimate;

  Animation currentAnimation;

  private double velx, vely;

  List<Animation> animations = new ArrayList<>();
  List<Movable> entitesAnimated = new ArrayList<>();

  public boolean isAnimating = false;
  private boolean isWindowed = true;
  private boolean boardNeedMove = false;

  Timer t = new Timer(5, this);
  
  SoundHandler sh;
  AnimationHandler ah;

  public BoardView(Maze m) {
    this.m = m;
    tiles = m.getTiles();
    width = m.getWidth();
    height = m.getHeight();
    sh = new SoundHandler(m);
    ah = new AnimationHandler(m, this);

    startMaze = m;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    minPanel = Math.min(Gui.boardPanel.getHeight(), Gui.boardPanel.getWidth());


    if(isWindowed) {
      drawWindowedBoard(g);
    }
    else{
      drawWholeBoard(g);
    }

    if (isAnimating) {
      animate(g);
    }
  }

  /**
   * Draws the whole board.
   * @param g the graphics it draws.
   */
  public void drawWholeBoard(Graphics g) {
    if (width > height) {
      blockSize = Gui.boardPanel.getWidth() / width;
    } else {
      blockSize = Gui.boardPanel.getHeight() / height;
    }

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        Tile t = m.getTileAt(row, col);
        g.drawImage(getToolkit().getImage(t.getFilename()), col * blockSize, row * blockSize,
            blockSize, blockSize, this);
        if (t instanceof PathTile) {
          PathTile pt = (PathTile) t;
          if (!pt.getContainedEntities().isEmpty()) {
            for (Containable c : pt.getContainedEntities()) {
              if (isAnimating && c instanceof Movable&&entitesAnimated.contains(c)) {
                continue;
              }
              g.drawImage(getToolkit().getImage(c.getFilename()), col * blockSize, row * blockSize,
                  blockSize, blockSize, this);
            }
          }
        }
      }
    }

  }

  /**
   * Draws the windowed board on the screen.
   * 
   * @param g the graphics used
   */
  public void drawWindowedBoard(Graphics g) {
    blockSize = minPanel / windowSize;

    int chapRow = m.getChap().getContainer().getRow();
    int chapCol = m.getChap().getContainer().getCol();

    startRow = chapRow - viewTiles;
    startCol = chapCol - viewTiles;

    if (startRow < 0) {
      startRow = 0;
    }
    if (startCol < 0) {
      startCol = 0;
    }

    if (startCol + windowSize > width - 1) {
      startCol = width - windowSize;
    }
    if (startRow + windowSize > height - 1) {
      startRow = height - windowSize;
    }

    int currentRow = 0;
        for (int row = startRow; row < startRow + windowSize; row++) {
          int currentCol = 0;
          for (int col = startCol; col < startCol + windowSize; col++) {
            Tile t = m.getTileAt(row, col);
            g.drawImage(getToolkit().getImage(t.getFilename()), currentCol * blockSize,
                currentRow * blockSize, blockSize, blockSize, this);
        if (t instanceof PathTile) {
          PathTile pt = (PathTile) t;
          if (!pt.getContainedEntities().isEmpty()) {
            for (Containable c : pt.getContainedEntities()) {
//              if (isAnimating && c instanceof Movable&&entitesAnimated.contains(c)) {
//                toAnimate = (Movable) c;
//                continue;
//              }
              g.drawImage(getToolkit().getImage(c.getFilename()), currentCol * blockSize,
                  currentRow * blockSize, blockSize, blockSize, this);
            }
          }
        }
        currentCol++;
      }
      currentRow++;

    }
  }

  /**
   * Moves the movable object along.
   * @param g graphics object its drawing
   */
  public void animate(Graphics g) {

    if (d == Maze.Direction.LEFT) {
      velx = -3;
      vely = 0;
    } else if (d == Maze.Direction.UP) {
      vely = -3;
      velx = 0;
    } else if (d == Maze.Direction.DOWN) {
      vely = 3.5;
      velx = 0;
    } else if (d == Maze.Direction.RIGHT) {
      velx = 3.5;
      vely = 0;
    }

    if(!isWindowed||(from.getCol()<=viewTiles&&from.getRow()<= viewTiles)) {
      for (int i = 0; i < animations.size(); i++) {
        Animation a = animations.get(i);
        g.drawImage(getToolkit().getImage(a.getM().getFilename()), a.getFromX(), a.getFromY(), blockSize, blockSize, this);
      }
    }
    else{
      drawMoveBoard(g);
    }

  }

  public void drawMoveBoard(Graphics g){

  }

  /**
   * Initialises the parameters for the animation.
   *
   * @param entity what is moving
   * @param from where from
   * @param to where its moving to
   * @param d the direction its moving
   */
  public void initaliseAnimation(Movable entity, Tile from, Tile to, Maze.Direction d) {


    this.from = from;
    this.to = to;

    this.entity = entity;
    this.d = d;

    if(isWindowed){
//      startRow = entity.getContainer().getRow() - viewTiles;
//      startCol = entity.getContainer().getCol() - viewTiles;

      //System.out.println("___________________");
    }
    else{
      fromX = from.getCol() * getBlockSize();
      fromY = from.getRow() * getBlockSize();
      toX = to.getCol() * getBlockSize();
      toY = to.getRow() * getBlockSize();
    }

    if(!isWindowed) {
      entitesAnimated.add(entity);
      animations.add(new Animation(entity, fromX, toX, fromY, toY, d));
      t.start();
    }

  }

  public void setAnimating(boolean animating) {
    isAnimating = animating;
    if (!animating) {
      animations.clear();
      entitesAnimated.clear();
      startCount = 0;
      t.stop();
      //System.out.println("Animation Stopped");
    }
  }

  public void checkAnimation(){
    if(animations.isEmpty()||startCount>blockSize){
      setAnimating(false);

    }
  }

  /**
   * Sets the from and to positions for the windowed board because its more complicated
   * @param from
   * @param to
   * @param d
   */
  public void setWindowedPositions(Tile from, Tile to, Maze.Direction d) {

  }

  @Override
  public void actionPerformed(ActionEvent e) {
      repaint();
      if(!boardNeedMove) {
        for (int i = 0; i < animations.size(); i++) {
          currentAnimation = animations.get(i);
          double currentFromX = currentAnimation.getFromX();
          double currentFromY = currentAnimation.getFromY();
          currentAnimation.setFromX(currentFromX + velx);
          currentAnimation.setFromY(currentFromY + vely);

          if ((d == Maze.Direction.LEFT && currentAnimation.getFromX() <= currentAnimation.getToX()) ||
              (d == Maze.Direction.RIGHT && currentAnimation.getFromX() >= currentAnimation.getToX())) {
            entitesAnimated.remove(currentAnimation.getM());
            animations.remove(currentAnimation);
            checkAnimation();
          }
          if ((d == Maze.Direction.UP && currentAnimation.getFromY() <= currentAnimation.getToY()) ||
              (d == Maze.Direction.DOWN && currentAnimation.getFromY() >= currentAnimation.getToY())) {
            entitesAnimated.remove(currentAnimation.getM());
            animations.remove(currentAnimation);
            checkAnimation();
          }
        }
      }
    else if(startCount>blockSize){
      checkAnimation();
    }
    else{
        drawX += velx;
        drawY += vely;
    }
  }

  public Maze getMaze() {
    return m;
  }

  public boolean isAnimating() {
    return isAnimating;
  }


  public int getBlockSize() {
    return blockSize;
  }

  public void reset(){
    m = startMaze;
    m = BoardRig.lesson1();

    tiles = m.getTiles();
    width = m.getWidth();
    height = m.getHeight();
    sh = new SoundHandler(m);
    ah = new AnimationHandler(m, this);
    repaint();
  }

}
