package nz.ac.vuw.ecs.swen225.gp20.rendering;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import nz.ac.vuw.ecs.swen225.gp20.maze.*;



public class BoardView extends JComponent implements ActionListener {

  private Maze maze;

  private Tile[][] tiles;
  private int blockSize = 40;
  private int width;
  private int height;
  private int minPanel;
  private final int boardWidth = 650;
  private final int boardHeight = 650;

  private final int viewTiles = 3;

  // Stuff for animation
  private Tile from;

  private int windowSize = (2 * viewTiles) + 1;

  private int drawX;
  private int drawY;
  private Maze.Direction dir;

  int startCount = 0;

  Animation currentAnimation;

  private double velx;
  private double vely;

  List<Animation> animations = new ArrayList<>();
  List<Movable> entitesAnimated = new ArrayList<>();

  public boolean isAnimating = false;
  private final boolean isWindowed = true;
  private boolean boardNeedMove = false;

  Timer time = new Timer(5, this);

  SoundHandler sh;
  AnimationHandler ah;

  /**
   * Constructor for creating the boardView.
   *
   * @param m the maze that is being used
   */
  public BoardView(Maze m) {
    this.maze = m;
    tiles = m.getTiles();
    width = m.getWidth();
    height = m.getHeight();
    sh = new SoundHandler(m);
    ah = new AnimationHandler(m, this);

    if (windowSize > height || windowSize > width) {
      windowSize = Math.min(height, width);
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    minPanel = Math.min(boardHeight, boardWidth);


    if (isWindowed) {
      drawWindowedBoard(g);
    } else {
      drawWholeBoard(g);
    }

    if (isAnimating) {
      animate(g);
    }
  }

  /**
   * Draws the whole board.
   *
   * @param g the graphics it draws.
   */
  public void drawWholeBoard(Graphics g) {
    if (width > height) {
      blockSize = boardWidth / width;
    } else {
      blockSize = boardHeight / height;
    }

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        Tile t = maze.getTileAt(row, col);
        g.drawImage(getToolkit().getImage(t.getFilename()), col * blockSize, row * blockSize,
            blockSize, blockSize, this);
        if (t instanceof PathTile) {
          PathTile pt = (PathTile) t;
          if (!pt.getContainedEntities().isEmpty()) {
            for (Containable c : pt.getContainedEntities()) {
              if (isAnimating && c instanceof Movable && entitesAnimated.contains(c)) {
                continue;
              }
              try {
                g.drawImage(getToolkit().getImage(c.getFilename()),
                    col * blockSize, row * blockSize,
                    blockSize, blockSize, this);
              } catch (Exception e) {
                e.printStackTrace();
              }
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

    int chapRow = maze.getChap().getContainer().getRow();
    int chapCol = maze.getChap().getContainer().getCol();

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
        Tile t = maze.getTileAt(row, col);
        g.drawImage(getToolkit().getImage(t.getFilename()), currentCol * blockSize,
            currentRow * blockSize, blockSize, blockSize, this);
        if (t instanceof PathTile) {
          PathTile pt = (PathTile) t;
          if (!pt.getContainedEntities().isEmpty()) {
            for (Containable c : pt.getContainedEntities()) {
              g.drawImage(getToolkit().getImage(c.getFilename()),
                  currentCol * blockSize, currentRow * blockSize, blockSize, blockSize, this);
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
   *
   * @param g graphics object its drawing
   */
  public void animate(Graphics g) {

    if (dir == Maze.Direction.LEFT) {
      velx = -3;
      vely = 0;
    } else if (dir == Maze.Direction.UP) {
      vely = -3;
      velx = 0;
    } else if (dir == Maze.Direction.DOWN) {
      vely = 3.5;
      velx = 0;
    } else if (dir == Maze.Direction.RIGHT) {
      velx = 3.5;
      vely = 0;
    }

    if (!isWindowed || (from.getCol() <= viewTiles && from.getRow() <= viewTiles)) {
      for (Animation a : animations) {
        g.drawImage(getToolkit().getImage(a.getM().getFilename()),
            a.getFromX(), a.getFromY(), blockSize, blockSize, this);
      }
    }

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

    this.dir = d;

    if (!isWindowed) {
      int fromX = from.getCol() * getBlockSize();
      int fromY = from.getRow() * getBlockSize();
      int toX = to.getCol() * getBlockSize();
      int toY = to.getRow() * getBlockSize();

      entitesAnimated.add(entity);
      animations.add(new Animation(entity, fromX, toX, fromY, toY, d));
      time.start();
    }


  }

  /**
   * Starts the set of animations running and starts the timer.
   *
   * @param animating if it is animating or not. Either true or false.
   */
  public void setAnimating(boolean animating) {
    isAnimating = animating;
    if (!animating) {
      animations.clear();
      entitesAnimated.clear();
      startCount = 0;
      time.stop();
      //System.out.println("Animation Stopped");
    }
  }

  /**
   * Checks if the animation is still running.
   */
  public void checkAnimation() {
    if (animations.isEmpty() || startCount > blockSize) {
      setAnimating(false);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    repaint();
    if (!boardNeedMove) {
      for (int i = 0; i < animations.size(); i++) {
        currentAnimation = animations.get(i);
        double currentFromX = currentAnimation.getFromX();
        double currentFromY = currentAnimation.getFromY();
        currentAnimation.setFromX(currentFromX + velx);
        currentAnimation.setFromY(currentFromY + vely);

        if ((dir == Maze.Direction.LEFT && currentAnimation.getFromX() <= currentAnimation.getToX())
            || (dir == Maze.Direction.RIGHT && currentAnimation.getFromX() >= currentAnimation.getToX())) {
          entitesAnimated.remove(currentAnimation.getM());
          animations.remove(currentAnimation);
          checkAnimation();
        }
        if ((dir == Maze.Direction.UP && currentAnimation.getFromY() <= currentAnimation.getToY())
            || (dir == Maze.Direction.DOWN && currentAnimation.getFromY() >= currentAnimation.getToY())) {
          entitesAnimated.remove(currentAnimation.getM());
          animations.remove(currentAnimation);
          checkAnimation();
        }
      }
    } else if (startCount > blockSize) {
      checkAnimation();
    } else {
      drawX += velx;
      drawY += vely;
    }
  }

  public Maze getMaze() {
    return maze;
  }

  public int getBlockSize() {
    return blockSize;
  }

  /**
   * Resets the board and maze.
   *
   * @param maze the maze its resetting.
   */
  public void reset(Maze maze) {
    this.maze = maze;

    tiles = maze.getTiles();
    width = maze.getWidth();
    height = maze.getHeight();
    sh = new SoundHandler(maze);
    ah = new AnimationHandler(maze, this);
    repaint();
  }

}
