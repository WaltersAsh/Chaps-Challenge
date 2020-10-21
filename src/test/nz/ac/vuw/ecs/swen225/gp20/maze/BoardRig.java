package test.nz.ac.vuw.ecs.swen225.gp20.maze;

import java.util.*;
import java.util.regex.*;

import com.google.common.base.Preconditions;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

/**
 * A testing class for setting up a text only Board
 *
 * @author Ian 300474717
 *
 *         WA = wall PA = path KX = key with #X LX = lock with #X TR = treasure
 *         IN = info square EL = exit lock EX = exit CH = chap XX = crate WT =
 *         water EN = enemy
 */

public class BoardRig {
  private static Pattern keyPat = Pattern.compile("K(.)");
  private static Pattern lockPat = Pattern.compile("L(.)");
  private static Pattern telePat = Pattern.compile("P(.)");

  public static void main(String[] args) {
    System.out.println(lesson1());
  }

  public static Maze lesson1() {
    String board = "PA PA PA PA PA PA PA PA PA PA PA PA PA PA PA PA PA\n"
        + "PA PA PA WA WA WA WA WA PA WA WA WA WA WA PA PA PA\n"
        + "PA PA PA WA PA PA PA WA WA WA PA PA PA WA PA PA PA\n"
        + "PA PA PA WA PA TR PA WA EX WA PA TR PA WA PA PA PA\n"
        + "PA WA WA WA WA WA LG WA EL WA LG WA WA WA WA WA PA\n"
        + "PA WA PA KY PA LB PA PA PA PA PA LR PA KY PA WA PA\n"
        + "PA WA PA TR PA WA KB PA IN PA KR WA PA TR PA WA PA\n"
        + "PA WA WA WA WA WA TR PA CH PA TR WA WA WA WA WA PA\n"
        + "PA WA PA TR PA WA KB PA PA PA KR WA PA TR PA WA PA\n"
        + "PA WA PA PA PA LR PA PA TR PA PA LB PA PA PA WA PA\n"
        + "PA WA WA WA WA WA WA LY WA LY WA WA WA WA WA WA PA\n"
        + "PA PA PA PA PA WA PA PA WA PA PA WA PA PA PA PA PA\n"
        + "PA PA PA PA PA WA PA TR WA TR PA WA PA PA PA PA PA\n"
        + "PA PA PA PA PA WA PA PA WA KG PA WA PA PA PA PA PA\n"
        + "PA PA PA PA PA WA WA WA WA WA WA WA PA PA PA PA PA\n"
        + "PA PA PA PA PA PA PA PA PA PA PA PA PA PA PA PA PA";
    return BoardRig.fromString(board);
  }

  public static Maze crateTest() {
    String board = "PA PA PA PA PA PA PA PA PA\n" + "PA WA WA WA WA WA WA WA PA\n"
        + "PA WA PA PA PA PA PA WA PA\n" + "PA WA PA PA CH PA PA WA PA\n"
        + "PA WA PA PA PA PA PA WA PA\n" + "PA WA PA PA PA PA PA WA PA\n"
        + "PA WA PA PA XX PA PA WA PA\n" + "PA WA PA PA PA PA PA WA PA\n"
        + "PA WA PA PA PA PA PA WA PA\n" + "PA WA WA WA WA WA WA WA PA\n"
        + "PA PA PA PA PA PA PA PA PA\n";
    return BoardRig.fromString(board);
  }

  public static Maze crateAndWaterTest() {
    String board = "PA PA PA PA PA PA PA PA PA\n" + "PA WA WA WA WA WA WA WA PA\n"
        + "PA WA PA PA PA EL EX WA PA\n" + "PA WA CH PA PA PA WA WA PA\n"
        + "PA WA PA PA XX PA PA WA PA\n" + "PA WA PA PA XX PA PA WA PA\n"
        + "PA WA WT WT WT WT WT WA PA\n" + "PA WA WT WT WT WT WT WA PA\n"
        + "PA WA TR PA PA PA TR WA PA\n" + "PA WA WA WA WA WA WA WA PA\n"
        + "PA PA PA PA PA PA PA PA PA\n";
    return BoardRig.fromString(board);
  }

  public static Maze pathFindTest1() {
    String board = "PA PA PA PA PA PA PA PA PA\n" + "PA WA WA WA WA WA WA WA PA\n"
        + "PA WA PA PA PA PA PA WA PA\n" + "PA WA PA PA CH PA PA WA PA\n"
        + "PA WA PA PA PA PA PA WA PA\n" + "PA WA PA PA PA PA PA WA PA\n"
        + "PA WA PA PA PA PA PA WA PA\n" + "PA WA PA PA PA PA PA WA PA\n"
        + "PA WA PA PA PA PA EN WA PA\n" + "PA WA WA WA WA WA WA WA PA\n"
        + "PA PA PA PA PA PA PA PA PA\n";
    return BoardRig.fromString(board);
  }

  public static Maze levelEditorTest1() {
    String board = "WA WA WA WA WA WA WA WA WA WA WA WA WA WA WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA WT WT PA WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA WT WT PA WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA WT WT PA WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA WT WT PA WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA WT WT PA WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA WT WT PA WA\n"
        + "WA PA PA PA PA PA CH PA PA PA PA WT WT PA WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA WT WT PA WA\n"
        + "WA WT WT WT WT WT WT WT WT WT WT WT WT WT WA\n"
        + "WA WT WT WT WT WT WT WT WT WT WT WT WT WT WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA WT WT PA WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA WT WT PA WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA WT WT PA WA\n"
        + "WA WA WA WA WA WA WA WA WA WA WA WA WA WA WA";
    return BoardRig.fromString(board);
  }

  public static Maze levelEditorTest2() {
    String board = "WA WA WA WA WA WA WA WA WA WA WA WA WA WA WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA PA PA PA WA\n"
        + "WA PA CH PA PA PA PA PA PA PA PA TR PA KY WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA PA PA PA WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA PA PA PA WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA PA PA PA WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA PA PA PA WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA PA PA PA WA\n"
        + "WA WA WA WA WA WA WA WA WA LY WA WA WA WA WA\n"
        + "WA TR TR TR TR TR PA PA PA PA PA PA PA PA WA\n"
        + "WA TR TR TR TR TR PA PA PA PA EN PA PA PA WA\n"
        + "WA TR TR TR TR TR PA PA PA PA PA PA PA PA WA\n"
        + "WA TR TR TR TR TR PA PA PA PA PA PA PA PA WA\n"
        + "WA TR TR TR TR TR PA PA PA PA PA PA PA PA WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA PA PA PA WA\n"
        + "WA PA EN PA PA PA PA PA PA PA EN PA PA PA WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA PA PA KG WA\n"
        + "WA LG WA WA WA WA WA WA WA WA WA WA WA WA WA\n"
        + "WA PA PA PA PA PA PA PA PA PA PA PA EL EX WA\n"
        + "WA WA WA WA WA WA WA WA WA WA WA WA WA WA WA";
    return BoardRig.fromString(board);
  }

  public static Maze levelEditorTest3() {
    String board = "WA WA WA WA WA WA WA WA WA WA WA WA WA WA\n"+
        "WA EN PA PA WA KG PA PA LY PA PA PA EN WA\n"+
        "WA PA TR PA WA WA EL WA WA PA PA PA PA WA\n"+
        "WA PA PA PA WA PA EX PA WA WT WT WT WT WT\n"+
        "WA WA LG WA WA WA WA WA WA PA PA PA PA WA\n"+
        "WA PA KB PA WA PA PA PA WA PA KY XX PA WA\n"+
        "WA PA PA PA WA PA TR PA WA PA PA PA PA WA\n"+
        "WA WT WT WT WA PA PA PA LB PA PA PA PA WA\n"+
        "WA WT WT WT WA PA PA PA WA WA WA WA WA WA\n"+
        "WA XX PA PA WA PA PA PA PA WT WT WA TR WA\n"+
        "WA KR PA PA LR PA IN PA XX WT WT LG PA WA\n"+
        "WA XX CH PA WA PA PA PA XX WT WT WA PA WA\n"+
        "WA PA PA PA WA PA PA PA PA WT WT WA TR WA\n"+
        "WA WA WA WA WA WA WA WA WA WA WA WA WA WA";
    return BoardRig.fromString(board);
  }

  public static Maze teleporterTest1() {
    String board = "WA WA WA WA WA WA WA\n"+
        "WA PA PA PA PA PA WA\n"+
        "WA PR PG PA PB PY WA\n"+
        "WA WA WA WA WA WA WA\n"+
        "WA PB PY PA PR PG WA\n"+
        "WA PA PA CH PA PA WA\n"+
        "WA WA WA WA WA WA WA";
    return BoardRig.fromString(board);
  }

  public static Maze enemyKillTest1() {
    String board = "WA WA WA WA WA\n"+
        "WA PA PA PA WA\n"+
        "WA EN PA CH WA\n"+
        "WA PA PA PA WA\n"+
        "WA WA WA WA WA";
    return BoardRig.fromString(board);
  }
  
  public static Maze exitLockTest1() {
    String board = "WA WA WA WA\n"+
        "WA PA PA WA\n"+
        "WA CH TR WA\n"+
        "WA PA PA WA\n"+
        "WA WA EL WA\n"+
        "WA EX PA WA\n"+
        "WA WA WA WA";
    return BoardRig.fromString(board);
  }
  
  public static Maze diamondPickTest1() {
    String board = "WA WA WA\n"+
        "WA CH WA\n"+
        "WA KG WA\n"+
        "WA LG WA\n"+
        "WA LG WA\n"+
        "WA TR WA\n"+
        "WA EL WA\n"+
        "WA EX WA\n"+
        "WA WA WA";
    return BoardRig.fromString(board);
  }

  /**
   * Construct Board from string
   *getOther
   * @param input string formatted as in class comment
   * @return
   */
  public static Maze fromString(String input) {
    // split the input into rows then tokens
    String[] lines = input.split("\n");
    Tile[][] tiles = new Tile[lines.length][];
    List<Containable> entities = new ArrayList<>();
    List<Teleporter> unbound = new ArrayList<>();

    for (int r = 0; r < lines.length; r++) {

      String[] line = lines[r].split("\\s");
      Tile[] row = new Tile[line.length];

      for (int c = 0; c < line.length; c++) {
        Drawable d = fromToken(line[c]);
        // if it's a containable, make a PathTile and place the containable inside
        if (d instanceof Containable) {
          PathTile p = new PathTile("resources/textures/board/tile/smooth_stone.png");
          p.place((Containable) d);
          row[c] = p;
          entities.add((Containable) d);
          if(d instanceof Teleporter) {
            Teleporter tp = (Teleporter) d;
            Teleporter other = getMatch(tp, unbound);
            if(other!=null) {
              tp.setOther(other);
              other.setOther(tp);
            }else{
              unbound.add(tp);
            }
          }
        } else { // otherwise it's a Tile
          row[c] = (Tile) d;
        }
        row[c].setCoords(r, c); // ensure the 2 way link
      }
      tiles[r] = row;
    }
    return new Maze(tiles, entities);
  }

  private static Teleporter getMatch(Teleporter toMatch, List<Teleporter> unbound) {
    for(Teleporter tele: unbound) {
      if(toMatch.getColor().equals(tele.getColor())) {
        return tele;
      }
    }
    return null;
  }

  /**
   * Get a Drawable from a string token
   *
   * @param token
   * @return public Set<Treasure> getTreasures() { return treasures; }
   *
   */
  public static Drawable fromToken(String token) {
    Preconditions.checkArgument(token.length() == 2, "Token %s has length %s but expected 2",
        token, token.length());
    switch (token) {
    case "WA":
      return new WallTile("resources/textures/board/tile/wall.png");
    case "PA":
      return new PathTile("resources/textures/board/tile/smooth_stone.png");
    case "TR":
      return new Treasure("resources/textures/board/pickup/treasure/emerald.gif");
    case "IN":
      return new InfoField("resources/textures/board/pickup/sign.png", "testing");
    case "EL":
      return new ExitLock("resources/textures/board/blocking/doors/bedrock.png");
    case "EX":
      return new Exit("resources/textures/board/pickup/netherportal.gif");
    case "CH":
      // return new
      // Chap("resources/textures/board/moveable/character_skins/new_player_skin/steve.png");
      return new Chap(
          "resources/textures/board/moveable/character_skins/new_player_skin/SteveLeft_WalkBig.gif",
          "resources/textures/board/moveable/character_skins/new_player_skin/SteveRight_WalkBig.gif");
    case "XX":
      return new Crate("resources/textures/board/moveable/crate.png");
    case "WT":
      return new Water("resources/textures/board/blocking/water.gif");
    case "EN":
      return new Enemy(
          "resources/textures/board/moveable/character_skins/enemy_skins/creeper/creeper_leftwalkBig.gif");
    default:
      Matcher keyMatch = keyPat.matcher(token);
      Matcher lockMatch = lockPat.matcher(token);
      Matcher teleMatch = telePat.matcher(token);

      if (keyMatch.matches()) {
        return new Key(fileForKey(keyMatch.group(1)), colorFromToken(keyMatch.group(1)));
      } else if (lockMatch.matches()) {
        return new Door(fileForDoor(lockMatch.group(1)), colorFromToken(lockMatch.group(1)));
      }else if (teleMatch.matches()) {
          return new Teleporter(fileForTeleporter(teleMatch.group(1)), colorFromToken(teleMatch.group(1)));
      }
    }
    return null;
  }

  public static Maze.KeyColor colorFromToken(String token) {
    switch (token) {
    case "B":
      return Maze.KeyColor.BLUE;
    case "R":
      return Maze.KeyColor.RED;
    case "G":
      return Maze.KeyColor.GREEN;
    case "Y":
      return Maze.KeyColor.YELLOW;
    default:
      throw new IllegalArgumentException();
    }
  }

  public static String fileForKey(String token) {
    switch (token) {
    case "B":
      return "resources/textures/board/pickup/keys/wooden_pickaxe.png";
    case "R":
      return "resources/textures/board/pickup/keys/iron_pickaxe.png";
    case "G":
      return "resources/textures/board/pickup/keys/diamond_pickaxe.png";
    case "Y":
      return "resources/textures/board/pickup/keys/golden_pickaxe.png";
    default:
      throw new IllegalArgumentException();
    }
  }

  public static String fileForDoor(String token) {
    switch (token) {
    case "B":
      return "resources/textures/board/blocking/doors/spruce_planks.png";
    case "R":
      return "resources/textures/board/blocking/doors/iron_block.png";
    case "G":
      return "resources/textures/board/blocking/doors/diamond_block.png";
    case "Y":
      return "resources/textures/board/blocking/doors/gold_block.png";
    default:
      throw new IllegalArgumentException();
    }
  }

  public static String fileForTeleporter(String token) {
    switch (token) {
    case "B":
      return "resources/textures/board/pickup/portals/redportal.gif";
    case "R":
      return "resources/textures/board/pickup/portals/greenportal.gif";
    case "G":
      return "resources/textures/board/pickup/portals/diamondportal.gif";
    case "Y":
      return "resources/textures/board/pickup/portals/goldportal.gif";
    default:
      throw new IllegalArgumentException();
    }
  }

}
