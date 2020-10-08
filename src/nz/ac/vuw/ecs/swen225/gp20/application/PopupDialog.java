package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * PopupDialog class for instantiating a new popup box to the gui.
 *
 * @author Justin 300470389
 */
public class PopupDialog extends JDialog {

  public enum DialogState {
    LEVEL_COMPLETE,
    TIME_EXPIRED,
    DEATH
  }

  private final JPanel buttonPanel;
  private JLabel messageLabel;

  private JButton restartButton;
  private JButton nextButton;

  /**
   * Instantiates a new Popup dialog for the gui.
   *
   * @param state the enum representing which dialog should be presented.
   * @param actionListener the action listener to be added
   */
  public PopupDialog(DialogState state, ActionListener actionListener) {
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    switch (state) {
      case LEVEL_COMPLETE -> levelFinishDialog(actionListener);
      case TIME_EXPIRED -> messageDialog(actionListener, "TIME EXPIRED");
      case DEATH -> messageDialog(actionListener, "YOU DIED");

    }
    buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(Box.createRigidArea(new Dimension(0, 50)));
    add(messageLabel, gbc);
    add(Box.createRigidArea(new Dimension(0, 75)));
    add(buttonPanel, gbc);

    stylise();
    setBackground(ComponentLibrary.deepLavender);
    Border border = BorderFactory.createEmptyBorder(15, 15, 15, 15);
    border = BorderFactory.createCompoundBorder(border,
            BorderFactory.createLineBorder(Color.WHITE, 2));
    border = BorderFactory.createCompoundBorder(border,
            BorderFactory.createEmptyBorder(15, 15, 15, 15));
    getRootPane().setBorder(border);
    pack();
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension scrnsize = toolkit.getScreenSize();
    setBounds((scrnsize.width - getWidth()) / 2,
            (scrnsize.height - getHeight()) / 2, getWidth(), getHeight());
  }

  /**
   * Dialog for finished level.
   */
  public void levelFinishDialog(ActionListener actionListener) {
    messageLabel = new JLabel(" LEVEL COMPLETE ");
    restartButton = new JButton("Restart Level");
    nextButton = new JButton("Next Level");
    buttonPanel.add(restartButton);
    buttonPanel.add(Box.createRigidArea(new Dimension(50, 0)));
    buttonPanel.add(nextButton);
    nextButton.setBackground(ComponentLibrary.fullLavender);
    nextButton.setForeground(Color.WHITE);
    nextButton.setFont(ComponentLibrary.buttonFont);
    nextButton.addActionListener(actionListener);
    restartButton.addActionListener(actionListener);
  }

  /**
   * Dialog for countdown timer expiry.
   *
   * @param actionListener the actionlistener to be added.
   * @param text the text to be set for the message label initialised.
   */
  public void messageDialog(ActionListener actionListener, String text) {
    messageLabel = new JLabel(" COUNTDOWN TIMER EXPIRED ");
    restartButton = new JButton("Restart Level");
    restartButton.addActionListener(actionListener);
    buttonPanel.add(restartButton);
  }

  /**
   * Stylise the dialog.
   */
  public void stylise() {
    getContentPane().setBackground(ComponentLibrary.lightLavender);
    buttonPanel.setBackground(ComponentLibrary.lightLavender);
    messageLabel.setForeground(Color.BLACK);
    restartButton.setBackground(ComponentLibrary.fullLavender);
    restartButton.setForeground(Color.WHITE);
    messageLabel.setFont(ComponentLibrary.bigFont);
    restartButton.setFont(ComponentLibrary.buttonFont);
  }

  /**
   * Get the next button.
   *
   * @return the next JButton
   */
  public JButton getNextButton() {
    return nextButton;
  }

  /**
   * Get the restart button.
   *
   * @return the restart JButton
   */
  public JButton getRestartButton() {
    return restartButton;
  }
}
