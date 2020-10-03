package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * PopupDialog class for instantiating a new popup box to the gui.
 *
 * @author Justin 300470389
 */
public class PopupDialog extends JDialog {

  private JPanel buttonPanel;
  private JLabel messageLabel;
  private JButton restartButton;
  private JButton nextButton;

  /**
   * Instantiates a new Popup dialog for the gui.
   *
   * @param isLevelComplete boolean confirming level is complete
   */
  public PopupDialog(boolean isLevelComplete) {
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    if (isLevelComplete) {
      levelFinishDialog();
    } else {
      timerCountdownDialog();
    }
    buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(Box.createRigidArea(new Dimension(0, 50)));
    add(messageLabel, gbc);
    add(Box.createRigidArea(new Dimension(0, 100)));
    add(buttonPanel, gbc);

    stylise();
    setBackground(ComponentLibrary.deepLavender);
    setSize(300, 200);
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension scrnsize = toolkit.getScreenSize();
    setBounds((scrnsize.width - getWidth()) / 2,
            (scrnsize.height - getHeight()) / 2, getWidth(), getHeight());
  }

  /**
   * Dialog for finished level.
   */
  public void levelFinishDialog() {
    messageLabel = new JLabel("LEVEL COMPLETE");
    restartButton = new JButton("Restart Level");
    nextButton = new JButton("Next Level");
    buttonPanel.add(restartButton);
    buttonPanel.add(Box.createRigidArea(new Dimension(50, 0)));
    buttonPanel.add(nextButton);
    nextButton.setBackground(ComponentLibrary.fullLavender);
    nextButton.setForeground(Color.WHITE);
  }

  /**
   * Dialog for countdown timer expiry.
   */
  public void timerCountdownDialog() {
    messageLabel = new JLabel("COUNTDOWN TIMER EXPIRED");
    restartButton = new JButton("Restart Level");
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
  }
}
