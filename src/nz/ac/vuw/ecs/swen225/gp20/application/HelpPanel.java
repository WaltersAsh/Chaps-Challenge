package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * HelpPanel class for instantiating a new help panel to gui.
 *
 * @author Justin 300470389
 */
public class HelpPanel extends JScrollPane {

  JPanel contentPanel;
  JLabel titleLabel;
  JLabel keyboardLabel;

  /**
   * A help panel is constructed with its content panel.
   */
  public HelpPanel() {
    setViewportView(initialiseContentPanel());
    add(initialiseContentPanel());
  }

  /**
   * Initialise the content panels with instructions.
   */
  private JPanel initialiseContentPanel() {
    titleLabel = new JLabel("HOW TO PLAY");
    titleLabel.setFont(new Font("", Font.BOLD, 105));
    contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    contentPanel.add(titleLabel);
    contentPanel.add(keyboardControlsLabel());
    contentPanel.setBackground(ComponentLibrary.lightLavender);
    return contentPanel;
  }

  /**
   * Create the label containing instructions for keyboard controls.
   *
   * @return the keyboard controls JLabel
   */
  private JLabel keyboardControlsLabel() {
    keyboardLabel = new JLabel(
"<html>"
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
      + "</html>");
    return keyboardLabel;
  }

  /**
   * Help panel tester.
   *
   * @param args the commandline arguments
   */
  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.add(new HelpPanel());
    frame.setSize(800, 500);
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension scrnsize = toolkit.getScreenSize();
    frame.setBounds((scrnsize.width - frame.getWidth()) / 2,
            (scrnsize.height - frame.getHeight()) / 2, frame.getWidth(), frame.getHeight());
    frame.setVisible(true);
  }
}
