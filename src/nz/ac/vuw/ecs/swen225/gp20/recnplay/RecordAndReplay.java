package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import nz.ac.vuw.ecs.swen225.gp20.application.Gui;

import java.io.File;
import java.util.*;

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
    public static long beginning, end;

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
        if (!isRecording) {

            //set the move data
            currentRecording = recording;

            //record the timestamp of the beginning of this recording
            beginning = startTime;

            //set the end time, this is the time stamp of the final move
            end = Collections.min(currentRecording.keySet());

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
        if (!isRecording || inPlaybackMode) {
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
        if (currentRecording == null || currentRecording.isEmpty() || isRecording) {
            return;
        }

        //set index, or step back
        step = beginning;

        Runnable runnable = () -> {

            while (RecordAndReplay.step > end && !RecordAndReplay.paused) {
                //advance the recording
                RecordAndReplay.stepForward();
                try {
                    //if fastest playback speed is selected only sleep when chap moves
                    if (playbackSpeed == 0) {
                        //if the last move was chap
                        for (Move move : getMovesAtStep(step - 1)) {
                            //sleep the thread
                            if (move.actorId == -1) {
                                //sleep
                                Thread.sleep(130);
                                break;
                            }
                        }
                    } else {
                        //sleep
                        Thread.sleep(playbackSpeed);
                    }
                } catch (InterruptedException e) {
                    System.out.println("Error with playback: " + e);
                }
            }
            System.out.println("Replay finished!");
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
        if (inPlaybackMode) {

        }
    }

    /**
     * Advance the playback of this recording by one
     * step (1 millisecond or until the next move)
     */
    public static void stepForward() {
        if (!isRecording && step >= end) {

            //get the list of moves at this timestamp
            List<Move> movesAtStep = getMovesAtStep(step);

            //execute all the moves on the appropriate actors
            for (Move move : movesAtStep) {
                gui.executeMove(move, false);
            }

            //increment the step (index)
            step--;
        }
    }

    /**
     * Rewind the playback of this recording by one
     * step (1 millisecond or until the next move)
     */
    public static void stepBack() {
        if (!isRecording && step < beginning) {

            //increment the step (index)
            step++;

            //get the list of moves at this timestamp
            List<Move> movesAtStep = getMovesAtStep(step);

            //execute all the moves on the appropriate actors
            for (Move move : movesAtStep) {
                gui.executeMove(move, true);
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
    private static List<Move> getMovesAtStep(long step) {
        List<Move> moves = new ArrayList<>();
        if (!isRecording && !currentRecording.isEmpty() && currentRecording.containsKey(step)) {
            moves.addAll(currentRecording.get(step));
        }
        return moves;
    }

    /**
     * Advances the step to the next timestamp of a chap move
     * this stops the next frame button feeling un-responsive
     */
    public void nextFrame() {
        while (step > end) {
            //check if chap moves at this time stamp
            for (Move move : getMovesAtStep(step)) {
                if (move.actorId == -1) {
                    stepForward();
                    return;
                }
            }
            //apply the move(s)
            stepForward();
        }
    }

    /**
     * Rewinds the step to the last timestamp of a chap move
     * this stops the next frame button feeling un-responsive
     */
    public void lastFrame() {
        while(step < beginning) {
            //check if chap moved at this time stamp
            for (Move move : getMovesAtStep(step)) {
                if (move.actorId == -1) {
                    stepBack();
                    return;
                }
            }
            //undo the move(s)
            stepBack();
        }
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
