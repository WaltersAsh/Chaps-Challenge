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
 * A class used for storing and retrieving fonts, colours and icon images.
 * Follows the Singleton design pattern
 * https://en.wikipedia.org/wiki/Singleton_pattern
 *
 * @author Justin Joe 300470389
 */
public class ComponentLibrary {

  private final File fontFile = new File("resources/textures/gui/font/minecraft_font.ttf");
  private Font font = new Font("Arial", Font.PLAIN, 24);

  //Icons
  public Icon pausedIcon;
  public Icon exitIcon;

  //Fonts
  public Font buttonFont;
  public Font sideFont;
  public Font bigFont;
  public Font infoFont;
  public Font titleScreenFont;
  public Font bodyFont;

  //Colours
  public Color lavender;
  public Color lightLavender;
  public Color darkLavender;
  public Color deepLavender;
  public Color fullLavender;
  public Color paleLavender;

  //demo files
  public final File collectDemo = new File(
          "resources/textures/gui/instructions/collect.gif");
  public final File crateDemo = new File("resources/textures/gui/instructions/crate.gif");
  public final File enemiesDemo = new File(
          "resources/textures/gui/instructions/enemies.gif");
  public final File moveDemo = new File("resources/textures/gui/instructions/movement.gif");
  public final File portalDemo = new File("resources/textures/gui/instructions/portal.gif");
  public final File unlockDemo = new File("resources/textures/gui/instructions/unlock.gif");

  //only a single instance of this object is instantiated
  static ComponentLibrary cl = new ComponentLibrary();

  /**
   * The component library is instantiated by initialising colours, fonts and icons.
   */
  private ComponentLibrary() {
    initialiseColours();
    initialiseFonts();
    initialiseIcons();
  }

  /**
   * Get the only instance of the ComponentLibrary.
   *
   * @return the ComponentLibrary object
   */
  static ComponentLibrary getInstance() {
    return cl;
  }

  /**
   * Setup fonts.
   */
  private void initialiseFonts() {
    try {
      font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
    } catch (IOException | FontFormatException e) {
      e.printStackTrace();
    }
    buttonFont = font.deriveFont(Font.PLAIN, 20);
    sideFont = font.deriveFont(Font.PLAIN, 11);
    bigFont = font.deriveFont(Font.BOLD, 40);
    infoFont = font.deriveFont(Font.PLAIN, 40);
    titleScreenFont = font.deriveFont(Font.BOLD, 95);
    bodyFont = font.deriveFont(Font.PLAIN, 13);
  }

  /**
   * Setup colours.
   */
  private void initialiseColours() {
    lavender = new Color(74, 29, 138);
    lightLavender = new Color(179, 159, 207);
    darkLavender = new Color(50, 38, 66);
    deepLavender = new Color(85, 52, 130);
    fullLavender = new Color(102, 0, 255);
    paleLavender = new Color(237, 224, 255);
  }

  /**
   * Setup icons.
   */
  private void initialiseIcons() {
    try {
      Image image = ImageIO.read(new File("resources/textures/gui/paused.png"));
      image = image.getScaledInstance(103, 42, Image.SCALE_DEFAULT);
      pausedIcon = new ImageIcon(image);
      image = ImageIO.read(new File("resources/textures/gui/quit.png"));
      image = image.getScaledInstance(103, 42, Image.SCALE_DEFAULT);
      exitIcon = new ImageIcon(image);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Create and return a icon/indicator for recording.
   *
   * @return the JLabel as an indicator for recording
   */
  public JLabel recordingIconLabel() {
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
   * Create and return a icon/indicator for replaying.
   *
   * @return the JLabel as an indicator for replaying
   */
  public JLabel replayingIconLabel() {
    JLabel replayingIconLabel = new JLabel();
    try {
      Image image = ImageIO.read(new File("resources/textures/gui/replay.png"));
      ImageIcon icon = new ImageIcon(image.getScaledInstance(220, 83, Image.SCALE_DEFAULT));
      replayingIconLabel.setIcon(icon);
    } catch (IOException e) {
      e.printStackTrace();
    }
    replayingIconLabel.setBounds(-10, -465, 1000, 1000);
    replayingIconLabel.setVisible(false);
    return replayingIconLabel;
  }

  /**
   * Create and return an icon/indicator for pausing.
   *
   * @return the JLabel as an indicator for pausing
   */
  public JLabel pausedIconLabel() {
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
  public JLabel infoFieldLabel() {
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
  public JLabel infoFieldTextLabel() {
    JLabel infoFieldTextLabel = new JLabel();
    infoFieldTextLabel.setBounds(150, -225, 1000, 1000);
    infoFieldTextLabel.setFont(cl.infoFont);
    infoFieldTextLabel.setForeground(Color.BLACK);
    infoFieldTextLabel.setVisible(false);
    return infoFieldTextLabel;
  }
}
