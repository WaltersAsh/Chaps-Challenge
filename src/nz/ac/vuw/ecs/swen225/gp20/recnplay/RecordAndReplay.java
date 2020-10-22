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

    /**
     * The gui this recorder object belongs to
     */
    private static Gui gui;

    /**
     * Finite state machine for switching states of this recorder
     * and their transitions
     */
    public enum State {

        //game play is currently being recorded
        RECORDING {
            @Override
            public void startStopRecording() {
                gui.getMenuBar().getStartStopRecordingMenuItem().setText("Start Recording");
                gui.getMenuBar().getLoadRecordingMenuItem().setVisible(true);
                state = SLEEPING;
            }

            @Override
            public void startStopReplay() {
            }

            @Override
            public void loadRecording(Map<Long, List<Move>> recording) {
            }
        },

        //a recording is loaded and ready to be played back
        LOADED {
            @Override
            public void startStopRecording() {
            }

            @Override
            public void startStopReplay() {
                gui.getMenuBar().getStartStopReplayMenuItem().setText("Exit Replay");
                state = REPLAYING;
            }

            @Override
            public void loadRecording(Map<Long, List<Move>> recording) {
            }
        },

        //game play is currently being replayed
        REPLAYING {
            @Override
            public void startStopRecording() {
            }

            @Override
            public void startStopReplay() {
                //load quick saved game state
                gui.quickLoadLevel();
                //hide replay menu button
                gui.getMenuBar().getStartStopReplayMenuItem().setText("Start Replay");
                gui.getMenuBar().getStartStopReplayMenuItem().setVisible(false);
                //show recording and load menu buttons
                gui.getMenuBar().getStartStopRecordingMenuItem().setVisible(true);
                gui.getMenuBar().getLoadRecordingMenuItem().setVisible(true);
                gui.getReplayingIconLabel().setVisible(false);
                state = SLEEPING;
            }

            @Override
            public void loadRecording(Map<Long, List<Move>> recording) {
            }
        },

        //game is being played as normal, not being recorded
        SLEEPING {
            @Override
            public void startStopRecording() {
                gui.getMenuBar().getStartStopRecordingMenuItem().setText("Stop Recording");
                gui.getMenuBar().getLoadRecordingMenuItem().setVisible(false);
                currentRecording = new HashMap<>();
                state = RECORDING;
            }

            @Override
            public void startStopReplay() {
            }

            @Override
            public void loadRecording(Map<Long, List<Move>> recording) {
                setRecording(recording);
                //hide load and start recording buttons on menu
                gui.getMenuBar().getStartStopRecordingMenuItem().setVisible(false);
                gui.getMenuBar().getLoadRecordingMenuItem().setVisible(false);
                //show replay button on menu
                gui.getMenuBar().getStartStopReplayMenuItem().setVisible(true);
                gui.getMenuBar().getStartStopReplayMenuItem().setText("Start Replay");
                //show the replay icon label
                gui.getReplayingIconLabel().setVisible(true);
                state = LOADED;
            }

        };

        /**
         * Start or stop recording gameplay depending on current state
         */
        public abstract void startStopRecording();

        /**
         * Start or stop playback depending on current state
         */
        public abstract void startStopReplay();

        /**
         * Load a recording
         *
         * @param recording the map of lists of moves
         */
        public abstract void loadRecording(Map<Long, List<Move>> recording);
    }

    /**
     * The current state of this recorder, set to sleeping by default
     */
    private static State state = State.SLEEPING;

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
     * @param gui the application this recorder belongs to
     */
    public RecordAndReplay(Gui gui) {
        RecordAndReplay.gui = gui;
    }

    /**
     * Set the current recording field and update
     * menus accordingly
     *
     * @param recording the map of move data for this recording
     */
    private static void setRecording(Map<Long, List<Move>> recording) {
        //set the move data
        currentRecording = recording;
        //set the current index, or step of this recording to the beginning time stamp
        step = 0;
        //add the key set of times to a list
        timeStamps = new ArrayList<>(currentRecording.keySet());
        //sort the list in reverse
        timeStamps.sort(Collections.reverseOrder());
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
     * Replays the current recording in a new thread
     */
    public void playRecording() {
        //a recording can only be played from loaded state
        if (state != State.LOADED) {
            return;
        }

        Runnable runnable = () -> {

            //step through each move in the recording, sleeping in between moves
            while (step < timeStamps.size()) {

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
     * Advance the playback of this recording by one step
     */
    public static void stepForward() {
        if (step == timeStamps.size() - 1) {
            return;
        }

        long time = timeStamps.get(step);
        for (Move move : getMovesAtStep(time)) {
            gui.executeMove(move, false);
        }

        step++;
    }

    /**
     * Rewind the playback of this recording by one step
     */
    public static void stepBack() {
        if (step == 0) {
            return;
        }

        step--;

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
        if (!currentRecording.isEmpty() && currentRecording.containsKey(step)) {
            moves.addAll(currentRecording.get(step));
        }
        return moves;
    }

    /**
     * Advances the step to the next chap move
     */
    public void nextFrame() {
        if(state == State.REPLAYING || state == State.LOADED) {
            while (step < timeStamps.size() - 1) {
                stepForward();
                for (Move move : getMovesAtStep(timeStamps.get(step - 1))) {
                    if (move.actorId == -1) {
                        return;
                    }
                }
            }
        }
    }

    /**
     * Rewinds the step to the last timestamp of a chap move
     */
    public void lastFrame() {
        if(state == State.REPLAYING || state == State.LOADED) {
            while (step > 0) {
                stepBack();
                for (Move move : getMovesAtStep(timeStamps.get(step))) {
                    if (move.actorId == -1) {
                        return;
                    }
                }
            }
        }
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

    /**
     * Get the save file path for the game state at
     * the beginning of the recording
     *
     * @return the file
     */
    public File getSaveFile() {
        return saveFile;
    }

    /**
     * Set the save file path for the game state at
     * the beginning of the recording
     *
     * @param saveFile the file
     */
    public void setSaveFile(File saveFile) {
        this.saveFile = saveFile;
    }

    /**
     * Get the current state
     *
     * @return the current state of the recorder
     */
    public State getState() {
        return state;
    }

}
