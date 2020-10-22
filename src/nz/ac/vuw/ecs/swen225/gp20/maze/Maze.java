package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;

import nz.ac.vuw.ecs.swen225.gp20.maze.event.*;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.Move;

import java.util.*;

/**
 * The Maze board, which keeps track of the Tiles and logic in the game
 *
 * @author Ian 300474717
 */

public class Maze {
  // TODO: fix all the comments, outdated because of rewrite

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
  private int numTreasures;
  private List<Treasure> treasures = new ArrayList<Treasure>();
  private List<Enemy> enemies = new ArrayList<Enemy>();

  // Logic
  private boolean levelFinished = false;
  private long millisecondsLeft = 60000;
  private int levelID;
  private Timer pathFindingTimer;
  private int pathFindingDelay = 1000; // delay between path finding ticks in ms
  private boolean doPathFinding = true;

  // Output
  @JsonIgnore
  private List<MazeEventListener> listeners = new ArrayList<>();

  private MazeEventWalked dispatch;

  //FIXME REMOVE THIS COLLECTION
  private List<Direction> moves = new ArrayList<>();

  //FIXME test for recnplay
  private Map<Long, List<Move>> movesByTime;
  public void setMovesByTime(Map<Long, List<Move>> m) {
    movesByTime = m;
  }
  public Map<Long, List<Move>> getMovesByTime() {
    return movesByTime;
  }


  @JsonIgnore
  private UndoHandler undoredo = new UndoHandler(this, false);

  private boolean dead = false;

  /**
   * Instantiates a new Maze. For Jackson.
   */
  public Maze() {
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
    numTreasures = treasures.size();
    setupTimer();
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    for (Tile[] rows : tiles) {
      for (Tile t : rows) {
        String a = t.getInitials();
        if(t instanceof PathTile) {
          if(((PathTile)t).getContainedEntities().isEmpty()){
            a = "__";
          }
        }
        s.append(a);
        s.append(" ");
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
  public void addListener(MazeEventListener listener) {
    listeners.add(listener);
  }
  
  /**
   * Remove a listener
   */
  public void removeListener(MazeEventListener l) {
    listeners.remove(l);
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
    if (dispatch == null) {
      dispatch = event;
    } else {
      if (dispatch.getClass().isAssignableFrom(event.getClass())) {
        dispatch = event;
      }
    }
  }

  /**
   * Try to move in a direction.
   *
   * @param d the direction
   */
  public void move(Direction d) {
    preMoveChecks();
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
        moveChap(next);
        checkWalked(current, next, d);
        overrideDispatch(new MazeEventWalked(this, current, next, d));
      } else {
        if (checkBlocking(current, next, d)) {
          moveChap(next);
        }
      }
    }
    postMoveChecks();
    if (dispatch != null) {
      broadcast(dispatch);
      dispatch = null;
    }
  }

  public void preMoveChecks() {
    Preconditions.checkArgument(!dead, "Chap is dead, cannot move");
  }

  /**
   * Postcondition checks after moving
   */
  public void postMoveChecks() {
    assert(chap.getContainer() instanceof PathTile); // chap can only ever stand on a PathTile
    assert(chap.getContainer().getBlocker() instanceof Chap); // the PathTile Chap is on can only ever be blocked by Chap
    assert(treasures.size()+chap.getTreasures().size()==numTreasures); // total treasures is constant
  }

  public void moveChap(PathTile next) {
    next.moveTo(chap);
    undoredo.clearRedoStack();
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
    } else if (p instanceof Key) {

    }
    overrideDispatch(new MazeEventPickup(this, current, next, d, p));
  }

  public void checkTreasure(PathTile current, PathTile next, Direction d, Treasure t) {
    if (treasures.isEmpty()) {
      overrideDispatch(new MazeEventExitUnlocked(this, current, next, d, t, exitlock, exitlock.getContainer()));
      openExitLock();
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
      return tryUnlockDoor(current, blocked, (Door) bc, d);
    } else if (bc instanceof Crate) {
      return tryPushCrate(current, blocked, (Crate) bc, d);
    } else if (bc instanceof Enemy) {
      killChap();
      overrideDispatch(new MazeEventWalkedKilled(this,(Enemy)bc, current, blocked, d));
    } else if (bc instanceof Water) {
      killChap();
      overrideDispatch(new MazeEventWalkedDrowned(this,(Water)bc, current, blocked, d));
      return true;
    }
    return false;
  }


  /**
   * Check if we could open a door.
   *
   * @param door the door to check
   * @param d    the direction we moved to
   * @return the event if we opened the door and moved, null if we didn't
   */
  public boolean tryUnlockDoor(PathTile current, PathTile next, Door door, Direction d) {
    Key key = chap.hasMatchingKey(door);
    if (key != null) {
      door.getContainer().remove(door);
      // Green key may be used unlimited times
      if (!key.getColor().equals(KeyColor.GREEN)) {
        chap.getKeys().remove(key);
      }
      overrideDispatch(new MazeEventUnlocked(this, current, next, d, door, key));
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
  public boolean tryPushCrate(PathTile current, PathTile next, Crate c, Direction d) {
    Tile destination = tileTo(c.container, d);
    // if the tile we try to push to is a pathtile
    if (destination instanceof PathTile) {
      PathTile pt = (PathTile) destination;
      PathTile original = next;
      // if the pathtile we try to push to is free, push the crate
      if (pt.isWalkable()) {
        pt.moveTo(c);
        overrideDispatch(new MazeEventPushed(this, current, original, d, c));
        // can also push crate onto water to make a path
      } else if (pt.getBlocker() instanceof Water) {
        Water w = (Water) pt.getBlocker();
        pt.remove(w);
        c.getContainer().remove(c);
        overrideDispatch(new MazeEventPushedWater(this, current, original, d, c, w));
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
   * Disable or enable pathfinding
   */
  public void setDoPathfinding(boolean set) {
    doPathFinding = set;
  }

  /**
   * Setup the timer, but only if it's needed.
   */
  public void setupTimer() {
    if (!enemies.isEmpty()) {
      pathFindingTimer = new Timer();
      pathFindingTimer.schedule(new TimerTask() {

        @Override
        public void run() {
          if(!doPathFinding) return;
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
      moveEnemy(e, next);
    }
  }
  
  public void moveEnemy(Enemy e, Direction next) {
    if(next==null)return;
    PathTile pt = (PathTile) tileTo(e.getContainer(), next);
    if(pt.equals(chap.getContainer())) {
      killChap();
      broadcast(new MazeEventEnemyWalkedKilled(this, e, e.getContainer(), pt, next));
    }else {
      broadcast(new MazeEventEnemyWalked(this, e, e.getContainer(), pt, next));
      pt.moveTo(e);
    }
  }

  /**
   * Pause the game (suspending the game timer).
   */
  public void pause() {
    if (pathFindingTimer != null) {
      pathFindingTimer.cancel();
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

  public void killChap() {
    pause();
    dead = true;
  }

  public long getMillisecondsLeft() {
    return millisecondsLeft;
  }

  public void setMillisecondsLeft(long ms) {
    this.millisecondsLeft = ms;
  }
  public int getLevelID() {
    return levelID;
  }
  public void setLevelID(int levelID) {
    this.levelID = levelID;
  }

  public void setMoves(List<Direction> moves) {
    this.moves = moves;
  }
  public List<Direction> getMoves() {
    return moves;
  }

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
    this.numTreasures = treasures.size();
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
  public Timer getPathFindingTimer() {
    return pathFindingTimer;
  }

  @JsonIgnore
  public void setPathFindingTimer(Timer timer) {
    this.pathFindingTimer = timer;
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

  public UndoHandler getUndoRedo() {
    return undoredo;
  }
  
  public boolean isDead() {
    return dead;
  }
}
