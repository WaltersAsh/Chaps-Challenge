package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.maze.event.*;

import java.util.*;

/**
 * The Maze board, which keeps track of the Tiles and logic in the game
 *
 * @author Ian 300474717
 *
 */

public class Maze {
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

  // Logic
  private boolean levelFinished = false;
  private Timer timer;
  private int pathFindingDelay = 500; // delay between path finding ticks in ms

  // Output
  private List<MazeEventListener> listeners = new ArrayList<>();

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
   * @param <L>       any type which extends MazeEventListener
   * @param listener  the object which will listen to events
   */
  public <L extends MazeEventListener> void addListener(L listener) {
    listeners.add(listener);
  }

  /**
   * Broadcast MazeEvents to any MazeEventListeners we may have.
   * @param event
   */
  public void broadcast(MazeEvent event) {
    for (MazeEventListener listener : listeners) {
      event.receive(listener);
      listener.update(event);
    }
  }

  /**
   * Try to move in a direction.
   *
   * @param d   the direction
   */
  public void move(Direction d) {
    MazeEvent event;
    PathTile current = chap.getContainer();
    Tile next = tileTo(current, d);
    if (d == Direction.LEFT) {
      chap.changeFile(chap.left);
    }
    if (d == Direction.RIGHT) {
      chap.changeFile(chap.right);
    }

    if (next instanceof PathTile) {
      PathTile ptnext = (PathTile) next;
      event = new MazeEventWalked(this, current, ptnext, d);
      if (!ptnext.isWalkable()) {
        event = checkBlocking(ptnext, d);
        if (event == null) {
          // we did not move
          return;
        }
      }

      MazeEvent checkEntitiesEvent = checkEntities(ptnext, d);
      if (checkEntitiesEvent != null) {
        // should never override a MazeEventUnlocked or a MazeEventPushed
        // because you can never pick something up in the same move as these
        event = checkEntitiesEvent;
        if(event instanceof MazeEventTeleported) {
          MazeEventTeleported t = (MazeEventTeleported) event;
          ptnext = t.getTeleporter().getOther().getContainer();
        }
      }

      ptnext.moveTo(chap);
      broadcast(event);

    }
  }

  /**
   * Check if we can move onto a b PathTile
   *
   * We could move onto it if we had the matching key to the blocking door.
   *
   * @param blocked   the PathTile to check
   * @return          the event for moving to it if we did
   */
  public MazeEvent checkBlocking(PathTile blocked, Direction d) {
    BlockingContainable bc = blocked.getBlocker();
    if (bc instanceof Door) {
      return tryUnlockDoor((Door) bc, d);
    } else if (bc instanceof Crate) {
      return tryPushCrate((Crate) bc, d);
    }
    return null;
  }

  /**
   * Check if we could open a door.
   *
   * @param door  the door to check
   * @param d     the direction we moved to
   * @return      the event if we opened the door and moved, null if we didn't
   */
  public MazeEventUnlocked tryUnlockDoor(Door door, Direction d) {
    Key key = chap.hasMatchingKey(door);
    if (key != null) {
      door.getContainer().remove(door);
      // Green key may be used unlimited times
      if (!key.getColor().equals(KeyColor.GREEN)) {
        chap.getKeys().remove(key);
      }
      return new MazeEventUnlocked(this, chap.container, door.container, d, door, key);
    }
    return null;
  }

  /**
   * Check and pickup entities on the next PathTile.
   *
   * @param path  the tile to check
   * @param d     the direction we moved
   * @return      the event for picking up entity, if any
   */
  public MazeEvent checkEntities(PathTile path, Direction d) {
    for (Containable e : path.getContainedEntities()) {
      if (e instanceof InfoField) {
        return new MazeEventInfoField(this, chap.container, path, d, (InfoField) e);
      } else if (e instanceof Exit) {
        pause();
        return new MazeEventWon(this, chap.container, path, d);
      }else if (e instanceof Teleporter) {
        return new MazeEventTeleported(this, chap.container, path, d, (Teleporter)e);
      } else if (e instanceof Pickup) {
        Pickup p = (Pickup) e;
        p.addToInventory(chap);
        if(p instanceof Treasure) {
          if (chap.hasAllTreasures(this)) {
            openExitLock();
            return new MazeEventExitUnlocked(this, chap.container, path, d, p, exitlock);
          }
        }
        // should never be able to pick up more than one thing in one go.
        return new MazeEventPickup(this, chap.container, path, d, p);
      }
    }
    return null;
  }

  /**
   * Try to push a crate.
   *
   * @param c the crate to push
   * @param d the direction in which to push
   * @return the MazeEvent for pushing the crate
   */
  public MazeEventPushed tryPushCrate(Crate c, Direction d) {
    Tile destination = tileTo(c.container, d);
    // if the tile we try to push to is a pathtile
    if (destination instanceof PathTile) {
      PathTile pt = (PathTile) destination;
      PathTile original = c.getContainer();
      // if the pathtile we try to push to is free, push the crate
      if (pt.isWalkable()) {
        pt.moveTo(c);
        // can also push crate onto water to make a path
        return new MazeEventPushed(this, chap.container, original, d, c);
      } else if (pt.getBlocker() instanceof Water) {
        pt.remove(pt.getBlocker());
        c.getContainer().remove(c);
        return new MazeEventPushedWater(this, chap.container, original, d, c);
      }
    }
    return null;
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
    if(!enemies.isEmpty()) {
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
      if(next==null) continue;
      PathTile pt = (PathTile) tileTo(e.getContainer(), next);
      broadcast(new MazeEventEnemyWalked(this, e, e.getContainer(), pt, next));
      pt.moveTo(e);
    }
  }

  /**
   * Pause the game (suspending the game timer).
   */
  public void pause() {
    if(timer!=null) {
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
