package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import nz.ac.vuw.ecs.swen225.gp20.application.Gui;
import nz.ac.vuw.ecs.swen225.gp20.maze.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Class used for recording are replaying gameplay
 *
 * @author Ford Leins 300314179
 */
public class RecordAndReplay {

    private static Gui gui;

    /**
     * The current loaded recording
     */
    private static List<Maze.Direction> currentRecording;

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
    private static int wait = 600;

    private static ObjectMapper mapper = new ObjectMapper();


    /**
     * Constructor
     *
     * @param gui the application
     */
    public RecordAndReplay(Gui gui) {
        RecordAndReplay.gui = gui;
    }


    /**
     * Saves the current recording as a JSON file
     *
     * @param saveName the name of the save file
     */
    public static void saveRecording(String saveName) {
        if (currentRecording == null) {
            return; //no moves to save
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveName));
            writer.write(mapper.writeValueAsString(currentRecording));
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing save game file: " + e);
        }
    }

    /**
     * Loads a recording from a JSON file
     *
     * @param jsonSave the JSON file of this recording
     */
    public static void loadRecording(File jsonSave) {
        if (isRecording || jsonSave == null) {
            return;
        } //if game play is being recorded or there is no file, return
        try {
            currentRecording = new ArrayList<>();
            for(Object obj : mapper.readValue(jsonSave, ArrayList.class)) {
                Maze.Direction move;
                switch((String) obj) {
                    case "UP":
                        move = Maze.Direction.UP;
                        break;
                    case "DOWN":
                        move = Maze.Direction.DOWN;
                        break;
                    case "LEFT":
                        move = Maze.Direction.LEFT;
                        break;
                    case "RIGHT":
                        move = Maze.Direction.RIGHT;
                        break;
                    default:
                        return;
                }
                currentRecording.add(move);
            }
        } catch (IOException e) {
            System.out.println("Failed to load recording: " + e);
        }
    }

    /**
     * Replays the current recording
     */
    public void playRecording() {
        if (isRecording || currentRecording == null) {
            return;
        }

        //set game state here

        Runnable runnable = () -> {
            //execute moves
            for(Maze.Direction move : currentRecording) {
                gui.move(move);
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
     * Starts a new recording, sets the gameState JSON field
     */
    public static void startRecording() {
        if (isRecording) {
            return;
        } //do nothing if already recording
        isRecording = true;
        currentRecording = new ArrayList<>();
    }

    /**
     * Stops the current recording
     */
    public static void stopRecording() {
        isRecording = false;
    }

    /**
     * Adds a move to the collection of playerMoves in this recording
     *
     * @param direction the direction of this move
     */
    public static void addMove(Maze.Direction direction) {
        currentRecording.add(direction);
    }

    /**
     * Advance the playback of this recording by one step
     */
    public void stepForward() {
        if (currentRecording == null || index == currentRecording.size()) {
            return;
        }
    }

    /**
     * Rewind the playback of this recording by one step
     */
    public void stepBack() {
        if (currentRecording == null || index == 0) {
            return;
        }
    }

    /**
     * Check whether the game is being recorded
     */
    public static boolean isRecording() {
        return isRecording;
    }

}
