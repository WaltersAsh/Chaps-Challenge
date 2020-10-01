package nz.ac.vuw.ecs.swen225.gp20.application;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import nz.ac.vuw.ecs.swen225.gp20.maze.Key;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.KeyColor;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.*;
import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.RecordAndReplay;
import nz.ac.vuw.ecs.swen225.gp20.rendering.BoardView;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence.saveMaze;

/**
 * Gui class for visual display of the game.
 *
 * @author Justin 300470389
 */
@JsonIgnoreType
public class Gui extends MazeEventListener implements ActionListener {
  // frame and main panels
  private JFrame frame;
  private JPanel framePanel;
  public static JLayeredPane boardPanel;
  private SidePanel sidePanel;
  private JPanel recnplayControlsPanel;

  // value labels
  private JLabel levelValueLabel;
  private JLabel timeValueLabel;
  private JLabel treasuresValueLabel;
  private JLabel[] inventoryValueLabels;

  // infofield label
  private JLabel infoFieldLabel;
  private JLabel infoFieldLabelText;

  //icon labels
  private JLabel recordingIconLabel;
  private JLabel pausedIconLabel;

  // menu bar and menu items
  private JMenuBar menuBar;

  private JMenu fileMenu;
  private JMenuItem saveMenuItem;
  private JMenuItem loadMenuItem;

  private JMenu gameMenu;
  private JMenuItem resumeMenuItem;
  private JMenuItem pauseMenuItem;
  private JMenuItem redoMenuItem;
  private JMenuItem undoMenuItem;
  private JMenuItem exitMenuItem;
  private JMenuItem exitSaveMenuItem;

  private JMenu levelMenu;
  private JMenuItem restartCurrentLevelMenuItem;
  private JMenuItem startFirstLevelMenuItem;

  private JMenu recnplayMenu;
  private JMenuItem playMenuItem;
  private JMenuItem stopPlayMenuItem;
  private JMenuItem startRecordingMenuItem;
  private JMenuItem stopRecordingMenuItem;
  private JMenuItem loadRecordingMenuItem;

  private JMenu helpMenu;
  private JMenuItem showInstructMenuItem;

  //buttons for controlling replaying in recnplay
  private JButton nextFrameButton;
  private JButton lastFrameButton;
  private JButton autoPlayButton;
  private JButton fasterReplayButton;
  private JButton slowerReplayButton;
  private JButton standardReplayButton;

  //Files
  private final JFileChooser fileChooser = new JFileChooser(Paths.get(".").toFile());

  public static BoardView board;
  private Maze maze;
  private Maze initialMaze;
  private Timer timer;
  private TimerTask timerTask;
  private boolean isTimerActive;
  private int[] secondsLeft;
  private boolean isPaused;

  public static RecordAndReplay recnplay;

  /**
   * Construct the GUI: frame, panels, labels, menus, button listeners.
   */
  public Gui(Maze maze){
    this.maze = maze;
    initialMaze = maze;
    recnplay = new RecordAndReplay(this);
    // base frame that all JComponents will be added to
    frame = new JFrame();
    frame.setLayout(new BorderLayout());
    createFramePanel();
    createBoardPanel();
    createInfoFieldLabel();

    sidePanel = new SidePanel(maze);
    timeValueLabel = sidePanel.getTimeValueLabel();
    treasuresValueLabel = sidePanel.getTreasuresValueLabel();
    inventoryValueLabels = sidePanel.getInventoryValueLabels();

    /*
    createSidePanel();
    createInnerSidePanels();
    initialiseInnerSidePanels();
     */
    createMenuComponents();
    createRecnplayControls();

    recordingIconLabel = ComponentLibrary.initialiseRecnplayIconLabel();
    pausedIconLabel = ComponentLibrary.initialisePauseIconLabel();

    board.setBounds(0, 0, 1000, 1000);
    boardPanel.add(board, JLayeredPane.DEFAULT_LAYER);
    boardPanel.add(infoFieldLabel, JLayeredPane.PALETTE_LAYER);
    boardPanel.add(infoFieldLabelText, JLayeredPane.MODAL_LAYER);
    boardPanel.add(recordingIconLabel, JLayeredPane.PALETTE_LAYER);
    boardPanel.add(pausedIconLabel, JLayeredPane.PALETTE_LAYER);

    // add menus to menu bars
    menuBar.add(fileMenu);
    menuBar.add(gameMenu);
    menuBar.add(levelMenu);
    menuBar.add(recnplayMenu);
    menuBar.add(helpMenu);

    // set up board and side panels into frame panel
    // framePanel.add(Box.createHorizontalGlue());
    framePanel.add(boardPanel);
    framePanel.add(Box.createRigidArea(new Dimension(20, 0)));
    framePanel.add(sidePanel);
    // framePanel.add(Box.createHorizontalGlue());

    // initialise frame
    frame.add(framePanel);
    frame.add(recnplayControlsPanel, BorderLayout.SOUTH);
    frame.setJMenuBar(menuBar);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Chap's Challenge");
    Dimension dimen = Toolkit.getDefaultToolkit().getScreenSize();
    frame.pack();
    frame.setSize(1024, 800);
    frame.setMinimumSize(new Dimension(875, 675));
    frame.setLocation(dimen.width / 2 - frame.getSize().width / 2,
        dimen.height / 2 - frame.getSize().height / 2);
    frame.setFocusable(true);
    setupTimer();
    setupKeyListener();
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
   * Create the board panel and rendered board.
   */
  public void createBoardPanel() {
    boardPanel = new JLayeredPane();
    maze.addListener(this);
    board = new BoardView(maze);
    boardPanel.setBackground(ComponentLibrary.lightLavender);
    boardPanel.setMinimumSize(
        new Dimension(board.getPreferredSize().width, board.getPreferredSize().height));
    boardPanel.setPreferredSize(
        new Dimension(board.getPreferredSize().width, board.getPreferredSize().height));
    boardPanel.setMaximumSize(new Dimension(800, 800));
  }

  /**
   * Create the info field label and text label.
   */
  public void createInfoFieldLabel() {
    try {
      Image sign = ImageIO.read(new File("resources/textures/gui/sign_large.png"));
      infoFieldLabel = new JLabel(
          new ImageIcon(sign.getScaledInstance(500, 500, Image.SCALE_DEFAULT)));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    infoFieldLabel.setBounds(-180, -90, 1000, 1000);
    infoFieldLabelText = new JLabel("swing is pain :(");
    infoFieldLabelText.setBounds(150, -225, 1000, 1000);
    infoFieldLabelText.setFont(ComponentLibrary.infoFont);

    infoFieldLabelText.setForeground(Color.BLACK);
    showInfoFieldToGui(false);
  }

  /**
   * Create the recnplay controls for navigating and interacting.
   */
  public void createRecnplayControls() {
    recnplayControlsPanel = new JPanel();
    recnplayControlsPanel.setBackground(ComponentLibrary.lightLavender);
    recnplayControlsPanel.setLayout(new BoxLayout(recnplayControlsPanel, BoxLayout.X_AXIS));

    JButton[] buttons = new JButton[]{
        lastFrameButton = new JButton("<"),
        autoPlayButton = new JButton("AUTO"),
        nextFrameButton = new JButton(">"),
        slowerReplayButton = new JButton("SLOWER"),
        standardReplayButton = new JButton("STANDARD"),
        fasterReplayButton = new JButton("FASTER")
    };

    for (JButton button : buttons) {
      button.setFont(ComponentLibrary.buttonFont);
      button.setForeground(Color.WHITE);
      button.setBackground(ComponentLibrary.lavender);
      button.addActionListener(this);
      recnplayControlsPanel.add(button);
      recnplayControlsPanel.add(Box.createRigidArea(new Dimension(50, 0)));
    }
  }

  /**
   * Create the menu bar, menus and menu items.
   */
  public void createMenuComponents() {
    menuBar = new JMenuBar();
    fileMenu = new JMenu("File");
    gameMenu = new JMenu("Game");
    levelMenu = new JMenu("Level");
    recnplayMenu = new JMenu("Rec'n'play");
    helpMenu = new JMenu("Help");

    final JMenuItem[] fileMenuItems = new JMenuItem[]{
        saveMenuItem = new JMenuItem("Save"),
        loadMenuItem = new JMenuItem("Load")
    };

    final JMenuItem[] gameMenuItems = new JMenuItem[]{
        resumeMenuItem = new JMenuItem("Resume"),
        pauseMenuItem = new JMenuItem("Pause"),
        redoMenuItem = new JMenuItem("Redo"),
        undoMenuItem = new JMenuItem("Undo"),
        exitMenuItem = new JMenuItem("Exit"),
        exitSaveMenuItem = new JMenuItem("Exit + Save")
    };

    final JMenuItem[] levelMenuItems = new JMenuItem[]{
        restartCurrentLevelMenuItem = new JMenuItem("Restart Current Level"),
        startFirstLevelMenuItem = new JMenuItem("Restart Level 1")
    };

    final JMenuItem[] recnplayMenuItems = new JMenuItem[]{
        startRecordingMenuItem = new JMenuItem("Record"),
        stopRecordingMenuItem = new JMenuItem("Stop Recording"),
        playMenuItem = new JMenuItem("Replay"),
        stopPlayMenuItem = new JMenuItem("Stop Replay"),
        loadRecordingMenuItem = new JMenuItem("Load Recording")
    };

    HashMap<JMenuItem[], JMenu> menuToMenuItems = new HashMap<>();
    menuToMenuItems.put(fileMenuItems, fileMenu);
    menuToMenuItems.put(gameMenuItems, gameMenu);
    menuToMenuItems.put(levelMenuItems, levelMenu);
    menuToMenuItems.put(recnplayMenuItems, recnplayMenu);

    final JMenuItem[][] superMenuItems = new JMenuItem[][]{
        fileMenuItems, gameMenuItems, levelMenuItems, recnplayMenuItems
    };

    for (JMenuItem[] menuItems : superMenuItems) {
      for (JMenuItem menuItem : menuItems) {
        menuItem.addActionListener(this);
        JMenu menu = menuToMenuItems.get(menuItems);
        menu.add(menuItem);
      }
    }

    showInstructMenuItem = new JMenuItem("How to Play");
    showInstructMenuItem.addActionListener(this);
    helpMenu.add(showInstructMenuItem);
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
    if (e.getSource() == resumeMenuItem) {
      resume();
      System.out.println("Resumed");
    } else if (e.getSource() == pauseMenuItem) {
      pause();
      System.out.println("Paused");
    } else if (e.getSource() == restartCurrentLevelMenuItem) {
      //restartCurrentLevel();
    } else if (e.getSource() == exitMenuItem) {
      System.exit(1);

      //persistence loading and saving
    } else if (e.getSource() == saveMenuItem) {
      pause();
      saveMaze(maze, saveFileChooser(false));
      resume();
    } else if (e.getSource() == loadMenuItem) {
      pause();
      loadMazeGui(Persistence.loadMaze(saveFileChooser(true)));
      resume();

      //recnplay menu functionalities

      //start recording game play
    } else if (e.getSource() == startRecordingMenuItem) {
      pause();
      RecordAndReplay.startRecording(openFileChooser(false));
      recordingIconLabel.setVisible(true);
      resume();

      //stop recording game play
    } else if (e.getSource() == stopRecordingMenuItem && RecordAndReplay.isRecording()) {
      pause();
      RecordAndReplay.stopRecording();
      recordingIconLabel.setVisible(false);
      resume();

      //play recording
    } else if (e.getSource() == playMenuItem) {
      pause();
      recnplay.playRecording();
      resume();

      //stop playing recording
    } else if (e.getSource() == stopPlayMenuItem) {

    } else if (e.getSource() == loadRecordingMenuItem) {
      pause();
      RecordAndReplay.loadRecording(openFileChooser(true));
      resume();
    }

    //recnplay button actions
    if (e.getSource() == nextFrameButton) {

    } else if (e.getSource() == lastFrameButton) {

    } else if (e.getSource() == lastFrameButton) {

    } else if (e.getSource() == autoPlayButton) {

    } else if (e.getSource() == slowerReplayButton) {

    } else if (e.getSource() == standardReplayButton) {

    } else if (e.getSource() == fasterReplayButton) {

    }
  }

  /**
   * Loads a maze.
   */
  public void loadMazeGui(Maze loadedMaze) {
    if (loadedMaze != null) {
      maze = loadedMaze;
      this.frame.setVisible(false);
      //FIXME: new gui shouldn't be instantiated,
      // find a way to redraw the board - probably a gui problem
      Gui updated = new Gui(maze);
      updated.reloadInventoryPanel();
      updated.getFrame().setVisible(true);
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
          } catch (IllegalStateException e1) {
            e1.getSuppressed();
          }
        }
        showInfoFieldToGui(false);

        // movement
        if (!isPaused || maze.isLevelFinished()) {
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
        }

        decrementTreasurePickUp();
        board.repaint();

        // pause and resume
        if (key == KeyEvent.VK_SPACE) {
          pause();
          pausedIconLabel.setVisible(true);
        } else if (key == KeyEvent.VK_ESCAPE) {
          resume();
          pausedIconLabel.setVisible(false);
        }

        // shortcuts
        if (e.isControlDown() && key == KeyEvent.VK_X) {
          System.out.println("ctrl + x pressed - exit game");
        } else if (e.isControlDown() && key == KeyEvent.VK_S) {
          System.out.println("ctrl + s pressed - exit and save");
        } else if (e.isControlDown() && key == KeyEvent.VK_R) {
          System.out.println("ctrl + r pressed - resume saved game");
        } else if (e.isControlDown() && key == KeyEvent.VK_P) {
          System.out.println("ctrl + p pressed - start new game at last unfinished level");
        } else if (e.isControlDown() && key == KeyEvent.VK_1) {
          restartCurrentLevel();
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
    timerTask = new TimerTask() {
      @Override
      public void run() {
        if (secondsLeft[0] > 0) {
          secondsLeft[0]--;
          setTimeValueLabel(secondsLeft[0]);
        }
      }
    };
  }

  /**
   * Pause the game, triggering countdown timer and maze to stop.
   */
  public void pause() {
    isPaused = true;
    maze.pause();
    timer.cancel();
  }

  /**
   * Resume the game, triggering countdown timer and maze to resume.
   */
  public void resume() {
    if (isPaused) {
      maze.resume();
      setupTimer();
      timer.schedule(timerTask, 0, 1000); // start the timer countdown
      isPaused = false;
    }
  }

  /**
   * Restart the current level.
   */
  public void restartCurrentLevel() {
    //reset timer count
    timer.cancel();
    isTimerActive = false;
    setTimeValueLabel(60);
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
    //FIXME: new gui shouldn't be instantiated,
    // find a way to redraw the board - probably a gui problem
    this.frame.setVisible(false);
    new Gui(initialMaze).getFrame().setVisible(true);
  }

  /**
   * Decrement treasure pickup value.
   */
  public void decrementTreasurePickUp() {
    int treasureCount = maze.getChap().getTreasures().size();
    setTreasuresValueLabel(maze.numTreasures() - treasureCount);
  }

  /**
   * Open the file chooser and set the file field.
   *
   * @param isLoad boolean confirming loading or saving (not loading)
   * @return target File object, or null is no file was chosen
   */
  public File openFileChooser(boolean isLoad) {
    int result = -1;
    if (isLoad) { //is loading the file
      result = fileChooser.showOpenDialog(null);
    } else { //is saving the file
      result = fileChooser.showSaveDialog(null);
    }
    if (result == JFileChooser.APPROVE_OPTION) {
      return fileChooser.getSelectedFile();
    }
    return null;
  }

  /**
   * Open the file chooser for game levels.
   *
   * @param isLoad boolean confirming loading or saving (not loading)
   * @return target File object, or null is no file was chosen
   */
  public File saveFileChooser(boolean isLoad) {
    JFileChooser fc = new JFileChooser(Paths.get(".", "levels").toFile());
    int result = -1;
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
   * Get the board panel.
   *
   * @return the JPanel representing the container for the board
   */
  public JLayeredPane getBoardPanel() {
    return boardPanel;
  }

  /**
   * Get the level value label.
   *
   * @return the label
   */
  public JLabel getLevelValueLabel() {
    return levelValueLabel;
  }

  /**
   * Get the time value label.
   *
   * @return the label
   */
  public JLabel getTimeValueLabel() {
    return timeValueLabel;
  }

  /**
   * Get the inventory labels.
   *
   * @return the inventory value labels array
   */
  public JLabel[] getInventoryValueLabels() {
    return inventoryValueLabels;
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
  public void setInfoFieldLabelText(String text) {
    infoFieldLabelText.setText(text);
    frame.revalidate();
  }

  /**
   * Display the info field to the gui.
   *
   * @param confirmation boolean confirming infofield is shown/hidden
   */
  public void showInfoFieldToGui(boolean confirmation) {
    infoFieldLabel.setVisible(confirmation);
    infoFieldLabelText.setVisible(confirmation);
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
  public void update(MazeEventInfoField e) {
    infoFieldLabel.setBounds(board.getX() - 175, board.getY() - 150, 1000, 1000);
    infoFieldLabelText.setBounds(infoFieldLabel.getX() + 300,
        infoFieldLabel.getY() - 150, 1000, 1000);
    frame.revalidate();
    showInfoFieldToGui(true);
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
    timer.cancel();
    timer.purge();
  }
}
