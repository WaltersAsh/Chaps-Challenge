package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * RecnplayControlsPanel class for instantiating a new control panel for the gui.
 *
 * @author Justin 300470389
 */
public class RecnplayControlsPanel extends JPanel {

  private JButton lastFrameButton;
  private JButton autoPlayButton;
  private JButton nextFrameButton;
  private JButton slowerReplayButton;
  private JButton standardReplayButton;
  private JButton fasterReplayButton;

  /**
   * Instantiates a new Recnplay controls panel.
   *
   * @param actionListener the action listener
   */
  public RecnplayControlsPanel(ActionListener actionListener) {
    createRecnplayControls(actionListener);
  }

  /**
   * Create the recnplay controls for navigating and interacting.
   *
   * @param actionListener the action listener
   */
  private void createRecnplayControls(ActionListener actionListener) {
    setBackground(ComponentLibrary.lightLavender);
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    JButton[] buttons = new JButton[]{
      lastFrameButton = new JButton("<"),
      autoPlayButton = new JButton("AUTO"),
      nextFrameButton = new JButton(">"),
      slowerReplayButton = new JButton("SLOWER"),
      standardReplayButton = new JButton("STANDARD"),
      fasterReplayButton = new JButton("FASTER")
    };

    Arrays.stream(buttons).forEach(button -> {
      button.setFont(ComponentLibrary.buttonFont);
      button.setForeground(Color.WHITE);
      button.setBackground(ComponentLibrary.lavender);
      button.addActionListener(actionListener);
      add(button);
      add(Box.createRigidArea(new Dimension(50, 0)));
    });
  }

  /**
   * Gets last frame button.
   *
   * @return the last frame button
   */
  public JButton getLastFrameButton() {
    return lastFrameButton;
  }

  /**
   * Gets auto play button.
   *
   * @return the auto play button
   */
  public JButton getAutoPlayButton() {
    return autoPlayButton;
  }

  /**
   * Gets faster replay button.
   *
   * @return the faster replay button
   */
  public JButton getFasterReplayButton() {
    return fasterReplayButton;
  }

  /**
   * Gets next frame button.
   *
   * @return the next frame button
   */
  public JButton getNextFrameButton() {
    return nextFrameButton;
  }

  /**
   * Gets slower replay button.
   *
   * @return the slower replay button
   */
  public JButton getSlowerReplayButton() {
    return slowerReplayButton;
  }

  /**
   * Gets standard replay button.
   *
   * @return the standard replay button
   */
  public JButton getStandardReplayButton() {
    return standardReplayButton;
  }

}
