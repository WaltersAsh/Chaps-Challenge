package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


/**
 * MenuBar class for instantiating a menu bar for gui.
 *
 * @author Justin 300470389
 */
public class MenuBar extends JMenuBar {

  //menu items
  private JMenu fileMenu;
  public static JMenuItem saveMenuItem;
  public static JMenuItem loadMenuItem;

  private JMenu gameMenu;
  public static JMenuItem resumeMenuItem;
  public static JMenuItem pauseMenuItem;
  public static JMenuItem redoMenuItem;
  public static JMenuItem undoMenuItem;
  public static JMenuItem exitMenuItem;
  public static JMenuItem exitSaveMenuItem;

  private JMenu levelMenu;
  public static JMenuItem restartCurrentLevelMenuItem;
  public static JMenuItem startFirstLevelMenuItem;

  private JMenu recnplayMenu;
  public static JMenuItem playMenuItem;
  public static JMenuItem stopPlayMenuItem;
  public static JMenuItem startRecordingMenuItem;
  public static JMenuItem stopRecordingMenuItem;
  public static JMenuItem loadRecordingMenuItem;

  private JMenu helpMenu;
  public static JMenuItem showInstructMenuItem;

  /**
   * Construct a menu bar and add all menus + menu items.
   *
   * @param actionListener the action listener passed (this - in gui)
   */
  public MenuBar(ActionListener actionListener) {
    createMenuComponents(actionListener);
    add(fileMenu);
    add(gameMenu);
    add(levelMenu);
    add(recnplayMenu);
    add(helpMenu);
  }

  /**
   * Create and configure the menu items.
   *
   * @param actionListener the ActionListener to be added (this - in Gui)
   */
  public void createMenuComponents(ActionListener actionListener) {
    fileMenu = new JMenu("File");
    gameMenu = new JMenu("Game");
    levelMenu = new JMenu("Level");
    recnplayMenu = new JMenu("Rec'n'play");
    helpMenu = new JMenu("Help");

    final JMenuItem[] fileMenuItems = new JMenuItem[]{
      saveMenuItem = new JMenuItem("Save"),
      loadMenuItem = new JMenuItem("Load")
    };

    final JMenuItem[] gameMenuItems = new JMenuItem[]{
      resumeMenuItem = new JMenuItem("Resume"),
      pauseMenuItem = new JMenuItem("Pause"),
      redoMenuItem = new JMenuItem("Redo"),
      undoMenuItem = new JMenuItem("Undo"),
      exitMenuItem = new JMenuItem("Exit"),
      exitSaveMenuItem = new JMenuItem("Exit + Save")
    };

    final JMenuItem[] levelMenuItems = new JMenuItem[]{
      restartCurrentLevelMenuItem = new JMenuItem("Restart Current Level"),
      startFirstLevelMenuItem = new JMenuItem("Restart Level 1")
    };

    final JMenuItem[] recnplayMenuItems = new JMenuItem[]{
      startRecordingMenuItem = new JMenuItem("Record"),
      stopRecordingMenuItem = new JMenuItem("Stop Recording"),
      playMenuItem = new JMenuItem("Replay"),
      stopPlayMenuItem = new JMenuItem("Stop Replay"),
      loadRecordingMenuItem = new JMenuItem("Load Recording")
    };

    HashMap<JMenuItem[], JMenu> menuToMenuItems = new HashMap<>();
    menuToMenuItems.put(fileMenuItems, fileMenu);
    menuToMenuItems.put(gameMenuItems, gameMenu);
    menuToMenuItems.put(levelMenuItems, levelMenu);
    menuToMenuItems.put(recnplayMenuItems, recnplayMenu);

    final JMenuItem[][] superMenuItems = new JMenuItem[][]{
      fileMenuItems, gameMenuItems, levelMenuItems, recnplayMenuItems
    };

    for (JMenuItem[] menuItems : superMenuItems) {
      for (JMenuItem menuItem : menuItems) {
        menuItem.addActionListener(actionListener);
        JMenu menu = menuToMenuItems.get(menuItems);
        menu.add(menuItem);
      }
    }

    showInstructMenuItem = new JMenuItem("How to Play");
    showInstructMenuItem.addActionListener(actionListener);
    helpMenu.add(showInstructMenuItem);
  }
}
