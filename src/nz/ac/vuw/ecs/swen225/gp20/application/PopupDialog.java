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

  /**
   * Dialog states upon dialog required to be shown.
   */
  public enum DialogState {
    LEVEL_COMPLETE,
    TIME_EXPIRED,
    DEATH
  }

  private final JPanel buttonPanel;
  private JPanel messagePanel;
  private JLabel messageLabel;

  private JButton restartButton;
  private JButton nextButton;

  private Border border;

  /**
   * Instantiates a new Popup dialog for the gui.
   *
   * @param state the enum representing which dialog should be presented.
   * @param actionListener the action listener to be added
   */
  public PopupDialog(DialogState state, ActionListener actionListener) {
    setLayout(new GridBagLayout());
    messagePanel = new JPanel();
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

    border = BorderFactory.createEmptyBorder(5, 5, 5, 5);
    border = BorderFactory.createCompoundBorder(border,
            BorderFactory.createLineBorder(Color.WHITE, 2));

    switch (state) {
      case LEVEL_COMPLETE:
        levelFinishDialog(actionListener);
        break;
      case TIME_EXPIRED:
        messageDialog(actionListener, " TIME EXPIRED ");
        break;
      case DEATH:
        messageDialog(actionListener, " YOU DIED ");
        break;
      default:
        break;
    }

    //transparent background
    setUndecorated(true);
    getRootPane().setOpaque(false);
    getContentPane().setBackground(new Color(0, 0, 0, 0));
    setBackground(new Color(0, 0, 0, 0));

    messagePanel.add(messageLabel);
    buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(Box.createRigidArea(new Dimension(0, 50)));
    add(messagePanel, gbc);
    add(Box.createRigidArea(new Dimension(0, 75)));
    add(buttonPanel, gbc);
    stylise();
    pack();
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension scrnsize = toolkit.getScreenSize();
    setBounds((scrnsize.width - getWidth()) / 2,
            (scrnsize.height - getHeight()) / 2, getWidth(), getHeight());
    setAlwaysOnTop(true);
  }

  /**
   * Dialog for finished level.
   *
   * @param actionListener the passed actionlistener
   */
  private void levelFinishDialog(ActionListener actionListener) {
    messageLabel = new JLabel(" LEVEL COMPLETE ");
    restartButton = new JButton(" Restart Level ");
    nextButton = new JButton(" Next Level ");
    buttonPanel.add(restartButton);
    buttonPanel.add(Box.createRigidArea(new Dimension(50, 0)));
    buttonPanel.add(nextButton);
    nextButton.setBackground(ComponentLibrary.fullLavender);
    nextButton.setForeground(Color.WHITE);
    nextButton.setFont(ComponentLibrary.buttonFont);
    nextButton.setBorder(border);
    nextButton.addActionListener(actionListener);
    restartButton.addActionListener(actionListener);
  }

  /**
   * Dialog for countdown timer expiry.
   *
   * @param actionListener the actionlistener to be added.
   * @param text the text to be set for the message label initialised.
   */
  private void messageDialog(ActionListener actionListener, String text) {
    messageLabel = new JLabel(text);
    restartButton = new JButton(" Restart Level ");
    restartButton.addActionListener(actionListener);
    buttonPanel.add(restartButton);
  }

  /**
   * Stylise the dialog.
   */
  private void stylise() {
    messagePanel.setBackground(ComponentLibrary.fullLavender);
    messagePanel.setBorder(border);
    buttonPanel.setBackground(new Color(0, 0, 0, 0));
    messageLabel.setForeground(Color.BLACK);
    restartButton.setBackground(ComponentLibrary.fullLavender);
    restartButton.setForeground(Color.WHITE);
    restartButton.setBorder(border);
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
