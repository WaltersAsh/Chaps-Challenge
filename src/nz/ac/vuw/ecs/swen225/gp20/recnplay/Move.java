package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import java.awt.event.KeyEvent;

public class Move {

    public int actorId;
    public KeyEvent keyEvent;

    public Move(int actorId, KeyEvent keyEvent) {
        //for enemies this id will be their index in the list of enemies in maze
        this.actorId = actorId;
        this.keyEvent = keyEvent;
    }

}
