package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import nz.ac.vuw.ecs.swen225.gp20.application.Gui;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;

import java.io.File;
import java.util.List;

/**
 * Class used for recording are replaying gameplay
 *
 * @author Ford Leins 300314179
 */
public class RecordAndReplay {

    private static Gui gui;

    /**
     * The current step (index) of this recording
     */
    private static int index = 0;

    /**
     * Fields for remembering whether gameplay is currently being recorded
     * or playback is paused
     */
    private static boolean isRecording = false;

    /**
     * Delay for playback speed
     */
    private static int wait = 120;

    private static List<Maze.Direction> loadedRecording;

    /**
     * The save file of this recording,
     * necessary for appending moves to the original game state
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
            Maze loadedMaze = Persistence.loadMaze(file);
            loadedRecording = loadedMaze.getMoves();
            gui.loadLevel(loadedMaze);
        }
    }

    /**
     * Replays the current loaded recording
     */
    public void playRecording() {
        if (isRecording || loadedRecording == null) {
            return;
        }
        Runnable runnable = () -> {
            //execute moves
            for(Maze.Direction move : loadedRecording) {
                //gui.move(move);
                //pause
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Starts a new recording, saves the gameState as a json
     *
     * @param file the file to save the game state to
     */
    public static void startRecording(File file) {
        if (!isRecording) {
            isRecording = true;
            saveFile = file;
            Persistence.saveMaze(gui.getMaze(), file);
        }
    }

    /**
     * Stops the current recording and updates the list of moves
     * in the original game state
     */
    public static void stopRecording() {
        isRecording = false;

        //load the original game state from the start of the recording
        Maze maze = Persistence.loadMaze(saveFile);

        //update the moves
        maze.setMoves(gui.getMaze().getMoves());

        //save the maze with the list of moves
        Persistence.saveMaze(maze, saveFile);
    }

    /**
     * Advance the playback of this recording by one step
     */
    public void stepForward() {
    }

    /**
     * Rewind the playback of this recording by one step
     */
    public void stepBack() {
    }

    /**
     * Check whether the game is being recorded
     */
    public static boolean isRecording() {
        return isRecording;
    }

}
