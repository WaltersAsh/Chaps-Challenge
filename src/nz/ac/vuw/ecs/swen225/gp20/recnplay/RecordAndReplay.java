package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import nz.ac.vuw.ecs.swen225.gp20.application.Gui;

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
     * Fields for remembering whether gameplay is
     * currently being recorded or playback is paused
     */
    public static boolean isRecording = false, inPlaybackMode = false, paused = false;

    /**
     * Delay for playback speed
     */
    public static long playbackSpeed = 1L;

    /**
     * The current step/time in this recording in milliseconds
     */
    public static long step;

    /**
     * The time stamp of the beginning of this
     * recording i.e. the time left (milliseconds)
     */
    private static long beginning;

    /**
     * The current recording either being constructed or loaded
     */
    public static Map<Long, List<Move>> currentRecording;

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
     * Loads a recording from a map of moves
     *
     * @param recording the map of lists of moves
     * @param startTime the time remaining at the start of the recording
     */
    public void loadRecording(Map<Long, List<Move>> recording, Long startTime) {
        if (!isRecording && !inPlaybackMode) {

            //set the move data
            currentRecording = recording;

            //record the timestamp of the beginning of this recording
            beginning = startTime;

            //set the current index, or step of this recording to the beginning time stamp
            step = startTime;

            //set playback mode to true
            inPlaybackMode = true;
        }
    }

    /**
     * Starts a new recording, records the time left when recording begins
     */
    public static void startRecording() {
        if (!isRecording && !inPlaybackMode) {
            isRecording = true;
            currentRecording = new HashMap<>();
        }
    }

    /**
     * Stops and saves the current recording to a json file
     */
    public static void stopRecording() {
        if(!isRecording || inPlaybackMode) {
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
     * Replays the current recording
     */
    public void playRecording() {
        if(currentRecording == null || currentRecording.isEmpty() || isRecording) {
            return;
        }

        //set index, or step back
        step = beginning;


        Runnable runnable = () -> {

            while(RecordAndReplay.step > 0 && !RecordAndReplay.paused) {

                RecordAndReplay.stepForward();
                try {
                    Thread.sleep(playbackSpeed);
                } catch (InterruptedException e1) {
                    System.out.println("Error with playback: " + e1);
                }
            }
            System.out.println("Replay finished");
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Pauses playback of the current recording
     */
    public void pausePlayback() {
        paused = true;
    }

    /**
     * Stops playback of the current recording
     */
    public void stopPlayback() {
        if(inPlaybackMode) {

        }
    }

    /**
     * Advance the playback of this recording by one
     * step (1 millisecond or until the next move)
     */
    public static void stepForward() {
        if (!isRecording && step > 0) {
            List<Move> movesAtStep = getMovesAtStep(step);
            if(movesAtStep != null) {
                //execute all the moves on the appropriate actors
                for (Move move : movesAtStep) {
                    gui.executeMove(move);
                }
            }
            step--;
        }
    }

    /**
     * Rewind the playback of this recording by one
     * step (1 millisecond or until the next move)
     */
    public static void stepBack() {
        if (!isRecording && step < beginning) {
            List<Move> movesAtStep = getMovesAtStep(step);

            if(movesAtStep != null) {

                //execute all the moves on the appropriate actors
                for (Move move : movesAtStep) {
                    //chaps moves need to be undone with undo redo handler whereas mobs can simply move backwards
                    if(move.actorId == -1) {
                        gui.undoMove();
                        continue;
                    }

                    //invert move direction
                    move.direction = move.direction == 0 || move.direction == 2 ? move.direction + 1 : move.direction - 1;

                    gui.executeMove(move);
                }
            }
            step++;
        }
    }

    /**
     * Gets the list of moves at a given point in time
     * in the recording
     *
     * @param step the time stamp to retrieve
     * @return the list of move objects
     */
    private static List<Move> getMovesAtStep(long step) {
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

    /**
     * Adjusts the playback speed
     *
     * @param newSpeed the new value to delay playback speed by
     */
    public void setPlaybackSpeed(long newSpeed) {
        playbackSpeed = newSpeed;
    }

}
