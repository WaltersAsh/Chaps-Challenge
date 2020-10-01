package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.rendering.BoardView;


/**
 * BoardPanel class for instantiating a new board panel for the gui.
 *
 * @author Justin 300470389
 */
public class BoardPanel extends JLayeredPane {

  private BoardView board;
  private JLabel infoFieldLabel;
  private JLabel infoFieldTextLabel;
  private JLabel recordingIconLabel;
  private JLabel pausedIconLabel;

  /**
   * Construct a board panel and add all required components.
   *
   * @param maze the maze
   */
  public BoardPanel(Maze maze) {
    createBoardPanel(maze);
    infoFieldLabel = ComponentLibrary.infoFieldLabel();
    infoFieldTextLabel = (ComponentLibrary.infoFieldTextLabel());
    recordingIconLabel = ComponentLibrary.recordingIconLabel();
    pausedIconLabel = ComponentLibrary.pausedIconLabel();

    board.setBounds(0, 0, 1000, 1000);
    add(board, JLayeredPane.DEFAULT_LAYER);
    add(infoFieldLabel, JLayeredPane.PALETTE_LAYER);
    add(infoFieldTextLabel, JLayeredPane.MODAL_LAYER);
    add(recordingIconLabel, JLayeredPane.PALETTE_LAYER);
    add(pausedIconLabel, JLayeredPane.PALETTE_LAYER);
  }

  /**
   * Create the board panel and rendered board.
   */
  public void createBoardPanel(Maze maze) {
    board = new BoardView(maze);
    this.setBackground(ComponentLibrary.lightLavender);
    this.setMinimumSize(
            new Dimension(board.getPreferredSize().width, board.getPreferredSize().height));
    this.setPreferredSize(
            new Dimension(board.getPreferredSize().width, board.getPreferredSize().height));
    this.setMaximumSize(new Dimension(800, 800));
  }

  /**
   * Get the board view.
   *
   * @return the BoardView
   */
  public BoardView getBoard() {
    return board;
  }

  /**
   * Get the info field label.
   *
   * @return the info field JLabel
   */
  public JLabel getInfoFieldLabel() {
    return infoFieldLabel;
  }

  /**
   * Get the info field text label.
   *
   * @return the info field text JLabel
   */
  public JLabel getInfoFieldTextLabel() {
    return infoFieldTextLabel;
  }

  /**
   * Get the recording icon label.
   *
   * @return the recording icon text JLabel
   */
  public JLabel getRecordingIconLabel() {
    return recordingIconLabel;
  }

  /**
   * Get the paused icon label.
   *
   * @return the paused icon JLabel.
   */
  public JLabel getPausedIconLabel() {
    return pausedIconLabel;
  }
}
