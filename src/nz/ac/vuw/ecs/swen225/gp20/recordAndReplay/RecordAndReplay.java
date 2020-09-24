package nz.ac.vuw.ecs.swen225.gp20.recordAndReplay;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEvent;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventListener;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventWalked;

import javax.swing.plaf.DimensionUIResource;
import java.nio.file.Path;
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
     * Collection for storing maze events
     */
    private List<MazeEvent> events;

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
        events = new ArrayList<>();
    }

    /**
     * Stops the current recording and saves it as a JSON
     */
    public void stopRecording() {
        isRecording = false;
    }

    /**
     * Add a maze event to the collection of events in this recording
     */
    public void addEvent(MazeEvent event) {
        events.add(event);
    }

    /**
     * Apply the event to the maze
     */
    public void applyEvent(MazeEvent event, boolean stepForward) {
        if(event instanceof MazeEventWalked) {
            MazeEventWalked eventWalked = ((MazeEventWalked) event);

            PathTile destination = stepForward ? eventWalked.getDestination() : eventWalked.getOrigin();


            Containable containable = eventWalked.getOrigin().getContainedEntities().peek();

            destination.moveTo(containable);

        }

    }



    /**
     * Advance the playback of this recording by one step
     */
    public void stepForward() {
        if(index == events.size()) {return;}

        //advance the recording one step
        index++;

        //get the current maze event
        MazeEvent event = events.get(index);

        //apply the event
        applyEvent(event, true);

    }

    /**
     * Rewind the playback of this recording by one step
     */
    public void stepBack() {
        if(index == 0) {return;}

        //get the current maze event
        MazeEvent event = events.get(index);

        //rewind the recording one step
        index--;

        //apply the event
        applyEvent(event, false);

    }

    /**
     * Check whether the game is being recorded
     */
    public boolean isRecording() {
        return isRecording;
    }

}
