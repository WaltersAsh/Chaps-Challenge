package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * InstructionsFrame class for instantiating a new instructions frame to gui.
 *
 * @author Justin Joe 300470389
 */
public class InstructionsFrame extends JFrame implements ActionListener {

  private final Gui gui;
  private final ComponentLibrary cl;

  /**
   * A help panel is constructed with its content panel.
   *
   * @param gui the gui object passed
   */
  public InstructionsFrame(Gui gui) {
    cl = ComponentLibrary.getInstance();
    this.gui = gui;
    JScrollPane jsp = new JScrollPane();

    //initialise scrollbar stylisation
    jsp.getVerticalScrollBar().setBackground(cl.paleLavender);
    jsp.getHorizontalScrollBar().setBackground(cl.paleLavender);
    jsp.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
      @Override
      protected void configureScrollBarColors() {
        this.thumbColor = cl.fullLavender;
      }
    });

    jsp.setViewportView(initialiseContentPanel());
    jsp.add(initialiseContentPanel());
    add(jsp);
    initialiseWindowListener();
    setSize(1024, 800);
    setMinimumSize(new Dimension(1010, 820));
    setTitle("Chap's Challenge - Instructions (Game is Paused)");
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension scrnsize = toolkit.getScreenSize();
    setBounds((scrnsize.width - getWidth()) / 2,
            (scrnsize.height - getHeight()) / 2, getWidth(), getHeight());
    setVisible(false);
  }

  /**
   * Initialise the content panels with instructions.
   *
   * @return a initialised content JPanel
   */
  private JPanel initialiseContentPanel() {
    final JLabel titleLabel = new JLabel("HOW TO PLAY");
    JButton exitButton = new JButton("EXIT");
    exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    exitButton.setBackground(cl.fullLavender);
    exitButton.setForeground(Color.WHITE);
    exitButton.setFont(cl.bigFont);
    exitButton.addActionListener(this);
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    titleLabel.setFont(cl.titleScreenFont);
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    contentPanel.add(titleLabel);
    contentPanel.add(Box.createRigidArea(new Dimension(0, 50)));
    contentPanel.add(exitButton);
    contentPanel.add(instructionsLabel());
    contentPanel.setBackground(cl.lightLavender);
    return contentPanel;
  }

  /**
   * Create the label containing instructions for keyboard controls.
   *
   * @return the keyboard controls JLabel
   */
  private JLabel instructionsLabel() {
    JLabel instructLabel = new JLabel(
            "<html>"
                    + "<body>"
                    + "<br>"
                    + "<br>"
                    + "<h1>KEYBOARD CONTROLS</h1>"
                    + "<br>"
                    + "<a>"
                    + "CTRL-X - exit the game, the current game state will be lost,"
                    + "the next time the game is started, it will resume from the"
                    + " last unfinished level\n"
                    + "<br>"
                    + "<br>"
                    + "CTRL-S - exit the game, saves the game state, game will resume\n"
                    + "next time the application will be started\n"
                    + "<br>"
                    + "<br>"
                    + "CTRL-R - resume a saved game\n"
                    + "<br>"
                    + "<br>"
                    + "CTRL-P - start a new game at the last unfinished level\n"
                    + "<br>"
                    + "<br>"
                    + "CTRL-1 - start a new game at level 1\n"
                    + "<br>"
                    + "<br>"
                    + "SPACE - pause the game, a paused dialog will popup\n"
                    + "<br>"
                    + "<br>"
                    + "ESC - resume the game, the paused dialog will disappear\n"
                    + "<br>"
                    + "<br>"
                    + "UP, DOWN, LEFT, RIGHT ARROWS - move Chap within the maze\n"
                    + "<br>"
                    + "<br>"
                    + "A - undo last move"
                    + "<br>"
                    + "<br>"
                    + "Use the menu bar at the top of the screen to access features, such as:"
                    + "<br>"
                    + "File - load and save"
                    + "<br>"
                    + "Game - resume, pause, redo, undo, exit and exit + save"
                    + "<br>"
                    + "Level - restart current level and restart from level 1"
                    + "<br>"
                    + "Rec'n'play - record and save, stop recording, replay, "
                    + "stop replay and load recording"
                    + "<br>"
                    + "Help - how to play (you are here)"
                    + "</a>"
                    + "<br>"
                    + "<br>"
                    + "<br>"
                    + "<h1> GAMEPLAY </h1>"
                    + "<a>"
                    + "Use the arrow keys to guide chap around the maze."
                    + "<br>"
                    + "<img src=\"file:" + cl.moveDemo.toString() + "\" border=\"3\">"
                    + "<br>"
                    + "<br>"
                    + "Collect the emerald treasures."
                    + "<br>"
                    + "<img src=\"file:" + cl.collectDemo.toString()
                      + "\" border=\"3\">"
                    + "<br>"
                    + "<br>"
                    + "Collect the pickaxes to mine through certain blocks/unlock doors."
                    + "<br>"
                    + "<img src=\"file:" + cl.unlockDemo.toString()
                      + "\" border=\"3\">"
                    + "<br>"
                    + "<br>"
                    + "Move crates to create a path in the water."
                    + "<br>"
                    + "<img src=\"file:" + cl.crateDemo.toString()
                      + "\" border=\"3\">"
                    + "<br>"
                    + "<br>"
                    + "Avoid enemies."
                    + "<br>"
                    + "<img src=\"file:" + cl.enemiesDemo.toString()
                      + "\" border=\"3\">"
                    + "<br>"
                    + "<br>"
                    + "Reach the portal before the time runs out to progress to the next level."
                    + "<br>"
                    + "<img src=\"file:" + cl.portalDemo.toString()
                      + "\" border=\"3\">"
                    + "<br>"
                    + "<br>"
                    + "</a>"
                    + "</body>"
                    + "</html>");
    instructLabel.setFont(cl.bodyFont);
    instructLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    return instructLabel;
  }

  /**
   * Initialise the window listener.
   */
  public void initialiseWindowListener() {
    addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        //don't resume the game if player opens instructions first thing
        if (gui.getIsTimerActive()) {
          gui.resume();
        }
      }
    });
  }

  /**
   * Performs the action of the exit button.
   *
   * @param actionEvent the action event
   */
  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    dispose(); //close the frame
  }
}
