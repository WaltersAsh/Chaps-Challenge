package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


/**
 * A utility class used for storing and retrieving fonts, colours and icon images.
 *
 * @author Justin 300470389
 */
public class ComponentLibrary {

  //static resources to be initialised
  private static final File fontFile = new File("resources/textures/gui/font/minecraft_font.ttf");
  private static Font font = new Font("Arial", Font.PLAIN, 24);
  private static Icon pausedIc = new ImageIcon();
  private static Icon exitIc = new ImageIcon();

  //instantiate resources
  static {
    try {
      font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
      Image image = ImageIO.read(new File("resources/textures/gui/paused.png"));
      image = image.getScaledInstance(103, 42, Image.SCALE_DEFAULT);
      pausedIc = new ImageIcon(image);
      image = ImageIO.read(new File("resources/textures/gui/quit.png"));
      image = image.getScaledInstance(103, 42, Image.SCALE_DEFAULT);
      exitIc = new ImageIcon(image);
    } catch (FontFormatException | IOException e) {
      e.printStackTrace();
    }
  }

  //Icons
  public static final Icon pausedIcon = pausedIc;
  public static final Icon exitIcon = exitIc;

  //Fonts
  public static final Font buttonFont = font.deriveFont(Font.PLAIN, 20);
  public static final Font sideFont = font.deriveFont(Font.PLAIN, 11);
  public static final Font bigFont = font.deriveFont(Font.BOLD, 40);
  public static final Font infoFont = font.deriveFont(Font.PLAIN, 40);
  public static final Font titleScreenFont = font.deriveFont(Font.BOLD, 95);
  public static final Font bodyFont = font.deriveFont(Font.PLAIN, 13);

  //Colours
  public static final Color lavender = new Color(74, 29, 138);
  public static final Color lightLavender = new Color(179, 159, 207);
  public static final Color darkLavender = new Color(50, 38, 66);
  public static final Color deepLavender = new Color(85, 52, 130);
  public static final Color fullLavender = new Color(102, 0, 255);
  public static final Color paleLavender = new Color(237, 224, 255);

  //demo files
  public static final File collectDemo = new File(
          "resources/textures/gui/instructions/collect.gif");
  public static final File crateDemo = new File("resources/textures/gui/instructions/crate.gif");
  public static final File enemiesDemo = new File(
          "resources/textures/gui/instructions/enemies.gif");
  public static final File moveDemo = new File("resources/textures/gui/instructions/movement.gif");
  public static final File portalDemo = new File("resources/textures/gui/instructions/portal.gif");
  public static final File unlockDemo = new File("resources/textures/gui/instructions/unlock.gif");

  /**
   * Create and return a icon/indicator for recording and return.
   */
  public static JLabel recordingIconLabel() {
    JLabel recordingIconLabel = new JLabel();
    try {
      Image image = ImageIO.read(new File("resources/textures/gui/rec-icon.jpg"));
      ImageIcon icon = new ImageIcon(image.getScaledInstance(100, 100, Image.SCALE_DEFAULT));
      recordingIconLabel.setIcon(icon);
    } catch (IOException e) {
      e.printStackTrace();
    }
    recordingIconLabel.setBounds(-10, -455, 1000, 1000);
    recordingIconLabel.setVisible(false);
    return recordingIconLabel;
  }

  /**
   * Create and return an icon/indicator for pausing.
   *
   * @return the pausediconLabel JLabel
   */
  public static JLabel pausedIconLabel() {
    JLabel pausedIconLabel = new JLabel();
    try {
      Image image = ImageIO.read(new File("resources/textures/gui/paused.png"));
      ImageIcon icon = new ImageIcon(image.getScaledInstance(256, 83, Image.SCALE_DEFAULT));
      pausedIconLabel.setIcon(icon);
    } catch (IOException e) {
      e.printStackTrace();
    }
    pausedIconLabel.setBounds(-20, -465, 1000, 1000);
    pausedIconLabel.setVisible(false);
    return pausedIconLabel;
  }

  /**
   * Create and return an info field label.
   *
   * @return the info field JLabel
   */
  public static JLabel infoFieldLabel() {
    JLabel infoFieldLabel = null;
    try {
      Image sign = ImageIO.read(new File("resources/textures/gui/sign_large.png"));
      infoFieldLabel = new JLabel(
              new ImageIcon(sign.getScaledInstance(500, 500, Image.SCALE_DEFAULT)));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    assert infoFieldLabel != null;
    infoFieldLabel.setBounds(-180, -90, 1000, 1000);
    infoFieldLabel.setVisible(false);
    return infoFieldLabel;
  }

  /**
   * Create and return an info field text label.
   *
   * @return the info field text JLabel
   */
  public static JLabel infoFieldTextLabel() {
    JLabel infoFieldTextLabel = new JLabel();
    infoFieldTextLabel.setBounds(150, -225, 1000, 1000);
    infoFieldTextLabel.setFont(ComponentLibrary.infoFont);
    infoFieldTextLabel.setForeground(Color.BLACK);
    infoFieldTextLabel.setVisible(false);
    return infoFieldTextLabel;
  }

  /**
   * Create and return a background image label.
   *
   * @return the background image JLabel.
   */
  public static JLabel backgroundImageLabel() {
    File backgroundFile = new File("resources/textures/gui/background.jpg");
    ImageIcon backgroundIcon = new ImageIcon(String.valueOf(backgroundFile));
    JLabel background = new JLabel("", backgroundIcon, JLabel.CENTER);
    background.setBounds(0, 0, 1280, 800);
    background.setVisible(true);
    return background;
  }

}
