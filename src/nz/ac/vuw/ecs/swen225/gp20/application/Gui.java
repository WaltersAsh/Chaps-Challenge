package nz.ac.vuw.ecs.swen225.gp20.application;

import static nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence.loadMaze;
import static nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence.saveMaze;

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
import java.util.List;
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
import nz.ac.vuw.ecs.swen225.gp20.maze.PathTile;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventEnemyWalked;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventInfoField;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventListener;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventPickup;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventUnlocked;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventWon;
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

  private JButton nextButton;
  private JButton levelCompleteRestartButton;
  private JButton timerExpiryRestartButton;

  public static BoardView board;
  private Maze maze;
  private Timer timer;
  private TimerTask timerTask;
  private boolean isTimerActive;
  private int[] secondsLeft;
  private boolean isPaused;
  private File currentLevel = Main.level1;

  private RecordAndReplay recnplay;

  /**
   * Construct the GUI: frame, panels, labels, menus, button listeners.
   */
  public Gui(Maze maze) {
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
    levelCompleteDialog = new PopupDialog(true, this);
    timerExpiryDialog = new PopupDialog(false, this);
    nextButton = levelCompleteDialog.getNextButton();
    levelCompleteRestartButton = levelCompleteDialog.getRestartButton();
    timerExpiryRestartButton = timerExpiryDialog.getRestartButton();
    levelCompleteDialog.setVisible(false);
    timerExpiryDialog.setVisible(false);
  }

  /**
   * Listen to menu buttons in menu bar, recnplay buttons and execute.
   *
   * @param e the action event
   */
  @Override
  public void actionPerformed(ActionEvent e) {

    //menu actions

    //maze functionality
    if (e.getSource() == menuBar.getResumeMenuItem()) {
      resume();
      pausedIconLabel.setVisible(false);
    } else if (e.getSource() == menuBar.getPauseMenuItem()) {
      pause();
      pausedIconLabel.setVisible(true);
    } else if (e.getSource() == menuBar.getRestartCurrentLevelMenuItem()) {
      if (currentLevel == Main.level1) {
        loadLevel(loadMaze(Main.level1));
      } else {
        loadLevel(loadMaze(Main.level2));
      }
    } else if (e.getSource() == menuBar.getExitMenuItem()) {
      frame.dispose();

      //persistence loading and saving
    } else if (e.getSource() == menuBar.getSaveMenuItem()) {
      pause();
      saveMaze(maze, openFileChooser(false));
      resume();
    } else if (e.getSource() == menuBar.getLoadMenuItem()) {
      pause();
      loadLevel(loadMaze(openFileChooser(true)));
      resume();

      //recnplay menu functionalities

      //start recording game play
    } else if (e.getSource() == menuBar.getStartRecordingMenuItem()) {
      pause();
      RecordAndReplay.startRecording(openFileChooser(false));
      recordingIconLabel.setVisible(true);
      resume();

      //stop recording game play
    } else if (e.getSource() == menuBar.getStopRecordingMenuItem()
            && RecordAndReplay.isRecording()) {
      pause();
      RecordAndReplay.stopRecording();
      recordingIconLabel.setVisible(false);
      resume();

      //play recording
    } else if (e.getSource() == menuBar.getPlayMenuItem()) {
      pause();
      recnplay.playRecording();
      resume();

      //stop playing recording
    } else if (e.getSource() == menuBar.getStopPlayMenuItem()) {

    } else if (e.getSource() == menuBar.getLoadRecordingMenuItem()) {
      pause();
      RecordAndReplay.loadRecording(openFileChooser(true));
      resume();

      //instructions
    } else if (e.getSource() == menuBar.getShowInstructMenuItem()) {
      pause();
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
      currentLevel = Main.level2;
      loadLevel(loadMaze(currentLevel));
      levelCompleteDialog.setVisible(false);
    }
    if (e.getSource() == levelCompleteRestartButton || e.getSource() == timerExpiryRestartButton) {
      System.out.println("Restart button pressed");
      if (currentLevel == Main.level1) {
        loadLevel(loadMaze(Main.level1));
      } else {
        loadLevel(loadMaze(Main.level2));
      }
      levelCompleteDialog.setVisible(false);
      timerExpiryDialog.setVisible(false);
    }
  }

  /**
   * Moves chap.
   *
   * @param direction the movement direction
   */
  public void move(Maze.Direction direction) {
    maze.move(direction);
    if (RecordAndReplay.isRecording()) {
      this.maze.moves.add(direction);
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
            timer.schedule(timerTask, 0, 1000); // start the timer countdown
          } catch (IllegalStateException ignored) {
            System.out.println();
          }
        }
        showInfoFieldToGui(false);

        // pause and resume
        if (key == KeyEvent.VK_SPACE) {
          pause();
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
        if (e.isControlDown() && key == KeyEvent.VK_X) {
          System.out.println("ctrl + x pressed - exit game");
        } else if (e.isControlDown() && key == KeyEvent.VK_S) {
          System.out.println("ctrl + s pressed - exit and save");
        } else if (e.isControlDown() && key == KeyEvent.VK_R) {
          System.out.println("ctrl + r pressed - resume saved game");
        } else if (e.isControlDown() && key == KeyEvent.VK_P) {
          System.out.println("ctrl + p pressed - start new game at last unfinished level");
          if (currentLevel == Main.level1) {
            loadLevel(loadMaze(Main.level1));
          } else {
            loadLevel(loadMaze(Main.level2));
          }
        } else if (e.isControlDown() && key == KeyEvent.VK_1) {
          currentLevel = Main.level1;
          loadLevel(loadMaze(currentLevel));
          System.out.println("ctrl + 1 pressed - start new game at level 1");
        } else if (key == KeyEvent.VK_A) {
          System.out.println("a pressed - undo");
        } else if (key == KeyEvent.VK_D) {
          System.out.println("d pressed - redo");
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
    secondsLeft = new int[]{Integer.parseInt(timeValueLabel.getText())};
    timer = new Timer();
    timeValueLabel.setForeground(Color.BLACK);
    timerTask = new TimerTask() {
      @Override
      public void run() {
        if (secondsLeft[0] > 0) {
          secondsLeft[0]--;
          setTimeValueLabel(secondsLeft[0]);
        }
        //timer drops down to last 10
        if (secondsLeft[0] <= 10) {
          timeValueLabel.setForeground(Color.RED);
        }
        //timer expires
        if (secondsLeft[0] == 0) {
          pause();
          timerExpiryDialog.setVisible(true);
        }
      }
    };
  }

  /**
   * Pause the game, triggering countdown timer and maze to stop.
   */
  public void pause() {
    isPaused = true;
    pausedIconLabel.setVisible(true);
    maze.pause();
    timer.cancel();
  }

  /**
   * Resume the game, triggering countdown timer and maze to resume.
   */
  public void resume() {
    if (isPaused) {
      pausedIconLabel.setVisible(false);
      maze.resume();
      setupTimer();
      timer.schedule(timerTask, 0, 1000); // start the timer countdown
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
    if (currentLevel == Main.level2) {
      setTimeValueLabel(40);
      levelValueLabel.setText("2");
    } else {
      levelValueLabel.setText("1");
      setTimeValueLabel(60);
    }
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
   * Set the level value text of the label.
   *
   * @param levelValue the String representing the level value to be set
   */
  public void setLevelValueLabel(String levelValue) {
    levelValueLabel.setText(levelValue);
    frame.revalidate();
  }

  /**
   * Set the time value text of the label.
   *
   * @param timeValue the String representing the time value to be set
   */
  public void setTimeValueLabel(int timeValue) {
    timeValueLabel.setText(String.valueOf(timeValue));
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
    infoFieldLabel.setBounds(board.getX() - 175, board.getY() - 150, 1000, 1000);
    infoFieldTextLabel.setBounds(infoFieldLabel.getX() + 300,
        infoFieldLabel.getY() - 150, 1000, 1000);
    frame.revalidate();
    showInfoFieldToGui(true);
  }

  /**
   * Update tracking when chap gets consumed by an enemy.
   *
   * @param e the enemy movement event
   */
  @Override
  //TODO: TO IMPLEMENT
  public void update(MazeEventEnemyWalked e) {
    PathTile enemy = e.getEnemyOrigin();
    PathTile chap = e.getEnemyTarget();
    if (enemy == chap) {
      System.out.println("Test");
    }
  }

  /**
   * Execute operations when maze has been won.
   *
   * @param e the maze won event
   */
  @Override
  public void update(MazeEventWon e) {
    //stop the timer
    pause();
    levelCompleteDialog.setVisible(true);
    timer.cancel();
    timer.purge();
  }

  /**
   * Initialise the window listener.
   */
  public void initialiseWindowListener() {
    frame.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        pause();
        int response = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?",
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
    });
  }
}
