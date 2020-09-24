package nz.ac.vuw.ecs.swen225.gp20.recordAndReplay;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Class used for recording are replaying gameplay
 */
public class RecordAndReplay {

    /**
     * The maze of this recording
     */
    private Maze maze;

    /**
     * Collection for storing maze playerMoves
     */
    private List<Maze.Direction> playerMoves;

    /**
     * The current step (index) of this recording
     */
    private static int index = 0;

    /**
     * Field for remembering whether gameplay is currently being recorded
     */
    private static boolean isRecording = false;


    /**
     * Constructor
     */
    public RecordAndReplay (Maze maze) {
        //Todo need a clone of the maze in its current state


    }

    /**
     * Starts a new recording
     */
    public void startRecording() {
        if(isRecording) {return;} //do nothing if already recording
        isRecording = true;
        playerMoves = new ArrayList<>();
    }

    /**
     * Stops the current recording and saves it as a JSON
     */
    public void stopRecording() {
        isRecording = false;
    }

    /**
     * Adds a move to the collection of playerMoves in this recording
     */
    public void addMove(Maze.Direction direction) {
        playerMoves.add(direction);
    }



    /**
     * Advance the playback of this recording by one step
     */
    public void stepForward() {
        if(index == playerMoves.size()) {return;}

        //advance the recording one step
        index++;

        //get the current maze event
        Maze.Direction direction = playerMoves.get(index);

    }

    /**
     * Rewind the playback of this recording by one step
     */
    public void stepBack() {
        if(index == 0) {return;}

        //get the current maze event
        Maze.Direction direction = playerMoves.get(index);

        //rewind the recording one step
        index--;

    }

    /**
     * Check whether the game is being recorded
     */
    public boolean isRecording() {
        return isRecording;
    }

}
