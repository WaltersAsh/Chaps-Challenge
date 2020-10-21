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
     * currently being recorded or a game is being played back
     */
    private static boolean isRecording = false, inPlaybackMode = false;

    /**
     * Delay for playback speed
     */
    private static double playbackSpeed = 1;

    /**
     * The current step/time in this recording in milliseconds
     */
    private static int step;

    /**
     * The current recording either being constructed or loaded
     */
    private static Map<Long, List<Move>> currentRecording;

    /**
     * Collection storing the time stamps of each move
     * in the current recording
     */
    private static List<Long> timeStamps = new ArrayList<>();

    /**
     * File used for recording game state
     */
    private File saveFile;



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
     */
    public void loadRecording(Map<Long, List<Move>> recording) {
        if (!isRecording) {

            //set the move data
            currentRecording = recording;

            //set the current index, or step of this recording to the beginning time stamp
            step = 0;

            timeStamps = new ArrayList<>();
            timeStamps.addAll(currentRecording.keySet());
            Collections.sort(timeStamps);
            Collections.reverse(timeStamps);

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
        if (currentRecording == null || currentRecording.isEmpty() || isRecording || !inPlaybackMode) {
            return;
        }

        Runnable runnable = () -> {

            //step through each move in the recording, sleeping in between moves
            while (step < timeStamps.size() - 1) {

                //step forward
                stepForward();

                try {
                    Thread.sleep((long) (150 * playbackSpeed));
                } catch (InterruptedException e) {
                    System.out.println("Error with playback: " + e);
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Stops playback of the current recording
     */
    public void stopPlayback() {
        inPlaybackMode = false;
    }

    /**
     * Advance the playback of this recording by one step
     */
    public static void stepForward() {
        if (isRecording || step == timeStamps.size() - 1 || !inPlaybackMode) {
            return;
        }
        long time = timeStamps.get(step);
        for (Move move : getMovesAtStep(time)) {
            gui.executeMove(move, false);
        }

        step = step < timeStamps.size() - 1 ? step + 1 : step;

    }

    /**
     * Rewind the playback of this recording by one step
     */
    public static void stepBack() {
        if (isRecording || step == 0 || !inPlaybackMode) {
            return;
        }

        step = step > 0 ? step - 1 : step;

        long time = timeStamps.get(step);
        for (Move move : getMovesAtStep(time)) {
            gui.executeMove(move, true);
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
     * Advances the step to the next chap move
     */
    public void nextFrame() {
        if(!inPlaybackMode) {
            return;
        }
        while (step < timeStamps.size() - 1) {
            stepForward();
            for(Move move : getMovesAtStep(timeStamps.get(step - 1))) {
                if (move.actorId == -1) {
                    return;
                }
            }
        }
    }

    /**
     * Rewinds the step to the last timestamp of a chap move
     */
    public void lastFrame() {
        if(!inPlaybackMode) {
            return;
        }
        while (step > 0) {
            stepBack();
            for(Move move : getMovesAtStep(timeStamps.get(step))) {
                if (move.actorId == -1) {
                    return;
                }
            }
        }
    }

    /**
     * Check whether the game is being recorded
     */
    public boolean isRecording() {
        return isRecording;
    }

    /**
     * Check whether a recording is being played back
     */
    public boolean isInPlaybackMode() {
        return inPlaybackMode;
    }

    /**
     * Adjusts the playback speed
     *
     * @param newSpeed the new value to delay playback speed by
     */
    public void setPlaybackSpeed(double newSpeed) {
        playbackSpeed = newSpeed;
    }

    /**
     * Gets the current map of move data
     *
     * @return the current recording
     */
    public Map<Long, List<Move>> getMovesByTime() {
        return currentRecording;
    }

    public File getSaveFile() {
        return saveFile;
    }

    public void setSaveFile(File saveFile) {
        this.saveFile = saveFile;
    }
}
