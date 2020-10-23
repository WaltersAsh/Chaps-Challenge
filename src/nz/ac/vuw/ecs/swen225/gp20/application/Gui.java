package nz.ac.vuw.ecs.swen225.gp20.application;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import nz.ac.vuw.ecs.swen225.gp20.maze.Key;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.KeyColor;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventEnemyWalked;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventEnemyWalkedKilled;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventInfoField;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventListener;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventPickup;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventUnlocked;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventWalkedDrowned;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventWalkedKilled;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventWon;
import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.Move;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.RecordAndReplay;
import nz.ac.vuw.ecs.swen225.gp20.rendering.BoardView;

/**
 * Gui class for visual display of the game.
 *
 * @author Justin Joe 300470389
 */
@JsonIgnoreType
public class Gui extends MazeEventListener implements ActionListener {
  // frame and main panels
  private final JFrame frame;
  private JPanel framePanel;
  public BoardPanel boardPanel;
  private SidePanel sidePanel;
  private final InstructionsFrame instructionsFrame;
  private final MenuBar menuBar;

  // value labels
  private JLabel levelValueLabel;
  private JLabel timeValueLabel;
  private JLabel treasuresValueLabel;
  private JLabel[] inventoryValueLabels;

  // infofield label
  private JLabel infoFieldLabel;
  private JLabel infoFieldTextLabel;

  //icon labels
  private JLabel recordingIconLabel;
  private JLabel pausedIconLabel;
  private JLabel replayingIconLabel;

  //buttons for controlling replaying in recnplay
  private final JButton nextFrameButton;
  private final JButton lastFrameButton;
  private final JButton autoPlayButton;
  private final JButton fasterReplayButton;
  private final JButton slowerReplayButton;
  private final JButton standardReplayButton;

  //popup dialog
  private PopupDialog levelCompleteDialog;
  private PopupDialog timerExpiryDialog;
  private PopupDialog deathDialog;

  //popup dialog buttons
  private JButton nextButton;
  private JButton levelCompleteRestartButton;
  private JButton timerExpiryRestartButton;
  private JButton deathRestartButton;

  //gui components
  private BoardView board;
  private Maze maze;
  private Timer timer;
  private TimerTask timerTask;
  private boolean isTimerActive;
  private boolean isPaused;

  private final ComponentLibrary cl;

  //recnplay components
  private final RecordAndReplay recnplay;

  /**
   * Construct the GUI: frame, panels, labels, menus, button listeners.
   *
   * @param maze the maze object passed
   */
  public Gui(Maze maze) {
    cl = ComponentLibrary.getInstance();
    this.maze = maze;

    recnplay = new RecordAndReplay(this);

    // base frame
    frame = new JFrame();
    frame.setLayout(new BorderLayout());
    createFramePanel();
    initialiseSidePanel();

    //menu bar
    menuBar = new MenuBar(this);

    //board panel
    boardPanel = new BoardPanel(maze);
    board = boardPanel.getBoard();
    maze.addListener(this);

    //labels/icons initialised
    infoFieldLabel = boardPanel.getInfoFieldLabel();
    infoFieldTextLabel = boardPanel.getInfoFieldTextLabel();
    recordingIconLabel = boardPanel.getRecordingIconLabel();
    pausedIconLabel = boardPanel.getPausedIconLabel();
    replayingIconLabel = boardPanel.getReplayingIconLabel();

    //recnplay initialisation
    RecnplayControlsPanel recnplayControlsPanel = new RecnplayControlsPanel(this);
    nextFrameButton = recnplayControlsPanel.getNextFrameButton();
    lastFrameButton = recnplayControlsPanel.getLastFrameButton();
    autoPlayButton = recnplayControlsPanel.getAutoPlayButton();
    fasterReplayButton = recnplayControlsPanel.getFasterReplayButton();
    slowerReplayButton = recnplayControlsPanel.getSlowerReplayButton();
    standardReplayButton = recnplayControlsPanel.getStandardReplayButton();

    // set up board and side panels into frame panel
    framePanel.add(boardPanel);
    framePanel.add(Box.createRigidArea(new Dimension(20, 0)));
    framePanel.add(sidePanel);

    //popup dialogs initialisation
    initialisePopupDialogs();

    //instuctions initialisation
    instructionsFrame = new InstructionsFrame(this);

    //reload gui information (for save and exit shortcuts)
    clearInventoryPanel();
    reloadInventoryPanel();
    levelValueLabel.setText(String.valueOf(maze.getLevelID()));
    timeValueLabel.setText(String.valueOf(maze.getMillisecondsLeft() / 1000));

    //initialise optionpane stylisation
    UIManager.put("OptionPane.background", new ColorUIResource(cl.deepLavender));
    UIManager.put("Panel.background", new ColorUIResource(cl.lightLavender));
    UIManager.put("OptionPane.messageFont", cl.buttonFont);
    UIManager.put("OptionPane.buttonFont", cl.buttonFont);
    UIManager.put("OptionPane.messageForeground", Color.BLACK);
    UIManager.put("Button.background", cl.fullLavender);
    UIManager.put("Button.foreground", Color.WHITE);
    GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(cl.infoFont);

    //initialise filechooser stylisation
    UIManager.put("List.background", cl.deepLavender);
    UIManager.put("List.foreground", Color.WHITE);
    UIManager.put("FileChooser.listFont", cl.sideFont);

    // initialise frame
    frame.add(framePanel);
    frame.add(recnplayControlsPanel, BorderLayout.SOUTH);
    frame.setJMenuBar(menuBar);
    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    frame.setTitle("Chap's Challenge");
    Dimension dimen = Toolkit.getDefaultToolkit().getScreenSize();
    frame.pack();
    frame.setSize(1024, 800);
    frame.setMinimumSize(new Dimension(1010, 820));
    frame.setLocation(dimen.width / 2 - frame.getSize().width / 2,
            dimen.height / 2 - frame.getSize().height / 2);
    frame.setFocusable(true);
    initialiseWindowListener();
    setupTimer();
    setupKeyListener();
    maze.resume();
  }

  /**
   * Create the frame panel.
   */
  private void createFramePanel() {
    framePanel = new JPanel();
    framePanel.setLayout(new BoxLayout(framePanel, BoxLayout.X_AXIS));
    framePanel.setBackground(cl.lightLavender);
    framePanel.setBorder(new EmptyBorder(50, 50, 50, 50));
  }

  /**
   * Initialise the side panel of the gui.
   */
  private void initialiseSidePanel() {
    sidePanel = new SidePanel(maze);
    levelValueLabel = sidePanel.getLevelValueLabel();
    timeValueLabel = sidePanel.getTimeValueLabel();
    treasuresValueLabel = sidePanel.getTreasuresValueLabel();
    inventoryValueLabels = sidePanel.getInventoryValueLabels();
  }

  /**
   * Initialise the popup dialogs.
   */
  private void initialisePopupDialogs() {
    levelCompleteDialog = new PopupDialog(PopupDialog.DialogState.LEVEL_COMPLETE, this);
    timerExpiryDialog = new PopupDialog(PopupDialog.DialogState.TIME_EXPIRED, this);
    deathDialog = new PopupDialog(PopupDialog.DialogState.DEATH, this);
    nextButton = levelCompleteDialog.getNextButton();
    levelCompleteRestartButton = levelCompleteDialog.getRestartButton();
    timerExpiryRestartButton = timerExpiryDialog.getRestartButton();
    deathRestartButton = deathDialog.getRestartButton();
    levelCompleteDialog.setVisible(false);
    timerExpiryDialog.setVisible(false);
    deathDialog.setVisible(false);
  }

  /**
   * Listen to menu buttons in menu bar, recnplay buttons and execute.
   *
   * @param e the action event
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    frame.requestFocusInWindow();

    //MAZE FUNCTIONALITY

    //resume
    if (e.getSource() == menuBar.getResumeMenuItem()) {
      resume();
      pausedIconLabel.setVisible(false);

      //pause
    } else if (e.getSource() == menuBar.getPauseMenuItem()) {
      pause(true);
      pausedIconLabel.setVisible(true);

      //restart unfinished level
    } else if (e.getSource() == menuBar.getRestartCurrentLevelMenuItem()) {
      restartUnfinishedLevel();

      //restart the first level
    } else if (e.getSource() == menuBar.getStartFirstLevelMenuItem()) {
      restartFirstLevel();

      //undo
    } else if (e.getSource() == menuBar.getUndoMenuItem()) {
      undoGui();
      System.out.println("Undo activated");

      //exit and save
    } else if (e.getSource() == menuBar.getExitSaveMenuItem()) {
      System.out.println("ctrl + s pressed - exit and save");
      pause(false);
      Persistence.quickSave(maze);
      displayExitOptionPanel("Are you sure you want to exit? \n"
              + "resume current state ", "ctrl + s");

      //exit
    } else if (e.getSource() == menuBar.getExitMenuItem()) {
      displayExitOptionPanel("Are you sure you want to exit? \n"
              + "All unsaved progress will be lost ", null);

      //PERSISTENCE FUNCTIONALITY

      //save
    } else if (e.getSource() == menuBar.getSaveMenuItem()) {
      pause(false);
      Persistence.saveMaze(maze, openFileChooser(false));
      resume();

      //load
    } else if (e.getSource() == menuBar.getLoadMenuItem()) {
      pause(false);
      File loadedFile = openFileChooser(true);
      if (loadedFile != null) {
        loadLevel(Persistence.loadMaze(loadedFile));
      }
      resume();

      //RECNPLAY FUNCTIONALITIES

      //start or stop recording game play
    } else if (e.getSource() == menuBar.getStartStopRecordingMenuItem()) {

      //pause the game
      pause(false);

      switch (recnplay.getState()) {
        case SLEEPING:
          //save game state when a recording begins
          File file = openFileChooser(false);
          if (file != null) {

            //quick save game state
            Persistence.saveMaze(maze, file);

            //set the save file in recnplay
            recnplay.setSaveFile(file);

            //set the new state in recnplay
            recnplay.getState().startStopRecording();

            //display recording icon
            recordingIconLabel.setVisible(true);
          }
          break;
        case RECORDING:
          //load game state from recSaveFile
          Maze loaded = Persistence.loadMaze(recnplay.getSaveFile());

          //set moves in loaded maze with moves in recnplay
          loaded.setMovesByTime(recnplay.getMovesByTime());

          //write game state with moves
          Persistence.saveMaze(loaded, recnplay.getSaveFile());

          //set the new state in recnplay
          recnplay.getState().startStopRecording();

          //hide recording icon
          recordingIconLabel.setVisible(false);

          break;
        default:
          break;
      }
      resume();


      //play recording
    } else if (e.getSource() == menuBar.getStartStopReplayMenuItem()) {
      recnplay.playRecording();
      recnplay.getState().startStopReplay();


      //load recording
    } else if (e.getSource() == menuBar.getLoadRecordingMenuItem()) {
      pause(false);
      //quick save current game
      Persistence.quickSave(maze);
      //choose a file to save recording to
      File file = openFileChooser(true);
      if (file != null) {
        //load game state
        Maze loaded = Persistence.loadMaze(file);
        //set the game state
        this.loadLevel(loaded);
        //load the recording in recnplay
        recnplay.getState().loadRecording(maze.getMovesByTime());
      } else {
        resume();
      }


      //instructions
    } else if (e.getSource() == menuBar.getShowInstructMenuItem()) {
      pause(true);
      instructionsFrame.setVisible(true);
    }

    //recnplay button actions

    if (e.getSource() == nextFrameButton) {
      recnplay.nextFrame();
    } else if (e.getSource() == lastFrameButton) {
      recnplay.lastFrame();
    } else if (e.getSource() == autoPlayButton) {
      recnplay.setPlaybackSpeed(1);
      recnplay.playRecording();
      recnplay.getState().startStopReplay();
    } else if (e.getSource() == slowerReplayButton) {
      recnplay.setPlaybackSpeed(1.75);
    } else if (e.getSource() == standardReplayButton) {
      recnplay.setPlaybackSpeed(1);
    } else if (e.getSource() == fasterReplayButton) {
      recnplay.setPlaybackSpeed(0.5);
    }


    //popup dialog button actions
    if (e.getSource() == nextButton) {
      System.out.println("Next button pressed");
      Maze newMaze = Persistence.loadMaze(Main.level2);
      newMaze.setLevelID(2);
      loadLevel(newMaze);
      levelCompleteDialog.setVisible(false);
    }
    if (e.getSource() == levelCompleteRestartButton
            || e.getSource() == timerExpiryRestartButton
            || e.getSource() == deathRestartButton) {
      System.out.println("Restart button pressed");
      restartUnfinishedLevel();
      levelCompleteDialog.setVisible(false);
      timerExpiryDialog.setVisible(false);
      deathDialog.setVisible(false);
    }
  }

  /**
   * Moves chap, tells recnplay to record the move.
   * if game play is being recorded
   *
   * @param direction the movement direction
   */
  public void move(Maze.Direction direction) {
    maze.move(direction);

    if (recnplay.getState() == RecordAndReplay.State.RECORDING) {

      //save chaps id as -1 so enemies can be saved by their index, (0, 1, 2, 3, etc...)
      Move move = new Move(-1, direction.ordinal());

      //add the move to the current collection of moves i.e. the current recording
      recnplay.addMove(move);
    }
  }

  /**
   * Executes a move on the maze from a recording.
   *
   * @param move the move to be executed
   * @param undo indicates if this move should be undone or applied
   */
  public void executeMove(Move move, boolean undo) {
    //get the direction from the moves ordinal value (this is to follow dependency diagram)
    Maze.Direction direction = Maze.Direction.values()[move.direction];

    if (move.actorId == -1) {
      if (undo) {
        this.undoGui();
      } else {
        //move chap
        maze.move(direction);
      }
    } else {
      if (undo) {
        //invert move direction
        int opposite = move.direction == 0 || move.direction == 2
                ? move.direction + 1 : move.direction - 1;
        direction = Maze.Direction.values()[opposite];
      }
      //move enemy
      maze.moveEnemy(maze.getEnemies().get(move.actorId), direction);
    }
  }

  /**
   * Key listener to detect keys and key strokes.
   */
  private void setupKeyListener() {
    frame.addKeyListener(new KeyListener() {

      @Override
      public void keyPressed(KeyEvent e) {
        int key = e.getExtendedKeyCode();
        if (!isTimerActive && !e.isControlDown()
                && recnplay.getState() == RecordAndReplay.State.SLEEPING) {
          resume();
          isTimerActive = true;
          try {
            timer.scheduleAtFixedRate(timerTask, 0, 1); // start the timer countdown
          } catch (IllegalStateException ignored) {
            System.out.println();
          }
        }
        showInfoFieldToGui(false);

        // pause and resume
        if (key == KeyEvent.VK_SPACE) {
          pause(true);
          pausedIconLabel.setVisible(true);
        } else if (key == KeyEvent.VK_ESCAPE) {
          resume();
          pausedIconLabel.setVisible(false);
        }

        // movement
        if (isPaused || maze.isLevelFinished()) {
          return;
        }
        switch (key) {
          case KeyEvent.VK_UP:
            move(Maze.Direction.UP);
            break;
          case KeyEvent.VK_DOWN:
            move(Maze.Direction.DOWN);
            break;
          case KeyEvent.VK_LEFT:
            move(Maze.Direction.LEFT);
            break;
          case KeyEvent.VK_RIGHT:
            move(Maze.Direction.RIGHT);
            break;
          default:
        }

        decrementTreasurePickUp();
        board.repaint();

        // shortcuts

        //exit and save at last unfinished level
        if (e.isControlDown() && key == KeyEvent.VK_X) {
          System.out.println("ctrl + x pressed - exit game");
          pause(false);
          displayExitOptionPanel("Are you sure you want to exit? \n"
                  + "Resume at last unfinished level ", "ctrl + x");

          //exit and save
        } else if (e.isControlDown() && key == KeyEvent.VK_S) {
          System.out.println("ctrl + s pressed - exit and save");
          pause(false);
          Persistence.quickSave(maze);
          displayExitOptionPanel("Are you sure you want to exit? \n"
                  + "Resume current state ", "ctrl + s");

          //resume a saved game
        } else if (e.isControlDown() && key == KeyEvent.VK_R) {
          System.out.println("ctrl + r pressed - resume saved game");
          pause(false);
          Maze loadedMaze = Persistence.quickLoad();
          if (loadedMaze != null) {
            loadLevel(loadedMaze);
          }
          resume();

          //restart unfinished level
        } else if (e.isControlDown() && key == KeyEvent.VK_P) {
          System.out.println("ctrl + p pressed - start new game at last unfinished level");
          restartUnfinishedLevel();

          //restart from level 1
        } else if (e.isControlDown() && key == KeyEvent.VK_1) {
          restartFirstLevel();
          System.out.println("ctrl + 1 pressed - start new game at level 1");

          //undo last move
        } else if (key == KeyEvent.VK_A) {
          undoGui();
          System.out.println("Undo activated");
        }

      }

      // dead code
      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public void keyReleased(KeyEvent e) {
      }
    });
  }

  /**
   * Setup the timer.
   */
  private void setupTimer() {
    timer = new Timer();
    timeValueLabel.setForeground(Color.BLACK);
    timerTask = new TimerTask() {
      @Override
      public void run() {

        //get time remaining
        long millisLeft = maze.getMillisecondsLeft();

        if (millisLeft > 0) {
          //decrement milliseconds left
          millisLeft--;
          maze.setMillisecondsLeft(millisLeft);

          //first two digits of the remaining time
          String value = millisLeft > 10
                  ? Long.toString(millisLeft).substring(0, 2) : Long.toString(millisLeft);

          //if its the last ten seconds
          if (millisLeft < 9999) {

            //set the label colour to red
            timeValueLabel.setForeground(Color.RED);

            //if its the last ten seconds but not the last second
            if (millisLeft > 999) {
              value = value.charAt(0) + "." + value.charAt(1);
            } else {
              value = "0." + value.charAt(0);
            }
          }

          //set the label
          timeValueLabel.setText(value);

          //if time runs out
          if (millisLeft == 0) {
            pause(false);
            timerExpiryDialog.setVisible(true);
          }
        }
      }
    };
  }

  /**
   * Pause the game, triggering countdown timer and maze to stop.
   *
   * @param showDialog boolean confirming if dialog should be shown.
   */
  public void pause(boolean showDialog) {
    if (!isPaused) {
      isPaused = true;
      pausedIconLabel.setVisible(true);
      maze.pause();
      timer.cancel();
      if (showDialog) {
        JOptionPane.showOptionDialog(frame, "Press esc to resume ",
                "Game Paused", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, cl.pausedIcon,
                null, null);
      }
    }
  }

  /**
   * Resume the game, triggering countdown timer and maze to resume.
   */
  public void resume() {
    if (isPaused) {
      pausedIconLabel.setVisible(false);
      setupTimer();
      timer.scheduleAtFixedRate(timerTask, 0, 1); // start the timer countdown
      maze.resume();
      isPaused = false;
    }
  }

  /**
   * Quick loads a level using persistence quick load.
   * this method is used by recnplay and needed
   * to adhere to package dependencies
   */
  public void quickLoadLevel() {
    //quick load maze
    Maze loaded = Persistence.quickLoad();

    //make sure the file is valid
    if (loaded != null) {
      this.loadLevel(loaded);
      resume();
      pause(true);
    }
  }

  /**
   * Restart the current level.
   *
   * @param maze the maze to be loaded
   */
  public void loadLevel(Maze maze) {
    //reset timer count
    pause(false);
    timer.cancel();
    isTimerActive = false;
    setupTimer();

    //reset treasures amount and inventory
    setTreasuresValueLabel(maze.numTreasures());
    clearInventoryPanel();

    //make sure right color of treasures left upon reloading
    if (maze.getTreasures().isEmpty()) {
      treasuresValueLabel.setForeground(Color.GREEN);
    } else {
      treasuresValueLabel.setForeground(Color.BLACK);
    }

    //make sure right color of time left upon reloading
    if (maze.getMillisecondsLeft() <= 11000) {
      timeValueLabel.setForeground(Color.RED);
      timeValueLabel.revalidate();
    }

    //reset state of board/maze back to start of level
    reinitialiseBoard(maze);

    isTimerActive = false;
    levelValueLabel.setText(String.valueOf(maze.getLevelID()));
    timeValueLabel.setText(Long.toString(maze.getMillisecondsLeft() / 1000));
    pausedIconLabel.setVisible(false);
    frame.revalidate();
  }

  /**
   * Decrement treasure pickup value.
   */
  public void decrementTreasurePickUp() {
    setTreasuresValueLabel(maze.getTreasures().size());

    //treasures are all collected
    if (maze.getTreasures().isEmpty()) {
      treasuresValueLabel.setForeground(Color.GREEN);
    } else {
      treasuresValueLabel.setForeground(Color.BLACK);
    }
  }

  /**
   * Reinitialise the boardview.
   *
   * @param maze the maze that the boardview is reinitialised.
   */
  public void reinitialiseBoard(Maze maze) {
    this.maze = maze;
    board = new BoardView(maze);
    board = boardPanel.getBoard();
    board.reset(maze);

    //reinitialise labels so they are visible upon trigger again
    infoFieldLabel = boardPanel.getInfoFieldLabel();
    infoFieldTextLabel = boardPanel.getInfoFieldTextLabel();
    recordingIconLabel = boardPanel.getRecordingIconLabel();
    pausedIconLabel = boardPanel.getPausedIconLabel();
    replayingIconLabel = boardPanel.getReplayingIconLabel();

    maze.addListener(this);
    clearInventoryPanel();
    reloadInventoryPanel();
  }

  /**
   * Restart the current unfinished level.
   */
  public void restartUnfinishedLevel() {
    if (maze.getLevelID() == 1) {
      restartFirstLevel();
    } else {
      Maze newMaze = Persistence.loadMaze(Main.level2);
      newMaze.setLevelID(2);
      loadLevel(newMaze);
    }
  }

  /**
   * Restart level 1.
   */
  public void restartFirstLevel() {
    Maze newMaze = Persistence.loadMaze(Main.level1);
    newMaze.setLevelID(1);
    loadLevel(newMaze);
  }

  /**
   * Execute undo and reflect changes in gui.
   */
  public void undoGui() {
    maze.getUndoRedo().undo();
    clearInventoryPanel();
    reloadInventoryPanel();
    decrementTreasurePickUp();
  }

  /**
   * Open the file chooser and set the file field.
   *
   * @param isLoad boolean confirming loading or saving (not loading)
   * @return target File object, or null is no file was chosen
   */
  public File openFileChooser(boolean isLoad) {
    int result = -1;
    JFileChooser fc = new JFileChooser(Paths.get(".", "levels").toFile());
    if (isLoad) { //is loading the file
      result = fc.showOpenDialog(null);
    } else { //is saving the file
      result = fc.showSaveDialog(null);
    }
    if (result == JFileChooser.APPROVE_OPTION) {
      return fc.getSelectedFile();
    }
    return null;
  }

  /**
   * Find, add and display the key image to the inventory gui.
   *
   * @param key the key to be displayed in the inevntory gui
   */
  private void addKeyToInventoryPanel(Key key) {
    Image keyImage;
    try {
      keyImage = ImageIO.read(new File(key.getFilename()));
      ImageIcon keyIcon = new ImageIcon(
              keyImage.getScaledInstance(50, 50, Image.SCALE_DEFAULT));
      for (JLabel inventoryValueLabel : inventoryValueLabels) {
        if (inventoryValueLabel.getText().equals(" ")) { // check label is empty
          inventoryValueLabel.setText(key.getColor().name()); // identify as non-empty label
          inventoryValueLabel.setIcon(keyIcon);
          frame.revalidate();
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Clear the whole inventory panel.
   */
  public void clearInventoryPanel() {
    for (JLabel inventoryValueLabel : inventoryValueLabels) {
      inventoryValueLabel.setText(" "); // set label to empty again
      inventoryValueLabel.setIcon(null); // remove the icon (display nothing)
    }
    frame.revalidate();
  }

  /**
   * Reload the inventory panel gui.
   */
  public void reloadInventoryPanel() {
    List<Key> inventory = maze.getChap().getKeys();
    for (Key key : inventory) {
      addKeyToInventoryPanel(key);
    }
  }

  /**
   * Get the frame.
   *
   * @return the JFrame representing the container for the gui
   */
  public JFrame getFrame() {
    return frame;
  }

  /**
   * Get the board.
   *
   * @return the JComponent representing the board.
   */
  public BoardView getBoard() {
    return board;
  }

  /**
   * Get the maze.
   *
   * @return the maze
   */
  public Maze getMaze() {
    return maze;
  }

  /**
   * Get timer active.
   *
   * @return the boolean confirming that the time is active
   */
  public boolean getIsTimerActive() {
    return isTimerActive;
  }

  /**
   * Get the time stamp.
   *
   * @return the timestamp
   */
  public Long getCurrentTimeStamp() {
    return maze.getMillisecondsLeft();
  }

  /**
   * Set the treasures value text of the label.
   *
   * @param treasuresValue the String representing the treasures value to be set
   */
  public void setTreasuresValueLabel(int treasuresValue) {
    treasuresValueLabel.setText(String.valueOf(treasuresValue));
    frame.revalidate();
  }

  /**
   * Get the menu bar object to update text displayed in the menus.
   */
  public MenuBar getMenuBar() {
    return this.menuBar;
  }

  /**
   * Get the replaying icon label so recnplay can show or hide it.
   */
  public JLabel getReplayingIconLabel() {
    return replayingIconLabel;
  }

  /**
   * Set the info field label text.
   *
   * @param text the desired text to be entered into the text label
   */
  public void setInfoFieldTextLabel(String text) {
    infoFieldTextLabel.setText(text);
    frame.revalidate();
  }

  /**
   * Display the info field to the gui.
   *
   * @param confirmation boolean confirming infofield is shown/hidden
   */
  public void showInfoFieldToGui(boolean confirmation) {
    infoFieldLabel.setVisible(confirmation);
    infoFieldTextLabel.setVisible(confirmation);
    frame.revalidate();
  }

  /**
   * Update enemies moves and add them to recnplay.
   *
   * @param e the maze event enemy walked event
   */
  @Override
  public void update(MazeEventEnemyWalked e) {
    int enemyId = maze.getEnemies().indexOf(e.getEnemy());
    if (recnplay.getState() == RecordAndReplay.State.RECORDING) {
      Move move = new Move(enemyId, e.getEnemyDirection().ordinal());
      recnplay.addMove(move);
    }
  }

  /**
   * Update the inventory when we pick up a key.
   *
   * @param e the key pickup event
   */
  @Override
  public void update(MazeEventPickup e) {
    if (e.getPicked() instanceof Key) {
      Key key = (Key) e.getPicked();
      addKeyToInventoryPanel(key);
    }
  }

  /**
   * Update the inventory when we open a door.
   *
   * @param e the door open event
   */
  @Override
  public void update(MazeEventUnlocked e) {
    if (e.getDoor().getColor() == KeyColor.GREEN) {
      return; // temporary fix
    }
    for (JLabel inventoryValueLabel : inventoryValueLabels) {
      if (inventoryValueLabel.getText().equals(e.getDoor().getColor().name())) {
        inventoryValueLabel.setText(" "); // set label to empty again
        inventoryValueLabel.setIcon(null); // remove the icon (display nothing)
        frame.revalidate();
        break;
      }
    }
  }

  /**
   * Update the gui to show/hide info field.
   *
   * @param e the info field activation event
   */
  @Override
  public void update(MazeEventInfoField e) {
    String info;
    switch (maze.getLevelID()) {
      case 1:
        info = "<html>Collect pickaxes, mine <br>the blocks, "
                + "collect the<br> emeralds to open and <br>reach the portal!</html>";
        break;
      case 2:
        info = "<html>Push the blocks into <br>the water and avoid <br>enemies!</html>";
        break;
      default:
        info = "PLEASE, PROGRAM LET ME DO IT. I AM THE PROGRAMMER, I AM THE KING HERE. "
                + "PLEASE LET ME DO IT";
    }
    infoFieldTextLabel.setText(info);
    infoFieldLabel.setBounds(board.getX() - 175, board.getY() - 150, 1000, 1000);
    infoFieldTextLabel.setBounds(infoFieldLabel.getX() + 300,
            infoFieldLabel.getY() - 150, 1000, 1000);
    infoFieldTextLabel.setFont(cl.infoFont);
    frame.revalidate();
    showInfoFieldToGui(true);
  }

  /**
   * Update operations when maze has been won.
   *
   * @param e the maze won event
   */
  @Override
  public void update(MazeEventWon e) {
    //stop the timer
    pause(false);
    levelCompleteDialog.setVisible(true);
    timer.cancel();
    timer.purge();
  }

  /**
   * Update death upon chap walking into an enemy.
   *
   * @param e the walked and killed event
   */
  @Override
  public void update(MazeEventWalkedKilled e) {
    pause(false);
    deathDialog.setVisible(true);
  }

  /**
   * Update death upon enemy walking into chap.
   *
   * @param e the enemy walked and killed event
   */
  @Override
  public void update(MazeEventEnemyWalkedKilled e) {
    pause(false);
    deathDialog.setVisible(true);
  }

  /**
   * Update death upon chap walking into water.
   *
   * @param e the walked and drowned event.
   */
  @Override
  public void update(MazeEventWalkedDrowned e) {
    pause(false);
    deathDialog.setVisible(true);
  }

  /**
   * Display an option panel that exits the frame.
   *
   * @param message          the String to display a message
   * @param operationOnClose the String representing an operation to execute before closing
   */
  private void displayExitOptionPanel(String message, String operationOnClose) {
    JFrame temp = new JFrame(); //if player wants to exit while optionpane is active
    temp.setAlwaysOnTop(true);
    int response = JOptionPane.showConfirmDialog(temp, message,
            "Exit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
            cl.exitIcon);
    if (response == JOptionPane.YES_OPTION) {
      if (operationOnClose == null) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.dispose();
        System.exit(0);
      }
      switch (Objects.requireNonNull(operationOnClose)) {
        case "ctrl + x":
          restartUnfinishedLevel();
          Persistence.quickSave(maze);
          break;
        case "ctrl + s":
          Persistence.quickSave(maze);
          break;
        default:
      }
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.dispose();
      System.exit(0);
    } else {
      frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      if (isTimerActive) {
        resume();
      }
    }
  }

  /**
   * Initialise the window listener.
   */
  private void initialiseWindowListener() {
    frame.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        pause(false);
        displayExitOptionPanel("Are you sure you want to exit? \n"
                + "All unsaved progress will be lost ", null);
      }
    });
  }
}
