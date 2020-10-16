package nz.ac.vuw.ecs.swen225.gp20.recnplay;

/**
 * Class for recording a move, stores the
 * id of the actor that moved and the direction they moved
 *
 * @author Ford Leins 300314179
 */
public class Move {

    public int actorId;
    public int direction;

    public Move(int actorId, int direction) {
        //for enemies this id will be their index in the list of enemies in maze
        this.actorId = actorId;
        this.direction = direction;
    }

    public Move() {

    }

}
