package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEvent;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventEnemyWalked;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventEnemyWalkedKilled;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventExitUnlocked;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventInfoField;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventListener;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventPickup;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventPushed;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventPushedWater;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventTeleported;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventUnlocked;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventWalked;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventWalkedDrowned;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventWalkedKilled;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventWon;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.Move;


/**
 * The Maze board, which keeps track of the Tiles and logic in the game.
 *
 * @author Ian 300474717
 */

public class Maze {
  // TODO: fix all the comments, outdated because of rewrite

  // Enums
  /**
   * Enums for directions.
   * @author Ian 300474717
   *
   */
  public enum Direction {
    /**
     * Up.
     */
    UP, 
    
    /**
     * Down.
     */
    DOWN, 
    
    /**
     * Left.
     */
    LEFT, 
    
    /**
     * Right.
     */
    RIGHT
  }

  /**
   * Enum for Key Colors.
   * 
   * @author Ian 300474717
   */
  public enum KeyColor {
    /**
     * Blue (Wood).
     */
    BLUE, 
    
    /**
     * Red (Iron).
     */
    RED, 
    
    /**
     * Green (Diamond).
     */
    GREEN, 
    
    /**
     * Yellow (Gold).
     */
    YELLOW
  }

  // Board and Entities
  private int width;
  private int height;
  private Tile[][] tiles;
  private Chap chap;
  private int numTreasures;
  private List<Treasure> treasures = new ArrayList<Treasure>();
  private List<Enemy> enemies = new ArrayList<Enemy>();
  private ExitLock exitlock;


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

  // FIXME REMOVE THIS COLLECTION
  private List<Direction> moves = new ArrayList<>();

  // FIXME test for recnplay
  private Map<Long, List<Move>> movesByTime;

  /**
   * Set the moves by time.
   * 
   * @param m New moves by time.
   */
  public void setMovesByTime(Map<Long, List<Move>> m) {
    movesByTime = m;
  }

  /**
   * Get the moves by time.
   * 
   * @return The moves by time.
   */
  public Map<Long, List<Move>> getMovesByTime() {
    return movesByTime;
  }

  @JsonIgnore
  private UndoHandler undoredo = new UndoHandler(this, false);

  private boolean dead = false;

  /**
   * Empty constructor for Persistence.
   */
  public Maze() {
  }

  /**
   * Construct Board from predetermined Tiles.
   *
   * @param t        2d Array of Tiles.
   * @param entities Entities to be placed.
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
        if (t instanceof PathTile) {
          if (((PathTile) t).getContainedEntities().isEmpty()) {
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
   * @param listener the object which will listen to events.
   */
  public void addListener(MazeEventListener listener) {
    listeners.add(listener);
  }

  /**
   * Remove a listener.
   * @param l The listener to remove. 
   */
  public void removeListener(MazeEventListener l) {
    listeners.remove(l);
  }

  /**
   * Broadcast MazeEvents to any MazeEventListeners we may have.
   * @param event The event to broadcast.
   */
  public void broadcast(MazeEvent event) {
    for (MazeEventListener listener : listeners) {
      event.receive(listener);
      listener.update(event);
    }
  }

  /**
   * Replace the event to be dispatched if the new event is a subclass of it.
   *
   * @param event The new event.
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
      chap.changeFile(chap.getLeft());
    }
    if (d == Direction.RIGHT) {
      chap.changeFile(chap.getRight());
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

  private void preMoveChecks() {
    Preconditions.checkArgument(!dead, "Chap is dead, cannot move");
  }

  private void postMoveChecks() {
    // chap can only ever stand on a PathTile
    assert (chap.getContainer() instanceof PathTile); 

    // the PathTile Chap is on can only ever be blocked by Chap
    assert (chap.getContainer().getBlocker() instanceof Chap); 
    
    // total treasures is constant
    assert (treasures.size() + chap.getTreasures().size() == numTreasures); 
  }

  /**
   * Move Chap to a new PathTile.
   * @param next  the new PathTile.
   */
  public void moveChap(PathTile next) {
    next.moveTo(chap);
    undoredo.clearRedoStack();
  }

  /**
   * Check the next PathTile for any entities.
   * 
   * @param current The current PathTile.
   * @param next The next PathTile.
   * @param d The direction moved.
   */
  public void checkWalked(PathTile current, PathTile next, Direction d) {
    if (next.getContainedEntities().isEmpty()) {
      return;
    }
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

  private void checkPickup(PathTile current, PathTile next, Direction d, Pickup p) {
    if (p instanceof Treasure) {
      checkTreasure(next, current, d, (Treasure) p);
    }
    overrideDispatch(new MazeEventPickup(this, current, next, d, p));
  }

  private void checkTreasure(PathTile current, PathTile next, Direction d, Treasure t) {
    if (treasures.isEmpty()) {
      overrideDispatch(
          new MazeEventExitUnlocked(this, current, next, d, t, exitlock, exitlock.getContainer()));
      openExitLock();
    }
  }

  private boolean checkBlocking(PathTile current, PathTile blocked, Direction d) {
    BlockingContainable bc = blocked.getBlocker();
    if (bc instanceof Door) {
      return tryUnlockDoor(current, blocked, (Door) bc, d);
    } else if (bc instanceof Crate) {
      return tryPushCrate(current, blocked, (Crate) bc, d);
    } else if (bc instanceof Enemy) {
      killChap();
      overrideDispatch(new MazeEventWalkedKilled(this, (Enemy) bc, current, blocked, d));
    } else if (bc instanceof Water) {
      killChap();
      overrideDispatch(new MazeEventWalkedDrowned(this, (Water) bc, current, blocked, d));
      return true;
    }
    return false;
  }

  private boolean tryUnlockDoor(PathTile current, PathTile next, Door door, Direction d) {
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

  private boolean tryPushCrate(PathTile current, PathTile next, Crate c, Direction d) {
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

  /**
   * Get the Tile to a certain direction of a given one.
   * @param t The given Tile.
   * @param d The direction to check.
   * @return  The next tile in specified direction.
   */
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
   * Disable or enable path finding.
   * @param set Whether to enable or disable path finding.
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
          if (!doPathFinding) {
            return;
          }
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

  /**
   * Move an Enemy.
   * @param e The Enemy to move.
   * @param next  The direction to move.
   */
  public void moveEnemy(Enemy e, Direction next) {
    if (next == null) {
      return;
    }
    PathTile pt = (PathTile) tileTo(e.getContainer(), next);
    if (pt.equals(chap.getContainer())) {
      killChap();
      broadcast(new MazeEventEnemyWalkedKilled(this, e, e.getContainer(), pt, next));
    } else {
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
  
  /**
   * Get a Tile at a specified row and column on the board.
   * @param row The specified row.
   * @param col The specified column.
   * @return  The tile at the position.
   */
  public Tile getTileAt(int row, int col) {
    return tiles[row][col];
  }

  /**
   * Get the Chap entity.
   * @return  The Chap entity.
   */
  public Chap getChap() {
    return chap;
  }

  /**
   * Get the Tiles of the board.
   * @return  The Tiles of the board.
   */
  public Tile[][] getTiles() {
    return tiles;
  }

  /**
   * Get whether the level is finished or not.
   * @return  Whether the level is finished or not.
   */
  public boolean isLevelFinished() {
    return levelFinished;
  }

  /**
   * Get the board width.
   * @return  The board width.
   */
  public int getWidth() {
    return width;
  }

  /**
   * Get the board height.
   * @return  The board height.
   */
  public int getHeight() {
    return height;
  }

  /**
   * Get the list of Treasures on the board.
   * @return  The Treasures on the board.
   */
  public List<Treasure> getTreasures() {
    return treasures;
  }

  /**
   * Get the number of Treasures on the board.
   * @return  The number of Treasures on the board.
   */
  public int numTreasures() {
    return treasures.size();
  }

  /**
   * Open the ExitLock.
   */
  public void openExitLock() {
    exitlock.getContainer().remove(exitlock);
  }

  /**
   * Kill Chap and pause the game.
   */
  public void killChap() {
    pause();
    dead = true;
  }

  /**
   * Get the ms left.
   * @return The ms left.
   */
  public long getMillisecondsLeft() {
    return millisecondsLeft;
  }

  /**
   * Set the ms left.
   * @param ms  New value for the ms left.
   */
  public void setMillisecondsLeft(long ms) {
    this.millisecondsLeft = ms;
  }

  /**
   * Get the level ID.
   * @return  Current level ID.
   */
  public int getLevelID() {
    return levelID;
  }

  /**
   * Set the level ID.
   * @param levelID New value for the level ID.
   */
  public void setLevelID(int levelID) {
    this.levelID = levelID;
  }

  /**
   * Get the list of moves.
   * @return  Current list of moves.
   */
  public List<Direction> getMoves() {
    return moves;
  }

  /**
   * Set the list of moves.
   * @param moves New value for list of moves.
   */
  public void setMoves(List<Direction> moves) {
    this.moves = moves;
  }

  /**
   * Set width.
   * @param width New width.
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * Set height.
   * @param height  New height.
   */
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * Set Tiles.
   * @param tiles New Tiles.
   */
  public void setTiles(Tile[][] tiles) {
    this.tiles = tiles;
  }

  /**
   * Set Chap.
   * @param chap New Chap.
   */
  public void setChap(Chap chap) {
    this.chap = chap;
  }

  /**
   * Set Treasures.
   * @param treasures New Treasures.
   */
  public void setTreasures(List<Treasure> treasures) {
    this.treasures = treasures;
    this.numTreasures = treasures.size();
  }

  /**
   * Get Enemies.
   * @return Current Enemies.
   */
  public List<Enemy> getEnemies() {
    return enemies;
  }

  /**
   * Set Enemies.
   * @param enemies New Enemies.
   */
  public void setEnemies(List<Enemy> enemies) {
    this.enemies = enemies;
  }

  /**
   * Get ExitLock.
   * @return  Current ExitLock.
   */
  public ExitLock getExitlock() {
    return exitlock;
  }

  /**
   * Set ExitLock.
   * @param exitlock New ExitLock.
   */
  public void setExitlock(ExitLock exitlock) {
    this.exitlock = exitlock;
  }

  /**
   * Set Level Finished.
   * @param levelFinished New Level Finished.
   */
  public void setLevelFinished(boolean levelFinished) {
    this.levelFinished = levelFinished;
  }

  /**
   * Get Path Finding timer.
   * @return  current Path Finding timer. 
   */
  @JsonIgnore
  public Timer getPathFindingTimer() {
    return pathFindingTimer;
  }

  /**
   * Set new Path Finding timer.
   * @param timer new Path Finding timer.
   */
  @JsonIgnore
  public void setPathFindingTimer(Timer timer) {
    this.pathFindingTimer = timer;
  }

  /**
   * Get Path Finding delay.
   * @return  current Path Finding delay.
   */
  public int getPathFindingDelay() {
    return pathFindingDelay;
  }

  /**
   * Set Path Finding delay.
   * @param pathFindingDelay  new Path Finding delay.
   */
  public void setPathFindingDelay(int pathFindingDelay) {
    this.pathFindingDelay = pathFindingDelay;
  }

  /**
   * Get Listeners.
   * @return  Current Listeners.
   */
  public List<MazeEventListener> getListeners() {
    return listeners;
  }

  /**
   * Set Listeners.
   * @param listeners new Listeners.
   */
  public void setListeners(List<MazeEventListener> listeners) {
    this.listeners = listeners;
  }

  /**
   * Get Undo handler.
   * @return Current Undo handler.
   */
  public UndoHandler getUndoRedo() {
    return undoredo;
  }

  /**
   * Get if Chap is dead.
   * @return  if Chap is dead.
   */
  public boolean isDead() {
    return dead;
  }
}
