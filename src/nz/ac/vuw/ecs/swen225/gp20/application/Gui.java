package nz.ac.vuw.ecs.swen225.gp20.application;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import nz.ac.vuw.ecs.swen225.gp20.maze.Key;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.KeyColor;
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
 * @author Justin 300470389
 */
@JsonIgnoreType
public class Gui extends MazeEventListener implements ActionListener {
  // frame and main panels
  private final JFrame frame;
  private JPanel framePanel;
  public static BoardPanel boardPanel;
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

  private JButton nextButton;
  private JButton levelCompleteRestartButton;
  private JButton timerExpiryRestartButton;
  private JButton deathRestartButton;

  public static BoardView board;
  private Maze maze;
  private Timer timer;
  private TimerTask timerTask;
  private boolean isTimerActive;
  private boolean isNewLevel;
  private boolean isPaused;
  private int currentLevel = 1;

  private RecordAndReplay recnplay;
  private Map<Long, List<Move>> timeToMoveMap = new HashMap<>();

  /**
   * Construct the GUI: frame, panels, labels, menus, button listeners.
   */
  public Gui(Maze maze) {
    this.maze = maze;
    isNewLevel = true;

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

    // initialise frame
    frame.add(framePanel);
    frame.add(recnplayControlsPanel, BorderLayout.SOUTH);
    frame.setJMenuBar(menuBar);
    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    frame.setTitle("Chap's Challenge");
    Dimension dimen = Toolkit.getDefaultToolkit().getScreenSize();
    frame.pack();
    frame.setSize(1024, 800);
    frame.setMinimumSize(new Dimension(875, 675));
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
  public void createFramePanel() {
    framePanel = new JPanel();
    framePanel.setLayout(new BoxLayout(framePanel, BoxLayout.X_AXIS));
    framePanel.setBackground(ComponentLibrary.lightLavender);
    framePanel.setBorder(new EmptyBorder(50, 50, 50, 50));
  }

  /**
   * Initialise the side panel of the gui.
   */
  public void initialiseSidePanel() {
    sidePanel = new SidePanel(maze);
    levelValueLabel = sidePanel.getLevelValueLabel();
    timeValueLabel = sidePanel.getTimeValueLabel();
    treasuresValueLabel = sidePanel.getTreasuresValueLabel();
    inventoryValueLabels = sidePanel.getInventoryValueLabels();
  }

  /**
   * Initialise the popup dialogs.
   */
  public void initialisePopupDialogs() {
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
      if (currentLevel == 1) {
        loadLevel(Persistence.loadMaze(Main.level1));
      } else {
        loadLevel(Persistence.loadMaze(Main.level2));
      }

      //exit
    } else if (e.getSource() == menuBar.getExitMenuItem()) {
      frame.dispose();

      //PERSISTENCE FUNCTIONALITY

      //save
    } else if (e.getSource() == menuBar.getSaveMenuItem()) {
      pause(false);
      isNewLevel = false;
      Persistence.saveMaze(maze, openFileChooser(false));
      resume();

      //load
    } else if (e.getSource() == menuBar.getLoadMenuItem()) {
      pause(false);
      loadLevel(Persistence.loadMaze(openFileChooser(true)));
      resume();

      //RECNPLAY FUNCTIONALITIES

      //start recording game play
    } else if (e.getSource() == menuBar.getStartRecordingMenuItem()) {
      pause(false);
      RecordAndReplay.startRecording(openFileChooser(false));
      recordingIconLabel.setVisible(true);
      resume();

      //stop recording game play
    } else if (e.getSource() == menuBar.getStopRecordingMenuItem()
            && RecordAndReplay.isRecording()) {
      pause(false);
      RecordAndReplay.stopRecording();
      recordingIconLabel.setVisible(false);
      resume();

      //play recording
    } else if (e.getSource() == menuBar.getPlayMenuItem()) {
      pause(false);
      recnplay.playRecording();
      resume();

      //stop playing recording
    } else if (e.getSource() == menuBar.getStopPlayMenuItem()) {

      //load recording
    } else if (e.getSource() == menuBar.getLoadRecordingMenuItem()) {
      pause(false);
      RecordAndReplay.loadRecording(openFileChooser(true));
      resume();

      //instructions
    } else if (e.getSource() == menuBar.getShowInstructMenuItem()) {
      pause(true);
      instructionsFrame.setVisible(true);
    }

    //recnplay button actions
    if (e.getSource() == nextFrameButton) {
      System.out.println("Next frame button pressed");
    } else if (e.getSource() == lastFrameButton) {
      System.out.println("Last frame button pressed");
    } else if (e.getSource() == autoPlayButton) {
      System.out.println("Auto play button pressed");
    } else if (e.getSource() == slowerReplayButton) {
      System.out.println("Slower replay button pressed");
    } else if (e.getSource() == standardReplayButton) {
      System.out.println("Standard replay button pressed");
    } else if (e.getSource() == fasterReplayButton) {
      System.out.println("Faster replay button pressed");
    }

    //popup dialog button actions
    if (e.getSource() == nextButton) {
      System.out.println("Next button pressed");
      currentLevel = 2;
      loadLevel(Persistence.loadMaze(Main.level2));
      levelCompleteDialog.setVisible(false);
    }
    if (e.getSource() == levelCompleteRestartButton
            || e.getSource() == timerExpiryRestartButton
            || e.getSource() == deathRestartButton) {
      System.out.println("Restart button pressed");
      if (currentLevel == 1) {
        loadLevel(Persistence.loadMaze(Main.level1));
      } else {
        loadLevel(Persistence.loadMaze(Main.level2));
      }
      levelCompleteDialog.setVisible(false);
      timerExpiryDialog.setVisible(false);
      deathDialog.setVisible(false);
    }
  }

  /**
   * Moves chap.
   *
   * @param direction the movement direction
   */
  public void move(Maze.Direction direction, KeyEvent keyEvent) {
    maze.move(direction);
    if (RecordAndReplay.isRecording()) {
      this.maze.moves.add(direction);
      Move move = new Move(1, keyEvent);
      long timestamp = maze.getMillisecondsLeft();
      if (timeToMoveMap.containsKey(timestamp)) {
        timeToMoveMap.get(timestamp).add(move);
      } else {
        List<Move> newList = new ArrayList<>();
        timeToMoveMap.put(timestamp, newList);
      }

    }
  }

  /**
   * Key listener to detect keys and key strokes.
   */
  public void setupKeyListener() {
    frame.addKeyListener(new KeyListener() {

      @Override
      public void keyPressed(KeyEvent e) {
        int key = e.getExtendedKeyCode();
        if (!isTimerActive && !e.isControlDown() && !RecordAndReplay.isRecording() && !isPaused) {
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
            move(Maze.Direction.UP, e);
            break;
          case KeyEvent.VK_DOWN:
            move(Maze.Direction.DOWN, e);
            break;
          case KeyEvent.VK_LEFT:
            move(Maze.Direction.LEFT, e);
            break;
          case KeyEvent.VK_RIGHT:
            move(Maze.Direction.RIGHT, e);
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
          displayExitOptionPanel("Are you sure you want to exit? (resume at last unfinished level");
          resume();

          //exit and save
        } else if (e.isControlDown() && key == KeyEvent.VK_S) {
          System.out.println("ctrl + s pressed - exit and save");
          pause(false);
          Persistence.quickSave(maze);
          displayExitOptionPanel("Are you sure you want to exit? (resume current state)");
          resume();

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
          if (currentLevel == 1) {
            loadLevel(Persistence.loadMaze(Main.level1));
          } else {
            loadLevel(Persistence.loadMaze(Main.level2));
          }

          //restart from level 1
        } else if (e.isControlDown() && key == KeyEvent.VK_1) {
          currentLevel = 1;
          loadLevel(Persistence.loadMaze(Main.level1));
          System.out.println("ctrl + 1 pressed - start new game at level 1");

          //undo last move
        } else if (key == KeyEvent.VK_A) {
          maze.getUndoRedo().undo();
          clearInventoryPanel();
          reloadInventoryPanel();
          decrementTreasurePickUp();
          System.out.println("Undo activated");

          //redo last move
        } else if (key == KeyEvent.VK_D) {
          maze.getUndoRedo().redo();
          clearInventoryPanel();
          reloadInventoryPanel();
          decrementTreasurePickUp();
          System.out.println("Redo activated");
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
  public void setupTimer() {
    timer = new Timer();
    timeValueLabel.setForeground(Color.BLACK);
    timerTask = new TimerTask() {
      @Override
      public void run() {
        if (maze.getMillisecondsLeft() > 0) {
          maze.setMillisecondsLeft(maze.getMillisecondsLeft() - 1);
          long millisecondsLeft = maze.getMillisecondsLeft();
          if (millisecondsLeft > 9) {
            timeValueLabel.setText(Long.toString(millisecondsLeft / 1000));
          }
          //timer drops down to last 10
          if (millisecondsLeft <= 11000) {
            timeValueLabel.setForeground(Color.RED);
          }
          //timer expires
          if (millisecondsLeft == 0) {
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
        JOptionPane.showOptionDialog(frame, "PAUSED - Press esc to resume",
            "Game Paused", JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE, ComponentLibrary.pausedIcon,
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
      maze.resume();
      setupTimer();
      timer.scheduleAtFixedRate(timerTask, 0, 1); // start the timer countdown
      isPaused = false;
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

    //reset treasures amount
    setTreasuresValueLabel(maze.numTreasures());

    //reset inventory
    for (JLabel inventoryValueLabel : inventoryValueLabels) {
      inventoryValueLabel.setText(" "); // set label to empty again
      inventoryValueLabel.setIcon(null); // remove the icon (display nothing)
      break;
    }

    //reset state of board/maze back to start of level
    reinitialiseBoard(maze);

    //level panel
    if (isNewLevel) {
      if (currentLevel == 2) {
        maze.setMillisecondsLeft(40000);
      } else {
        maze.setMillisecondsLeft(60000);
      }
    } else {
      currentLevel = maze.getLevelID();
    }
    levelValueLabel.setText(String.valueOf(currentLevel));
    timeValueLabel.setText(Long.toString(maze.getMillisecondsLeft() / 1000));
    pausedIconLabel.setVisible(false);
    setupTimer();
  }

  /**
   * Decrement treasure pickup value.
   */
  public void decrementTreasurePickUp() {
    int treasureCount = maze.getChap().getTreasures().size();
    setTreasuresValueLabel(maze.numTreasures() - treasureCount);

    //treasures are all collected
    if (maze.numTreasures() - treasureCount == 0) {
      treasuresValueLabel.setForeground(Color.GREEN);
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
    infoFieldLabel = boardPanel.getInfoFieldLabel();
    infoFieldTextLabel = boardPanel.getInfoFieldTextLabel();
    recordingIconLabel = boardPanel.getRecordingIconLabel();
    pausedIconLabel = boardPanel.getPausedIconLabel();
    maze.addListener(this);
    for (JLabel inventoryValueLabel : inventoryValueLabels) {
      inventoryValueLabel.setText(" "); // set label to empty again
      inventoryValueLabel.setIcon(null); // remove the icon (display nothing)
    }
    reloadInventoryPanel();
    treasuresValueLabel.setForeground(Color.BLACK);
    isTimerActive = false;
    isPaused = false;
    maze.resume();
  }

  /**
   * Open the file chooser and set the file field.
   *
   * @param isLoad boolean confirming loading or saving (not loading)
   * @return target File object, or null is no file was chosen
   */
  public File openFileChooser(boolean isLoad) {
    int result = -1;
    JFileChooser fc = new JFileChooser(Paths.get(".", "levels").toFile());;
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
  public void addKeyToInventoryPanel(Key key) {
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
    //reset whole inventory display
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
   * Set the level value text of the label.
   *
   * @param levelValue the String representing the level value to be set
   */
  public void setLevelValueLabel(String levelValue) {
    levelValueLabel.setText(levelValue);
    frame.revalidate();
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
    infoFieldTextLabel.setText(e.getInfoField().getInformation());
    infoFieldLabel.setBounds(board.getX() - 175, board.getY() - 150, 1000, 1000);
    infoFieldTextLabel.setBounds(infoFieldLabel.getX() + 300,
        infoFieldLabel.getY() - 150, 1000, 1000);
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
   * @param message the String to display a message
   */
  public void displayExitOptionPanel(String message) {
    JFrame temp = new JFrame(); //if player wants to exit while optionpane is active
    temp.setAlwaysOnTop(true);
    int response = JOptionPane.showConfirmDialog(temp, message,
            "Exit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (response == JOptionPane.YES_OPTION) {
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
  public void initialiseWindowListener() {
    frame.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        pause(false);
        displayExitOptionPanel("Are you sure you want to exit?");
      }
    });
  }
}
