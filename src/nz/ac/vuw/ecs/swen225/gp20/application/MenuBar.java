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
  private JMenuItem saveMenuItem;
  private JMenuItem loadMenuItem;

  private JMenu gameMenu;
  private JMenuItem resumeMenuItem;
  private JMenuItem pauseMenuItem;
  private JMenuItem redoMenuItem;
  private JMenuItem undoMenuItem;
  private JMenuItem exitMenuItem;
  private JMenuItem exitSaveMenuItem;

  private JMenu levelMenu;
  private JMenuItem restartCurrentLevelMenuItem;
  private JMenuItem startFirstLevelMenuItem;

  private JMenu recnplayMenu;
  private JMenuItem playMenuItem;
  private JMenuItem stopPlayMenuItem;
  private JMenuItem startRecordingMenuItem;
  private JMenuItem stopRecordingMenuItem;
  private JMenuItem loadRecordingMenuItem;

  private JMenu helpMenu;
  private JMenuItem showInstructMenuItem;

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

    JMenuItem[] fileMenuItems = new JMenuItem[]{
      saveMenuItem = new JMenuItem("Save"),
      loadMenuItem = new JMenuItem("Load")
    };

    JMenuItem[] gameMenuItems = new JMenuItem[]{
      resumeMenuItem = new JMenuItem("Resume"),
      pauseMenuItem = new JMenuItem("Pause"),
      redoMenuItem = new JMenuItem("Redo"),
      undoMenuItem = new JMenuItem("Undo"),
      exitMenuItem = new JMenuItem("Exit"),
      exitSaveMenuItem = new JMenuItem("Exit + Save")
    };

    JMenuItem[] levelMenuItems = new JMenuItem[]{
      restartCurrentLevelMenuItem = new JMenuItem("Restart Current Level"),
      startFirstLevelMenuItem = new JMenuItem("Restart Level 1")
    };

    JMenuItem[] recnplayMenuItems = new JMenuItem[]{
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

  /**
   * Gets save menu item.
   *
   * @return the save menu item
   */
  public JMenuItem getSaveMenuItem() {
    return saveMenuItem;
  }

  /**
   * Gets load menu item.
   *
   * @return the load menu item
   */
  public JMenuItem getLoadMenuItem() {
    return loadMenuItem;
  }

  /**
   * Gets exit menu item.
   *
   * @return the exit menu item
   */
  public JMenuItem getExitMenuItem() {
    return exitMenuItem;
  }

  /**
   * Gets pause menu item.
   *
   * @return the pause menu item
   */
  public JMenuItem getPauseMenuItem() {
    return pauseMenuItem;
  }

  /**
   * Gets play menu item.
   *
   * @return the play menu item
   */
  public JMenuItem getPlayMenuItem() {
    return playMenuItem;
  }

  /**
   * Gets redo menu item.
   *
   * @return the redo menu item
   */
  public JMenuItem getRedoMenuItem() {
    return redoMenuItem;
  }

  /**
   * Gets resume menu item.
   *
   * @return the resume menu item
   */
  public JMenuItem getResumeMenuItem() {
    return resumeMenuItem;
  }

  /**
   * Gets undo menu item.
   *
   * @return the undo menu item
   */
  public JMenuItem getUndoMenuItem() {
    return undoMenuItem;
  }

  /**
   * Gets exit save menu item.
   *
   * @return the exit save menu item
   */
  public JMenuItem getExitSaveMenuItem() {
    return exitSaveMenuItem;
  }

  /**
   * Gets load recording menu item.
   *
   * @return the load recording menu item
   */
  public JMenuItem getLoadRecordingMenuItem() {
    return loadRecordingMenuItem;
  }

  /**
   * Gets start recording menu item.
   *
   * @return the start recording menu item
   */
  public JMenuItem getStartRecordingMenuItem() {
    return startRecordingMenuItem;
  }

  /**
   * Gets show instruct menu item.
   *
   * @return the show instruct menu item
   */
  public JMenuItem getShowInstructMenuItem() {
    return showInstructMenuItem;
  }

  /**
   * Gets stop play menu item.
   *
   * @return the stop play menu item
   */
  public JMenuItem getStopPlayMenuItem() {
    return stopPlayMenuItem;
  }

  /**
   * Gets stop recording menu item.
   *
   * @return the stop recording menu item
   */
  public JMenuItem getStopRecordingMenuItem() {
    return stopRecordingMenuItem;
  }

  /**
   * Gets restart current level menu item.
   *
   * @return the restart current level menu item
   */
  public JMenuItem getRestartCurrentLevelMenuItem() {
    return restartCurrentLevelMenuItem;
  }

  /**
   * Gets start first level menu item.
   *
   * @return the start first level menu item
   */
  public JMenuItem getStartFirstLevelMenuItem() {
    return startFirstLevelMenuItem;
  }
}
