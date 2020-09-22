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

  private Tile[][] tiles;
  private int blockSize = 40;
  private int width, height, minPanel;

  private int viewTiles = 4;

  // Stuff for animation
  private Tile from, to;
  private int fromX, fromY, toX, toY;
  private Maze.Direction d;
  private Movable entity;

  private int prevRow = 1;
  private int prevCol = 1;

  Movable toAnimate;

  Animation currentAnimation;

  private double velx, vely;

  List<Animation> animations = new ArrayList<>();
  List<Movable> entitesAnimated = new ArrayList<>();

  public boolean isAnimating = false;
  private boolean isWindowed = false;

  Timer t = new Timer(2, this);
  
  SoundHandler sh;
  AnimationHandler ah;

  public BoardView(Maze m) {
    this.m = m;
    tiles = m.getTiles();
    width = m.getWidth();
    height = m.getHeight();
    sh = new SoundHandler(m);
    ah = new AnimationHandler(m, this);
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
      for(int i=0; i<animations.size();i++) {
        animate(g, animations.get(i));
      }
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
//              toAnimate = (Movable) c;
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

    int windowSize = (2 * viewTiles) + 1;

    blockSize = minPanel / windowSize;

    int chapRow = m.getChap().getContainer().getRow();
    int chapCol = m.getChap().getContainer().getCol();

    int startRow = chapRow - viewTiles;
    int startCol = chapCol - viewTiles;

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
              if (isAnimating && c instanceof Movable&&entitesAnimated.contains(c)) {
                toAnimate = (Movable) c;
                continue;
              }
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
   * @param a the animation its moving
   */
  public void animate(Graphics g, Animation a) {


    currentAnimation = a;
    if (d == Maze.Direction.LEFT) {
      velx = -2;
      vely = 0;
    } else if (d == Maze.Direction.UP) {
      vely = -2;
      velx = 0;
    } else if (d == Maze.Direction.DOWN) {
      vely = 2.5;
      velx = 0;
    } else if (d == Maze.Direction.RIGHT) {
      velx = 2.5;
      vely = 0;
    }

    g.drawImage(getToolkit().getImage(a.getM().getFilename()), a.getFromX(), a.getFromY(), blockSize, blockSize, this);
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
    entitesAnimated.add(entity);

    this.from = from;
    this.to = to;

    System.out.println("From Tile: "+from+ "To Tile: "+to);

    this.entity = entity;
    this.d = d;

    if(isWindowed){
      setWindowedPostitions(from, to, d);
    }
    else{
      fromX = from.getCol() * getBlockSize();
      fromY = from.getRow() * getBlockSize();
      toX = to.getCol() * getBlockSize();
      toY = to.getRow() * getBlockSize();
    }

    System.out.printf("Movable: %s| fromX: %d; toX:%d\n", entity, fromX,toX);
    animations.add(new Animation(entity, fromX, toX, fromY, toY, d));
    // System.out.println(fromX+ " "+toX);
    t.start();
  }

  public int getBlockSize() {
    return blockSize;
  }

  public void setAnimating(boolean animating) {
    isAnimating = animating;
    if (!animating) {
      entitesAnimated.clear();
      animations.clear();
      t.stop();
    }
  }

  /**
   * Sets the from and to positions for the windowed board because its more complicated
   * @param from
   * @param to
   * @param d
   */
  public void setWindowedPostitions(Tile from, Tile to, Maze.Direction d) {

    //Calculate XPos
    if (from.getCol() < viewTiles) {
      fromX = from.getCol() * blockSize;
      toX = to.getCol() * blockSize;
    } else if (from.getCol() >= viewTiles && from.getCol() < width - viewTiles) {
      fromX = viewTiles * blockSize;
      if (d == Maze.Direction.RIGHT) {
        toX = fromX + blockSize;
      } else {
        toX = fromX - blockSize;
      }
    } else {
      fromX = viewTiles*blockSize+(prevCol*blockSize);
      if (d == Maze.Direction.RIGHT) {
        toX = fromX + blockSize;
        prevCol++;
      } else if(d==Maze.Direction.LEFT) {
        toX = fromX - blockSize;
        if(prevCol>1) {
          prevCol--;
        }
      }
    }

    //Calculate yPos
    if (from.getRow() < viewTiles) {
      fromY = from.getRow() * blockSize;
      toY = to.getRow()*blockSize;
    } else if (from.getRow() >= viewTiles && from.getRow() <= height - viewTiles) {
      fromY = viewTiles * blockSize;
      if (d == Maze.Direction.DOWN) {
        toY = fromY + blockSize;
      } else {
        toY = fromY - blockSize;
      }
    } else {
      fromY = viewTiles*blockSize+(prevRow*blockSize);
      if (d == Maze.Direction.DOWN) {
        toY = fromY + blockSize;
        prevRow++;
      } else if(d==Maze.Direction.UP){
        toY = fromY - blockSize;
        if(prevRow>1) {
          prevRow--;
        }
      }
    }

  }

  @Override
  public void actionPerformed(ActionEvent e) {
      repaint();
      double currentFromX = currentAnimation.getFromX();
      double currentFromY = currentAnimation.getFromY();
      currentAnimation.setFromX(currentFromX + velx);
      currentAnimation.setFromY(currentFromY + vely);

      if ((d == Maze.Direction.LEFT && currentAnimation.getFromX() <= currentAnimation.getToX() ) ||
          (d == Maze.Direction.RIGHT && currentAnimation.getFromX() >= currentAnimation.getToX())) {
        setAnimating(false);
      }
      if ((d == Maze.Direction.UP && currentAnimation.getFromY() <= currentAnimation.getToY()) ||
          (d == Maze.Direction.DOWN && currentAnimation.getFromY() >= currentAnimation.getToY())) {
        setAnimating(false);
      }
  }

  public Maze getMaze() {
    return m;
  }
}
