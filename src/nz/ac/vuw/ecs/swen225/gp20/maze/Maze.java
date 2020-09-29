package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The Maze board, which keeps track of the Tiles and logic in the game
 *
 * @author Ian 300474717
 */

public class Maze {
  // TODO: fix all the comments, outdated because of rewrite

  /**
   * Instantiates a new Maze. For Jackson.
   */
  public Maze() {
  }

  // Enums
  public enum Direction {
    UP, DOWN, LEFT, RIGHT
  }

  public enum KeyColor {
    BLUE, RED, GREEN, YELLOW
  }

  // Board and Entities
  private int width, height;
  private Tile[][] tiles;
  private Chap chap;
  private List<Treasure> treasures = new ArrayList<Treasure>();
  private List<Enemy> enemies = new ArrayList<Enemy>();
  private ExitLock exitlock;

  public void setWidth(int width) {
    this.width = width;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public void setTiles(Tile[][] tiles) {
    this.tiles = tiles;
  }

  public void setChap(Chap chap) {
    this.chap = chap;
  }

  public void setTreasures(List<Treasure> treasures) {
    this.treasures = treasures;
  }

  public List<Enemy> getEnemies() {
    return enemies;
  }

  public void setEnemies(List<Enemy> enemies) {
    this.enemies = enemies;
  }

  public ExitLock getExitlock() {
    return exitlock;
  }

  public void setExitlock(ExitLock exitlock) {
    this.exitlock = exitlock;
  }

  public void setLevelFinished(boolean levelFinished) {
    this.levelFinished = levelFinished;
  }

  @JsonIgnore
  public Timer getTimer() {
    return timer;
  }

  @JsonIgnore
  public void setTimer(Timer timer) {
    this.timer = timer;
  }

  public int getPathFindingDelay() {
    return pathFindingDelay;
  }

  public void setPathFindingDelay(int pathFindingDelay) {
    this.pathFindingDelay = pathFindingDelay;
  }

  public List<MazeEventListener> getListeners() {
    return listeners;
  }

  public void setListeners(List<MazeEventListener> listeners) {
    this.listeners = listeners;
  }

  // Logic
  private boolean levelFinished = false;
  private Timer timer;
  private int pathFindingDelay = 500; // delay between path finding ticks in ms

  // Output
  @JsonIgnore
  private List<MazeEventListener> listeners = new ArrayList<>();

  private MazeEventWalked dispatch;

  /**
   * Constuct empty Board with a width and height
   *
   * @param width  width of board
   * @param height height of board
   */
  public Maze(int width, int height) {
    tiles = new Tile[width][height];
    throw new UnsupportedOperationException("Not implemented.");
  }

  /**
   * Construct Board from predetermined Tiles
   *
   * @param t        Tile[][] of Tiles
   * @param entities what entities need to be placed
   */
  public Maze(Tile[][] t, List<Containable> entities) {
    tiles = t;

    height = tiles.length;
    width = tiles[0].length;

    for (Containable c : entities) {
      if (c instanceof Treasure) {
        treasures.add((Treasure) c);
      } else if (c instanceof Chap) {
        chap = (Chap) c;
      } else if (c instanceof ExitLock) {
        exitlock = (ExitLock) c;
      } else if (c instanceof Enemy) {
        Enemy e = (Enemy) c;
        e.initPathFinder(this);
        enemies.add(e);
      }
    }

    setupTimer();
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    for (Tile[] rows : tiles) {
      for (Tile t : rows) {
        s.append(t.getInitials());
      }
      s.append("\n");
    }
    return s.toString();
  }

  /**
   * Add a listener which will respond to MazeEvents produced by this Maze.
   *
   * @param <L>      any type which extends MazeEventListener
   * @param listener the object which will listen to events
   */
  public <L extends MazeEventListener> void addListener(L listener) {
    listeners.add(listener);
  }

  /**
   * Broadcast MazeEvents to any MazeEventListeners we may have.
   *
   * @param event
   */
  public void broadcast(MazeEvent event) {
    for (MazeEventListener listener : listeners) {
      event.receive(listener);
      listener.update(event);
    }
  }

  /**
   * Replace the event to be dispatched if the new event is a subclass of it
   *
   * @param event the new event
   */
  private void overrideDispatch(MazeEventWalked event) {
    // System.out.printf("trying to override current dispatch %s with new %s\n", dispatch, event);
    if (dispatch == null) {
      dispatch = event;
    } else {
      if (dispatch.getClass().isAssignableFrom(event.getClass())) {
        dispatch = event;
      }
    }
    // System.out.printf("dispatch = %s\n", dispatch);
  }

  /**
   * Try to move in a direction.
   *
   * @param d the direction
   */
  public void move(Direction d) {
    if (d == Direction.LEFT) {
      chap.changeFile(chap.left);
    }
    if (d == Direction.RIGHT) {
      chap.changeFile(chap.right);
    }

    PathTile current = chap.getContainer();
    Tile check = tileTo(current, d);

    if (check instanceof PathTile) {
      PathTile next = (PathTile) check;

      if (next.isWalkable()) {
        next.moveTo(chap);
        checkWalked(current, next, d);
        overrideDispatch(new MazeEventWalked(this, current, next, d));
      } else {
        if (checkBlocking(current, next, d)) {
          next.moveTo(chap);
        }
      }
    }

    if (dispatch != null) {
      broadcast(dispatch);
      dispatch = null;
    }
  }

  public void checkWalked(PathTile current, PathTile next, Direction d) {
    if (next.getContainedEntities().isEmpty()) return;
    for (int i = next.getContainedEntities().size() - 1; i >= 0; i--) {
      Containable c = next.getContainedEntities().get(i);
      c.onWalked(this);
      if (c instanceof Pickup) {
        checkPickup(current, next, d, (Pickup) c);
      } else if (c instanceof Trigger) {
        checkTrigger(current, next, d, (Trigger) c);
      }
    }
  }

  private void checkTrigger(PathTile current, PathTile next, Direction d, Trigger t) {
    if (t instanceof InfoField) {
      overrideDispatch(new MazeEventInfoField(this, current, next, d, (InfoField) t));
    } else if (t instanceof Exit) {
      overrideDispatch(new MazeEventWon(this, current, next, d));
      pause();
    } else if (t instanceof Teleporter) {
      Teleporter tp = (Teleporter) t;
      overrideDispatch(new MazeEventTeleported(this, current, next, d, tp));
    }
  }

  public void checkPickup(PathTile current, PathTile next, Direction d, Pickup p) {
    if (p instanceof Treasure) {
      checkTreasure(next, current, d, (Treasure) p);
      if (chap.hasAllTreasures(this)) {
        openExitLock();
        overrideDispatch(new MazeEventExitUnlocked(this, current, next, d, p, exitlock));
      }
    } else if (p instanceof Key) {

    }
    overrideDispatch(new MazeEventPickup(this, current, next, d, p));
  }

  public void checkTreasure(PathTile current, PathTile next, Direction d, Treasure t) {

    if (chap.hasAllTreasures(this)) {
      openExitLock();
      overrideDispatch(new MazeEventExitUnlocked(this, current, next, d, t, exitlock));
    }
  }

  /**
   * Check if we can move onto a b PathTile
   * <p>
   * We could move onto it if we had the matching key to the blocking door.
   *
   * @param blocked the PathTile to check
   * @return the event for moving to it if we did
   */
  public boolean checkBlocking(PathTile current, PathTile blocked, Direction d) {
    BlockingContainable bc = blocked.getBlocker();
    if (bc instanceof Door) {
      return tryUnlockDoor(current, (Door) bc, d);
    } else if (bc instanceof Crate) {
      return tryPushCrate(current, (Crate) bc, d);
    } else {
      return false;
    }
  }

  /**
   * Check if we could open a door.
   *
   * @param door the door to check
   * @param d    the direction we moved to
   * @return the event if we opened the door and moved, null if we didn't
   */
  public boolean tryUnlockDoor(PathTile current, Door door, Direction d) {
    Key key = chap.hasMatchingKey(door);
    if (key != null) {
      door.getContainer().remove(door);
      // Green key may be used unlimited times
      if (!key.getColor().equals(KeyColor.GREEN)) {
        chap.getKeys().remove(key);
      }
      overrideDispatch(new MazeEventUnlocked(this, current, door.container, d, door, key));
      return true;
    }
    return false;
  }

  /**
   * Try to push a crate.
   *
   * @param c the crate to push
   * @param d the direction in which to push
   * @return the MazeEvent for pushing the crate
   */
  public boolean tryPushCrate(PathTile current, Crate c, Direction d) {
    Tile destination = tileTo(c.container, d);
    // if the tile we try to push to is a pathtile
    if (destination instanceof PathTile) {
      PathTile pt = (PathTile) destination;
      PathTile original = c.getContainer();
      // if the pathtile we try to push to is free, push the crate
      if (pt.isWalkable()) {
        pt.moveTo(c);
        overrideDispatch(new MazeEventPushed(this, current, original, d, c));
        // can also push crate onto water to make a path
      } else if (pt.getBlocker() instanceof Water) {
        pt.remove(pt.getBlocker());
        c.getContainer().remove(c);
        overrideDispatch(new MazeEventPushedWater(this, current, original, d, c));
      } else {
        return false;
      }

      return true;
    }
    return false;
  }

  public Tile tileTo(Tile t, Direction d) {
    switch (d) {
      case DOWN:
        return tiles[t.row + 1][t.col];
      case LEFT:
        return tiles[t.row][t.col - 1];
      case RIGHT:
        return tiles[t.row][t.col + 1];
      case UP:
        return tiles[t.row - 1][t.col];
      default:
        return null;
    }
  }

  /**
   * Setup the timer, but only if it's needed.
   */
  public void setupTimer() {
    if (!enemies.isEmpty()) {
      timer = new Timer();
      timer.schedule(new TimerTask() {

        @Override
        public void run() {
          tickPathFinding();
        }
      }, 0, pathFindingDelay);
    }
  }

  /**
   * Tick the enemy path finding.
   */
  public void tickPathFinding() {
    for (Enemy e : enemies) {
      Direction next = e.tickPathFinding();
      if (next == null) continue;
      PathTile pt = (PathTile) tileTo(e.getContainer(), next);
      broadcast(new MazeEventEnemyWalked(this, e, e.getContainer(), pt, next));
      pt.moveTo(e);
    }
  }

  /**
   * Pause the game (suspending the game timer).
   */
  public void pause() {
    if (timer != null) {
      timer.cancel();
    }
  }

  /**
   * Resume the game.
   */
  public void resume() {
    setupTimer();
  }

  public Tile getTileAt(int row, int col) {
    return tiles[row][col];
  }

  public Chap getChap() {
    return chap;
  }

  public Tile[][] getTiles() {
    return tiles;
  }

  public boolean isLevelFinished() {
    return levelFinished;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public List<Treasure> getTreasures() {
    return treasures;
  }

  public int numTreasures() {
    return treasures.size();
  }

  public void openExitLock() {
    exitlock.getContainer().remove(exitlock);
  }
}
