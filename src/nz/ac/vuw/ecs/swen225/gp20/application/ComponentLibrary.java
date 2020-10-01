package nz.ac.vuw.ecs.swen225.gp20.application;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * A utility class used for storing and retrieving fonts, colours and icon images.
 */
public class ComponentLibrary {

  // Text sizes
  public static final Font regText = new Font("", Font.PLAIN, 25);
  public static final Font bigText = new Font("", Font.BOLD, 45);

  //fonts
  private static final File fontFile = new File("resources/textures/gui/font/minecraft_font.ttf");
  private static Font font = null;

  static {
    try {
      font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
    } catch (FontFormatException | IOException e) {
      e.printStackTrace();
    }
  }

  public static final Font buttonFont = font.deriveFont(Font.PLAIN, 20);
  public static final Font sideFont = font.deriveFont(Font.PLAIN, 11);
  public static final Font bigFont = font.deriveFont(Font.BOLD, 40);
  public static final Font infoFont = font.deriveFont(Font.PLAIN, 40);

  //Colours
  public static final Color lavender = new Color(74, 29, 138);
  public static final Color lightLavender = new Color(179, 159, 207);
  public static final Color darkLavender = new Color(50, 38, 66);
  public static final Color deepLavender = new Color(85, 52, 130);
  public static final Color fullLavender = new Color(102, 0, 255);
  public static final Color paleLavender = new Color(237, 224, 255);

  /**
   * Create and return a icon/indicator for recording and return.
   */
  public static JLabel initialiseRecnplayIconLabel() {
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
   */
  public static JLabel initialisePauseIconLabel() {
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

}
