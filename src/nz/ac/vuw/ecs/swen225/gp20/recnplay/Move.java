package nz.ac.vuw.ecs.swen225.gp20.recnplay;

/**
 * Class for recording a move, stores the
 * id of the actor that moved and the direction they moved.
 *
 * @author Ford Leins 300314179
 */
public class Move {

  /**
   * The id of this actor, non negative for enemies, -1 for chap.
   */
  public int actorId;

  /**
   * The ordinal direction of this move.
   */
  public int direction;

  /**
   * Constructor.
   *
   * @param actorId   the integer id of the actor that moved
   * @param direction the ordinal direction this actor moved
   */
  public Move(int actorId, int direction) {
    //for enemies this id will be their index in the list of enemies in maze
    this.actorId = actorId;
    this.direction = direction;
  }

  /**
   * Empty constructor for Jackson.
   */
  public Move() {

  }

}
