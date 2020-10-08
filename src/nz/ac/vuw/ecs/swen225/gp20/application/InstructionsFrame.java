package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * InstructionsFrame class for instantiating a new instructions frame to gui.
 *
 * @author Justin 300470389
 */
public class InstructionsFrame extends JFrame implements ActionListener {

  private Gui gui;
  private JPanel contentPanel;
  private JLabel titleLabel;
  private JLabel instructLabel;
  private JButton exitButton;

  /**
   * A help panel is constructed with its content panel.
   */
  public InstructionsFrame(Gui gui) {

    this.gui = gui;

    JScrollPane jsp = new JScrollPane();
    jsp.setViewportView(initialiseContentPanel());
    jsp.add(initialiseContentPanel());
    add(jsp);
    initialiseWindowListener();
    setSize(1024, 800);
    setTitle("Chap's Challenge - Instructions (Game is Paused)");
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension scrnsize = toolkit.getScreenSize();
    setBounds((scrnsize.width - getWidth()) / 2,
            (scrnsize.height - getHeight()) / 2, getWidth(), getHeight());
    setVisible(false);
  }

  /**
   * Initialise the content panels with instructions.
   */
  private JPanel initialiseContentPanel() {
    titleLabel = new JLabel("HOW TO PLAY");
    exitButton = new JButton("EXIT");
    exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    exitButton.setBackground(ComponentLibrary.fullLavender);
    exitButton.setForeground(Color.WHITE);
    exitButton.setFont(ComponentLibrary.bigFont);
    exitButton.addActionListener(this);
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    titleLabel.setFont(ComponentLibrary.titleScreenFont);
    contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    contentPanel.add(titleLabel);
    contentPanel.add(Box.createRigidArea(new Dimension(0, 50)));
    contentPanel.add(exitButton);
    contentPanel.add(instructionsLabel());
    contentPanel.setBackground(ComponentLibrary.lightLavender);
    return contentPanel;
  }

  /**
   * Create the label containing instructions for keyboard controls.
   *
   * @return the keyboard controls JLabel
   */
  private JLabel instructionsLabel() {
    instructLabel = new JLabel(
    "<html>"
          + "<body>"
          + "<br>"
          + "<br>"
          + "<h1>KEYBOARD CONTROLS</h1>"
          + "<br>"
          + "<a>"
            +  "CTRL-X - exit the game, the current game state will be lost,"
            +  "the next time the game is started, it will resume from the"
            +  " last unfinished level\n"
            + "<br>"
            + "<br>"
            +  "CTRL-S - exit the game, saves the game state, game will resume\n"
            + "next time the application will be started\n"
            + "<br>"
            + "<br>"
            +  "CTRL-R - resume a saved game\n"
            + "<br>"
            + "<br>"
            +  "CTRL-P - start a new game at the last unfinished level\n"
            + "<br>"
            + "<br>"
            +  "CTRL-1 - start a new game at level 1\n"
            + "<br>"
            + "<br>"
            +  "SPACE - pause the game and display a “game is paused” dialog\n"
            + "<br>"
            + "<br>"
            +  "ESC - close the “game is paused” dialog and resume the game\n"
            + "<br>"
            + "<br>"
            +  "UP, DOWN, LEFT, RIGHT ARROWS - move Chap within the maze\n"
          +  "</a>"
          + "<br>"
          + "<br>"
          + "<br>"
          + "<h1> GAMEPLAY </h1>"
          + "<a>"
            + "Use the arrow keys to guide chap around the maze."
            + "<br>"
            + "<img src=\"file:" + ComponentLibrary.moveDemo.toString() + "\" border=\"3\">"
            + "<br>"
            + "<br>"
            + "Collect the emerald treasures."
            + "<br>"
            + "<img src=\"file:" + ComponentLibrary.collectDemo.toString() + "\" border=\"3\">"
            + "<br>"
            + "<br>"
            + "Collect the pickaxes to mine through certain blocks/unlock doors."
            + "<br>"
            + "<img src=\"file:" + ComponentLibrary.unlockDemo.toString() + "\" border=\"3\">"
            + "<br>"
            + "<br>"
            + "Move crates to create a path in the water."
            + "<br>"
            + "<img src=\"file:" + ComponentLibrary.crateDemo.toString() + "\" border=\"3\">"
            + "<br>"
            + "<br>"
            + "Avoid enemies."
            + "<br>"
            + "<img src=\"file:" + ComponentLibrary.enemiesDemo.toString() + "\" border=\"3\">"
            + "<br>"
            + "<br>"
            + "Reach the portal before the time runs out to progress to the next level."
            + "<br>"
            + "<img src=\"file:" + ComponentLibrary.portalDemo.toString() + "\" border=\"3\">"
            + "<br>"
            + "<br>"
          + "</a>"
        + "</body>"
      + "</html>");
    instructLabel.setFont(ComponentLibrary.bodyFont);
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
