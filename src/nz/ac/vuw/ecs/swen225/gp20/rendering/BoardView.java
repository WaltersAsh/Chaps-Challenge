package nz.ac.vuw.ecs.swen225.gp20.rendering;

import nz.ac.vuw.ecs.swen225.gp20.application.Gui;
import nz.ac.vuw.ecs.swen225.gp20.maze.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardView extends JComponent implements ActionListener {

  private Maze m;

  private Tile[][] tiles;
  private int blockSize = 40;
  private int width, height, minPanel;

  // Stuff for animation
  private Tile from, to;
  private int fromX, fromY, toX, toY;
  private Maze.Direction d;
  private Movable entity;

  Chap toAnimate;

  private double velx, vely;

  public boolean isAnimating = false;

  Timer t = new Timer(10, this);
  
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

    drawWholeBoard(g);
    // drawWindowedBoard(g);
    if (isAnimating) {
      animate(g, toAnimate);
    }
  }

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
              if (isAnimating && c instanceof Chap) {
                toAnimate = (Chap) c;
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
    int viewTiles = 3;
    int windowSize = (2 * viewTiles) + 1;

    int blockSize = minPanel / windowSize;

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
              if (isAnimating && c instanceof Chap) {
                toAnimate = (Chap) c;
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

  public void animate(Graphics g, Chap c) {
    if (d == Maze.Direction.LEFT) {
      velx = -1;
      vely = 0;
    } else if (d == Maze.Direction.UP) {
      vely = -1;
      velx = 0;
    } else if (d == Maze.Direction.DOWN) {
      vely = 1.5;
      velx = 0;
    } else if (d == Maze.Direction.RIGHT) {
      velx = 1.5;
      vely = 0;
    }

    g.drawImage(getToolkit().getImage(c.getFilename()), fromX, fromY, blockSize, blockSize, this);
  }

  public void initaliseAnimation(Movable entity, Tile from, Tile to, Maze.Direction d) {
    this.from = from;
    this.to = to;

    this.entity = entity;
    this.d = d;

    fromX = from.getCol() * getBlockSize();
    fromY = from.getRow() * getBlockSize();
    toX = to.getCol() * getBlockSize();
    toY = to.getRow() * getBlockSize();

    // System.out.println(fromX+ " "+toX);
    t.start();
  }

  public int getBlockSize() {
    return blockSize;
  }

  public void setAnimating(boolean animating) {
    isAnimating = animating;
    if (!animating) {
      t.stop();
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    repaint();
    fromX += velx;
    fromY += vely;

    if ((d == Maze.Direction.LEFT || d == Maze.Direction.RIGHT) && fromX == toX) {
      setAnimating(false);
    }
    if ((d == Maze.Direction.DOWN || d == Maze.Direction.UP) && fromY == toY) {
      setAnimating(false);
    }
  }

  public Maze getMaze() {
    return m;
  }
}
