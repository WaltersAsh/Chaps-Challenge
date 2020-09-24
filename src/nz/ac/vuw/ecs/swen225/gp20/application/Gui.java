package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import nz.ac.vuw.ecs.swen225.gp20.maze.Key;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze.KeyColor;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventInfoField;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventListener;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventPickup;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventUnlocked;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.MazeEventWon;
import nz.ac.vuw.ecs.swen225.gp20.rendering.BoardView;

/**
 * Gui class for visual display of the game.
 *
 * @author Justin 300470389
 */
public class Gui extends MazeEventListener implements ActionListener {
  // frame and main panels
  private JFrame frame;
  private JPanel framePanel;
  public static JLayeredPane boardPanel;
  private JPanel sidePanel;

  // inner panels inside of side panel
  private JPanel levelPanel;
  private JPanel timePanel;
  private JPanel treasuresPanel;
  private JPanel inventoryPanel;

  // inner panels inside of inner panels of side panel
  private JPanel levelContentPanel;
  private JPanel timeContentPanel;
  private JPanel treasuresContentPanel;
  private JPanel inventoryGridPanel;
  private JPanel inventoryContentPanel;

  // title labels
  private JLabel levelTitleLabel;
  private JLabel timeTitleLabel;
  private JLabel treasuresTitleLabel;
  private JLabel inventoryTitleLabel;

  // value labels
  private JLabel levelValueLabel;
  private JLabel timeValueLabel;
  private JLabel treasuresValueLabel;
  private JLabel[] inventoryValueLabels;

  // infofield label
  private JLabel infoFieldLabel;
  private JLabel infoFieldLabelText;

  // menu bar and menu items
  private JMenuBar menuBar;

  private JMenu gameMenu;
  private JMenuItem resumeMenuItem;
  private JMenuItem pauseMenuItem;
  private JMenuItem redoMenuItem;
  private JMenuItem undoMenuItem;
  private JMenuItem saveMenuItem;
  private JMenuItem loadMenuItem;
  private JMenuItem exitMenuItem;
  private JMenuItem exitSaveMenuItem;

  private JMenu levelMenu;
  private JMenuItem restartCurrentLevelMenuItem;
  private JMenuItem startFirstLevelMenuItem;

  //TODO: add submenu and more to recnplay
  private JMenu recnplayMenu;
  private JMenuItem playMenuItem;
  private JMenuItem stopPlayMenuItem;
  private JMenuItem recMenuItem;

  private JMenu helpMenu;
  private JMenuItem showInstructMenuItem;

  // Text sizes and fonts
  private Font regText = new Font("", Font.PLAIN, 25);
  private Font bigText = new Font("", Font.BOLD, 45);

  // Background colours
  // TODO: Replace background color for theme of renderer assets
  private Color lavender = new Color(74, 29, 138);
  private Color lightLavender = new Color(179, 159, 207);
  private Color darkLavender = new Color(50, 38, 66);
  private Color deepLavender = new Color(85, 52, 130);
  private Color fullLavender = new Color(102, 0, 255);
  private Color paleLavender = new Color(237, 224, 255);

  public static BoardView board;
  private Maze maze;
  private Timer timer;
  private TimerTask timerTask;
  private boolean isTimerActive;
  private int[] secondsLeft;
  private boolean isPaused;

  /**
   * Construct the GUI: frame, panels, labels, menus, button listeners.
   */
  public Gui(Maze maze) {
    this.maze = maze;
    // base frame that all JComponents will be added to
    frame = new JFrame();
    frame.setLayout(new BorderLayout());
    createFramePanel();
    createBoardPanel();
    createInfoFieldLabel();
    createSidePanel();
    createInnerSidePanels();
    initialiseInnerSidePanels();
    createMenuComponents();

    // boardPanel.setLayout(new BorderLayout());
    board.setBounds(0, 0, 1000, 1000);
    boardPanel.add(board, JLayeredPane.DEFAULT_LAYER);
    boardPanel.add(infoFieldLabel, JLayeredPane.PALETTE_LAYER);
    boardPanel.add(infoFieldLabelText, JLayeredPane.MODAL_LAYER);

    // add menus to menu bars
    menuBar.add(gameMenu);
    menuBar.add(levelMenu);
    menuBar.add(recnplayMenu);
    menuBar.add(helpMenu);

    // add content panels to inner side panels
    levelPanel.add(levelContentPanel);
    timePanel.add(timeContentPanel);
    treasuresPanel.add(treasuresContentPanel);
    inventoryPanel.add(inventoryContentPanel);

    // add panels to side panels
    sidePanel.add(levelPanel);
    sidePanel.add(timePanel);
    sidePanel.add(treasuresPanel);
    sidePanel.add(inventoryPanel);

    // set up board and side panels into frame panel
    // framePanel.add(Box.createHorizontalGlue());
    framePanel.add(boardPanel);
    framePanel.add(Box.createRigidArea(new Dimension(20, 0)));
    framePanel.add(sidePanel);
    // framePanel.add(Box.createHorizontalGlue());

    // initialise frame
    frame.add(framePanel);
    frame.setJMenuBar(menuBar);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Chap's Challenge");
    Dimension dimen = Toolkit.getDefaultToolkit().getScreenSize();
    frame.pack();
    frame.setSize(1024, 800);
    frame.setMinimumSize(new Dimension(875, 675));
    frame.setLocation(dimen.width / 2 - frame.getSize().width / 2,
            dimen.height / 2 - frame.getSize().height / 2);

    setupTimer();
    setupKeyListener();
  }

  /**
   * Create the frame panel.
   */
  public void createFramePanel() {
    framePanel = new JPanel();
    framePanel.setLayout(new BoxLayout(framePanel, BoxLayout.X_AXIS));
    framePanel.setBackground(lightLavender);
    framePanel.setBorder(new EmptyBorder(50, 50, 50, 50));
  }

  /**
   * Create the board panel and rendered board.
   */
  public void createBoardPanel() {
    boardPanel = new JLayeredPane();
    maze.addListener(this);
    board = new BoardView(maze);
    boardPanel.setBackground(lightLavender);
    boardPanel.setMinimumSize(
            new Dimension(board.getPreferredSize().width, board.getPreferredSize().height));
    boardPanel.setPreferredSize(
            new Dimension(board.getPreferredSize().width, board.getPreferredSize().height));
    boardPanel.setMaximumSize(new Dimension(800, 800));
  }

  /**
   * Create the side panel.
   */
  public void createSidePanel() {
    sidePanel = new JPanel();
    sidePanel.setBackground(paleLavender);
    sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
    sidePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    sidePanel.setPreferredSize(new Dimension(175, 500));
    sidePanel.setMaximumSize(new Dimension(250, 800));
  }

  /**
   * Create the inner side panels.
   */
  public void createInnerSidePanels() {
    JPanel[] panels = new JPanel[] {
      levelPanel = new JPanel(),
      timePanel = new JPanel(),
      treasuresPanel = new JPanel(),
      inventoryPanel = new JPanel()
    };

    inventoryGridPanel = new JPanel();

    inventoryPanel.setLayout(new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS));
    inventoryGridPanel.setLayout(new GridLayout(2, 4));

    levelPanel.setBackground(fullLavender);
    timePanel.setBackground(lavender);
    treasuresPanel.setBackground(deepLavender);
    inventoryPanel.setBackground(darkLavender);
    inventoryGridPanel.setBackground(darkLavender);

    for (JPanel panel : panels) {
      panel.setPreferredSize(new Dimension(175, 125));
      panel.setBorder(new LineBorder(paleLavender, 2, false));
    }

    // inventory grid panel initialisation
    inventoryValueLabels = new JLabel[8];
    for (int i = 0; i < 8; i++) {
      JLabel label = new JLabel();
      label.setText(" ");
      label.setOpaque(true);
      label.setBackground(darkLavender);
      label.setBorder(BorderFactory.createLineBorder(paleLavender));
      inventoryValueLabels[i] = label;
      inventoryGridPanel.add(label);
    }
  }

  /**
   * Create and initialise the panels in the side panel.
   */
  public void initialiseInnerSidePanels() {
    // initialise inner panels for inner panels in side panel
    JPanel[] panels = new JPanel[] {
      levelContentPanel = new JPanel(),
      timeContentPanel = new JPanel(),
      treasuresContentPanel = new JPanel(),
      inventoryContentPanel = new JPanel()
    };

    for (JPanel panel : panels) {
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    }

    levelContentPanel.setBackground(fullLavender);
    timeContentPanel.setBackground(lavender);
    treasuresContentPanel.setBackground(deepLavender);
    inventoryContentPanel.setBackground(darkLavender);

    // initialise title labels for panels in inner side panel
    JLabel[] titleLabels = new JLabel[] {
      levelTitleLabel = new JLabel("LEVEL"),
      timeTitleLabel = new JLabel("TIME LEFT"),
      treasuresTitleLabel = new JLabel("TREASURES REMAINING"),
      inventoryTitleLabel = new JLabel("INVENTORY"),
    };

    for (JLabel label : titleLabels) {
      label.setForeground(Color.WHITE);
      label.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // initialise value labels
    JLabel[] valueLabels = new JLabel[] {
      levelValueLabel = new JLabel("1"),
      timeValueLabel = new JLabel("60"),
      treasuresValueLabel = new JLabel(String.valueOf(maze.numTreasures())),
    };

    for (JLabel valueLabel : valueLabels) {
      valueLabel.setFont(bigText);
      valueLabel.setForeground(Color.BLACK);
      valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // add labels and JComponents to inner panels in side panels
    levelContentPanel.add(levelTitleLabel);
    levelContentPanel.add(Box.createRigidArea(new Dimension(0, 35)));
    levelContentPanel.add(levelValueLabel);
    timeContentPanel.add(timeTitleLabel);
    timeContentPanel.add(Box.createRigidArea(new Dimension(0, 35)));
    timeContentPanel.add(timeValueLabel);
    treasuresContentPanel.add(treasuresTitleLabel);
    treasuresContentPanel.add(Box.createRigidArea(new Dimension(0, 35)));
    treasuresContentPanel.add(treasuresValueLabel);
    inventoryContentPanel.add(inventoryTitleLabel);
    inventoryContentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
    inventoryContentPanel.add(inventoryGridPanel);
    inventoryContentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
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
    infoFieldLabelText.setFont(new Font("", Font.BOLD, 50));
    infoFieldLabelText.setForeground(Color.BLACK);
    showInfoFieldToGui(false);
  }

  /**
   * Create the menu bar, menus and menu items.
   */
  public void createMenuComponents() {

    menuBar = new JMenuBar();
    gameMenu = new JMenu("Game");
    levelMenu = new JMenu("Level");
    recnplayMenu = new JMenu("Rec'n'play");
    helpMenu = new JMenu("Help");

    final JMenuItem[] gameMenuItems = new JMenuItem[]{
        resumeMenuItem = new JMenuItem("Resume"),
        pauseMenuItem = new JMenuItem("Pause"),
        redoMenuItem = new JMenuItem("Redo"),
        undoMenuItem = new JMenuItem("Undo"),
        saveMenuItem = new JMenuItem("Save"),
        loadMenuItem = new JMenuItem("Load"),
        exitMenuItem = new JMenuItem("Exit"),
        exitSaveMenuItem = new JMenuItem("Exit + Save")
    };

    final JMenuItem[] levelMenuItems = new JMenuItem[]{
        restartCurrentLevelMenuItem = new JMenuItem("Restart Current Level"),
        startFirstLevelMenuItem = new JMenuItem("Restart Level 1")
    };

    final JMenuItem[] recnplayMenuItems = new JMenuItem[]{
        recMenuItem = new JMenuItem("Record"),
        playMenuItem = new JMenuItem("Replay"),
        stopPlayMenuItem = new JMenuItem("Stop Replay")
    };

    for (JMenuItem gameMenuItem : gameMenuItems) {
      gameMenuItem.addActionListener(this);
      gameMenu.add(gameMenuItem);
    }

    for (JMenuItem levelMenuItem : levelMenuItems) {
      levelMenuItem.addActionListener(this);
      levelMenu.add(levelMenuItem);
    }

    for (JMenuItem recnplayMenuItem : recnplayMenuItems) {
      recnplayMenuItem.addActionListener(this);
      recnplayMenu.add(recnplayMenuItem);
    }

    showInstructMenuItem = new JMenuItem("How to Play");
    showInstructMenuItem.addActionListener(this);
    helpMenu.add(showInstructMenuItem);
  }

  /**
   * Listen to menu buttons in menu bar and execute.
   *
   * @param e the action event
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == resumeMenuItem) {
      resume();
      System.out.println("Resumed");
    } else if (e.getSource() == pauseMenuItem) {
      pause();
      System.out.println("Paused");
    } else if (e.getSource() == restartCurrentLevelMenuItem) {
      restartCurrentLevel();
    } else if (e.getSource() == exitMenuItem) {
      System.exit(1);
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
        if (!isTimerActive && !e.isControlDown()) {
          timer.schedule(timerTask, 0, 1000); // start the timer countdown
          isTimerActive = true;
        }
        showInfoFieldToGui(false);
        // movement
        if (!isPaused || maze.isLevelFinished()) {
          if (key == KeyEvent.VK_UP) {
            maze.move(Maze.Direction.UP);
          } else if (key == KeyEvent.VK_DOWN) {
            maze.move(Maze.Direction.DOWN);
          } else if (key == KeyEvent.VK_LEFT) {
            maze.move(Maze.Direction.LEFT);
          } else if (key == KeyEvent.VK_RIGHT) {
            maze.move(Maze.Direction.RIGHT);
          }
          decrementTreasurePickUp();
          board.repaint();
        }
        decrementTreasurePickUp();
        board.repaint();

        // pause and resume
        if (key == KeyEvent.VK_SPACE) {
          pause();
        } else if (key == KeyEvent.VK_ESCAPE) {
          resume();
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


  }

  /**
   * Decrement treasure pickup value.
   */
  public void decrementTreasurePickUp() {
    int treasureCount = maze.getChap().getTreasures().size();
    setTreasuresValueLabel(maze.numTreasures() - treasureCount);
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
   * Get the board.
   *
   * @return the JComponent representing the board.
   */
  public BoardView getBoard() {
    return board;
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
    try {
      if (e.getPicked() instanceof Key) {
        Key key = (Key) e.getPicked();
        Image keyImage;
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
      }
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
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
