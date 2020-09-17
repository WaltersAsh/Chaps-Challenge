package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.util.Timer;
import java.util.TimerTask;

import nz.ac.vuw.ecs.swen225.gp20.maze.BoardRig;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.rendering.BoardView;

/**
 * Gui class for visual display of the game.
 *
 * @author Justin 300470389
 */
public class Gui {
  //frame and main panels
  private JFrame frame;
  private JPanel framePanel;
  public static JPanel boardPanel;
  private JPanel sidePanel;

  //inner panels inside of side panel
  private JPanel levelPanel;
  private JPanel timePanel;
  private JPanel collectablesPanel;
  private JPanel inventoryPanel;

  //inner panels inside of inner panels of side panel
  private JPanel levelContentPanel;
  private JPanel timeContentPanel;
  private JPanel collectablesContentPanel;
  private JPanel inventoryGridPanel;
  private JPanel inventoryContentPanel;

  //title labels
  private JLabel levelTitleLabel;
  private JLabel timeTitleLabel;
  private JLabel collectablesTitleLabel;
  private JLabel inventoryTitleLabel;

  //value labels
  private JLabel levelValueLabel;
  private JLabel timeValueLabel;
  private JLabel collectablesValueLabel;
  private JLabel[] inventoryValueLabels;

  //menu bar and menu
  //TODO: Add menu items to menus
  private JMenuBar menuBar;
  private JMenu gameMenu;
  private JMenu levelMenu;
  private JMenu helpMenu;

  //Text sizes and fonts
  private Font regText = new Font("", Font.PLAIN, 25);
  private Font bigText = new Font("", Font.BOLD, 45);

  //Background colours
  //TODO: Replace background color for theme of renderer assets
  private Color lavender = new Color(74, 29, 138);
  private Color lightLavender = new Color(179, 159, 207);
  private Color darkLavender = new Color(50, 38, 66);
  private Color deepLavender = new Color(85, 52, 130);
  private Color fullLavender = new Color(102, 0, 255);
  private Color paleLavender = new Color(237, 224, 255);

  private BoardView board;
  private Maze maze;
  private Timer timer;
  private TimerTask timerTask;
  private boolean isTimerActive;

  /**
   * Construct the GUI: frame, panels, labels, menus, button listeners.
   */
  public Gui() {
    //base frame that all JComponents will be added to
    frame = new JFrame();
    frame.setLayout(new BorderLayout());
    createFramePanel();
    createBoardPanel();
    createSidePanel();
    createInnerSidePanels();
    initialiseInnerSidePanels();
    createMenuComponents();

    boardPanel.setLayout(new BorderLayout());
    boardPanel.add(board);

    //add menus to menu bars
    menuBar.add(gameMenu);
    menuBar.add(levelMenu);
    menuBar.add(helpMenu);

    //add content panels to inner side panels
    levelPanel.add(levelContentPanel);
    timePanel.add(timeContentPanel);
    collectablesPanel.add(collectablesContentPanel);
    inventoryPanel.add(inventoryContentPanel);

    //add panels to side panels
    sidePanel.add(levelPanel);
    sidePanel.add(timePanel);
    sidePanel.add(collectablesPanel);
    sidePanel.add(inventoryPanel);

    //set up board and side panels into frame panel
    //framePanel.add(Box.createHorizontalGlue());
    framePanel.add(boardPanel);
    framePanel.add(Box.createRigidArea(new Dimension(20, 0)));
    framePanel.add(sidePanel);
    //framePanel.add(Box.createHorizontalGlue());

    //initialise frame
    frame.add(framePanel);
    frame.setJMenuBar(menuBar);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Chap's Challenge");
    Dimension dimen = Toolkit.getDefaultToolkit().getScreenSize();
    frame.pack();
    frame.setSize(1024, 800);
    frame.setMinimumSize(new Dimension(800, 700));
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
    boardPanel = new JPanel();
    //TODO: Set maze and board somewhere else
    //maze = BoardRig.lesson1();
    //maze = BoardRig.crateTest();
    maze = BoardRig.crateAndWaterTest();
    board = new BoardView(maze);
    boardPanel.setBackground(paleLavender);
    boardPanel.setMinimumSize(new Dimension(400, 400));
    boardPanel.setPreferredSize(new Dimension(500, 500));
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
   * TODO: Condense code by adding panels to a collection and looping configurations
   */
  public void createInnerSidePanels() {
    levelPanel = new JPanel();
    timePanel = new JPanel();
    collectablesPanel = new JPanel();
    inventoryPanel = new JPanel();
    inventoryPanel.setLayout(new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS));
    inventoryGridPanel = new JPanel();
    inventoryGridPanel.setLayout(new GridLayout(2, 4));
    levelPanel.setBackground(fullLavender);
    timePanel.setBackground(lavender);
    collectablesPanel.setBackground(deepLavender);
    inventoryPanel.setBackground(darkLavender);
    inventoryGridPanel.setBackground(darkLavender);
    levelPanel.setPreferredSize(new Dimension(175, 125));
    timePanel.setPreferredSize(new Dimension(175, 125));
    collectablesPanel.setPreferredSize(new Dimension(175, 125));
    inventoryPanel.setPreferredSize(new Dimension(175, 125));
    levelPanel.setBorder(new LineBorder(paleLavender, 2, false));
    timePanel.setBorder(new LineBorder(paleLavender, 2, false));
    collectablesPanel.setBorder(new LineBorder(paleLavender, 2, false));
    inventoryPanel.setBorder(new LineBorder(paleLavender, 2, false));

    //inventory grid panel initialisation
    for (int i = 0; i < (2 * 4); i++) {
      final JLabel label = new JLabel();
      label.setOpaque(true);
      label.setBackground(darkLavender);
      label.setBorder(BorderFactory.createLineBorder(paleLavender));
      inventoryGridPanel.add(label);
    }
  }

  /**
   * Create and initialise the panels in the side panel.
   * TODO: Condense code by adding panels to a collection and looping configurations
   */
  public void initialiseInnerSidePanels() {
    //initialise inner panels for inner panels in side panel
    levelContentPanel = new JPanel();
    timeContentPanel = new JPanel();
    collectablesContentPanel = new JPanel();
    inventoryContentPanel = new JPanel();
    levelContentPanel.setLayout(new BoxLayout(levelContentPanel, BoxLayout.Y_AXIS));
    timeContentPanel.setLayout(new BoxLayout(timeContentPanel, BoxLayout.Y_AXIS));
    collectablesContentPanel.setLayout(new BoxLayout(collectablesContentPanel, BoxLayout.Y_AXIS));
    inventoryContentPanel.setLayout(new BoxLayout(inventoryContentPanel, BoxLayout.Y_AXIS));
    levelContentPanel.setBackground(fullLavender);
    timeContentPanel.setBackground(lavender);
    collectablesContentPanel.setBackground(deepLavender);
    inventoryContentPanel.setBackground(darkLavender);

    //initialise title labels for panels in inner side panel
    levelTitleLabel = new JLabel("LEVEL");
    timeTitleLabel = new JLabel("TIME LEFT");
    collectablesTitleLabel = new JLabel("COLLECTABLES REMAINING");
    inventoryTitleLabel = new JLabel("INVENTORY");
    levelTitleLabel.setForeground(Color.WHITE);
    timeTitleLabel.setForeground(Color.WHITE);
    collectablesTitleLabel.setForeground(Color.WHITE);
    inventoryTitleLabel.setForeground(Color.WHITE);
    levelTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    timeTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    collectablesTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    inventoryTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    //initialise value labels
    levelValueLabel = new JLabel("1");
    timeValueLabel = new JLabel("60");
    collectablesValueLabel = new JLabel("8");
    inventoryValueLabels = new JLabel[8];
    levelValueLabel.setFont(bigText);
    timeValueLabel.setFont(bigText);
    collectablesValueLabel.setFont(bigText);
    levelValueLabel.setForeground(Color.BLACK);
    timeValueLabel.setForeground(Color.BLACK);
    collectablesValueLabel.setForeground(Color.BLACK);
    levelValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    timeValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    collectablesValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    //add labels and JComponents to inner panels in side panels
    levelContentPanel.add(levelTitleLabel);
    levelContentPanel.add(Box.createRigidArea(new Dimension(0, 35)));
    levelContentPanel.add(levelValueLabel);
    timeContentPanel.add(timeTitleLabel);
    timeContentPanel.add(Box.createRigidArea(new Dimension(0, 35)));
    timeContentPanel.add(timeValueLabel);
    collectablesContentPanel.add(collectablesTitleLabel);
    collectablesContentPanel.add(Box.createRigidArea(new Dimension(0, 35)));
    collectablesContentPanel.add(collectablesValueLabel);
    inventoryContentPanel.add(inventoryTitleLabel);
    inventoryContentPanel.add(Box.createRigidArea(new Dimension(0, 35)));
    inventoryContentPanel.add(inventoryGridPanel);
  }

  /**
   * Create the menu bar, menus and menu items.
   * TODO: Create menu items
   */
  public void createMenuComponents() {
    menuBar = new JMenuBar();
    gameMenu = new JMenu("Game");
    levelMenu = new JMenu("Level");
    helpMenu = new JMenu("Help");
  }

  /**
   * Key listener to detect keys and key strokes.
   */
  public void setupKeyListener() {
    frame.addKeyListener(new KeyListener() {

      @Override
      public void keyPressed(KeyEvent e) {
        int key = e.getExtendedKeyCode();
        if (!isTimerActive) {
          timer.schedule(timerTask, 0, 1000); //start the timer countdown
          isTimerActive = true;
        }
        //movement
        if (key == KeyEvent.VK_UP) {
          maze.move(Maze.Direction.UP);
        } else if (key == KeyEvent.VK_DOWN) {
          maze.move(Maze.Direction.DOWN);
        } else if (key == KeyEvent.VK_LEFT) {
          maze.move(Maze.Direction.LEFT);
        } else if (key == KeyEvent.VK_RIGHT) {
          maze.move(Maze.Direction.RIGHT);
        }
        board.repaint();

        //pause and resume
        if (key == KeyEvent.VK_SPACE) {
          System.out.println("space pressed");
        } else if (key == KeyEvent.VK_ESCAPE) {
          System.out.println("escape pressed");

          //shortcuts
        } else if (e.isControlDown() && key == KeyEvent.VK_X) {
          System.out.println("ctrl + x pressed");
        } else if (e.isControlDown() && key == KeyEvent.VK_S) {
          System.out.println("ctrl + s pressed");
        } else if (e.isControlDown() && key == KeyEvent.VK_R) {
          System.out.println("ctrl + r pressed");
        } else if (e.isControlDown() && key == KeyEvent.VK_P) {
          System.out.println("ctrl + p pressed");
        } else if (e.isControlDown() && key == KeyEvent.VK_1) {
          System.out.println("ctrl + 1 pressed");
        }
      }

      //dead code
      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public void keyReleased(KeyEvent e) {
      }
    });
  }

  /**
   * Setup the timer
   */
  public void setupTimer() {
    final int[] secondsLeft = {Integer.parseInt(timeValueLabel.getText())};
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
  public JPanel getBoardPanel() {
    return boardPanel;
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
   * Set the collectables value text of the label.
   *
   * @param collectablesValue the String representing the collectables value to be set
   */
  public void setCollectablesValueLabel(String collectablesValue) {
    collectablesValueLabel.setText(collectablesValue);
    frame.revalidate();
  }

  /**
   * Main method for testing the GUI.
   *
   * @param args the commandline arguments
   */
  public static void main(String[] args) {
    new Gui().getFrame().setVisible(true);
  }

}
