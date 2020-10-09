package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import nz.ac.vuw.ecs.swen225.gp20.application.Gui;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class used for recording are replaying gameplay
 *
 * @author Ford Leins 300314179
 */
public class RecordAndReplay {

    private static Gui gui;

    /**
     * Fields for remembering whether gameplay is currently being recorded
     * or playback is paused
     */
    private static boolean isRecording = false;

    /**
     * Delay for playback speed
     */
    private static long playbackSpeed = 1000L;

    /**
     * The current step/time in this recording in milliseconds
     */
    private static long step;

    /**
     * The time stamp of the beginning of this recording i.e. the time left (milliseconds)
     */
    private static long beginning;

    /**
     * The current recording either being constructed or loaded
     */
    private static Map<Long, List<Move>> currentRecording;

    /**
     * The save file of this recording
     */
    private static File saveFile;







    /**
     * Constructor
     *
     * @param gui the application
     */
    public RecordAndReplay(Gui gui) {
        RecordAndReplay.gui = gui;
    }

    /**
     * Loads a recording from a JSON file
     *
     * @param file the save file to be loaded
     */
    public static void loadRecording(File file) {
        if (!isRecording) {
            //Todo need game state and map of moves
        }
    }

    /**
     * Replays the current loaded recording
     */
    public void playRecording() {

    }

    /**
     * Starts a new recording
     */
    public static void startRecording() {
        if (!isRecording) {
            isRecording = true;
            currentRecording = new HashMap<>();

        }
    }

    /**
     * Stops and saves the current recording to a json file
     */
    public static void stopRecording(File file) {
        if(!isRecording) {
            return;
        }
        isRecording = false;
    }

    /**
     * Add the given move to the current list of moves i.e. the recording
     *
     * @param move the move object containing the id of the actor which moved
     *             and which direction it was
     */
    public void addMove(Move move) {

        //the time when this move was made, used as the key
        long timeStamp = gui.getCurrentTimeStamp();

        //check if a move has already been made at this time
        if (currentRecording.containsKey(timeStamp)) {
            currentRecording.get(timeStamp).add(move);
        } else {
            //if not then create a new list to store moves at this time
            List<Move> newList = new ArrayList<>();
            newList.add(move);
            currentRecording.put(timeStamp, newList);
        }
    }

    /**
     * Advance the playback of this recording by one
     * step (1 millisecond or until the next move)
     */
    public void stepForward() {
        if (!isRecording && step > 0) {
            step--;
            List<Move> movesAtStep = getMovesAtStep(step);
            if(movesAtStep != null) {
                //execute all the moves on the appropriate actors
                for (Move move : movesAtStep) {
                    //gui.executeMove(move);
                }
            }
        }
    }

    /**
     * Rewind the playback of this recording by one
     * step (1 millisecond or until the next move)
     */
    public void stepBack() {
        if (!isRecording && step < beginning) {
            step++;
            List<Move> movesAtStep = getMovesAtStep(step);
            if(movesAtStep != null) {
                //execute all the moves on the appropriate actors
                for (Move move : movesAtStep) {
                    //gui.executeMove(move);
                }
            }
        }
    }

    /**
     * Gets the list of moves at a given point in time
     * in the recording
     *
     * @param step the time stamp to retrieve
     * @return the list of move objects
     */
    private List<Move> getMovesAtStep(long step) {
        if (!isRecording && !currentRecording.isEmpty() && currentRecording.containsKey(step)) {
            return currentRecording.get(step);
        }
        return null;
    }

    /**
     * Check whether the game is being recorded
     */
    public static boolean isRecording() {
        return isRecording;
    }

}
