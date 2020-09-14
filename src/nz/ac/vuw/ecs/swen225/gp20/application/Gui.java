package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Gui class for visual display of the game.
 *
 * @author Justin 30070389
 */
public class Gui {
  //frame and main panels
  private JFrame frame;
  private JPanel framePanel;
  private JPanel boardPanel;
  private JPanel sidePanel;

  //menu bar and menu
  //TODO: Add menu items to menus
  private JMenuBar menuBar;
  private JMenu gameMenu;
  private JMenu levelMenu;
  private JMenu helpMenu;

  //Background colours
  //TODO: Replace background color for theme of renderer assets
  Color lavender = new Color(74, 29, 138);
  Color lightLavender = new Color(179, 159, 207);
  Color darkLavender = new Color(50, 38, 66);
  Color deepLavender = new Color(85, 52, 130);
  Color fullLavender = new Color(102, 0, 255);
  Color paleLavender = new Color(237, 224, 255);

  /**
   * Construct the GUI: frame, panels, labels, menus, button listeners.
   */
  public Gui() {
    //base frame that all JComponents will be added to
    frame = new JFrame();
    frame.setLayout(new BorderLayout());
    createFramePanel();
    //TODO: Add rendered board object here
    createBoardPanel();
    createSidePanel();
    createMenuComponents();

    //add menus to menu bars
    menuBar.add(gameMenu);
    menuBar.add(levelMenu);
    menuBar.add(helpMenu);

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
   * Create he board panel.
   */
  public void createBoardPanel() {
    boardPanel = new JPanel();
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
   * Get the frame.
   *
   * @return the JFrame representing the container for the gui
   */
  public JFrame getFrame() {
    return frame;
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
